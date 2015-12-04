package de.uniol.inf.is.odysseus.net.source;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.FileHandler;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryProvider;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryListener;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDatadictionaryProviderListener;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.IQueryParser;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.CreateQueryCommand;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;
import de.uniol.inf.is.odysseus.net.data.DistributedDataException;
import de.uniol.inf.is.odysseus.net.data.IDistributedData;
import de.uniol.inf.is.odysseus.net.data.IDistributedDataListener;
import de.uniol.inf.is.odysseus.net.data.IDistributedDataManager;
import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLGenerator;

public class DistributedDataSourceManager implements IDistributedDataListener, IDatadictionaryProviderListener, IDataDictionaryListener {

	private static final Logger LOG = LoggerFactory.getLogger(DistributedDataSourceManager.class);
	private static final String DATA_SOURCE_DISTRIBUTION_NAME = "net.data.source";

	private static IDistributedDataManager dataManager;
	private static IPQLGenerator pqlGenerator;
	private static IQueryParser pqlParser;

	private static ISession currentSession;

	private final Map<String, UUID> createdSourcesMap = Maps.newHashMap();
	private final BiMap<UUID, String> importedSourcesBiMap = HashBiMap.create();

	// called by OSGi-DS
	public void bindDistributedDataManager(IDistributedDataManager serv) {
		dataManager = serv;

		dataManager.addListener(this);
	}

	// called by OSGi-DS
	public void unbindDistributedDataManager(IDistributedDataManager serv) {
		if (dataManager == serv) {
			dataManager.removeListener(this);

			dataManager = null;
		}
	}

	// called by OSGi-DS
	public static void bindPQLGenerator(IPQLGenerator serv) {
		pqlGenerator = serv;
	}

	// called by OSGi-DS
	public static void unbindPQLGenerator(IPQLGenerator serv) {
		if (pqlGenerator == serv) {
			pqlGenerator = null;
		}
	}

	// called by OSGi
	public static void bindParser(IQueryParser serv) {
		if (serv.getLanguage().equalsIgnoreCase("PQL")) {
			pqlParser = serv;
		}
	}

	// called by OSGi
	public static void unbindParser(IQueryParser serv) {
		if (serv == pqlParser) {
			pqlParser = null;
		}
	}

	@Override
	public void distributedDataManagerStarted(IDistributedDataManager sender) {
		ITenant tenant = UserManagementProvider.getDefaultTenant();
		DataDictionaryProvider.subscribe(tenant, this);

		IDataDictionary dd = DataDictionaryProvider.getDataDictionary(tenant);
		if (dd != null) {
			newDatadictionary(dd);
		}
	}

	@Override
	public void distributedDataAdded(IDistributedDataManager sender, IDistributedData addedData) {
		if (addedData.getName().equals(DATA_SOURCE_DISTRIBUTION_NAME) && !addedData.isCreatorLocal()) {
			try {
				JSONObject data = addedData.getData();

				String sourceName = data.getString("name");
				String userName = data.getString("user");
				String pqlStatement = data.getString("pql");

				Optional<ILogicalOperator> optLogicalOp = parsePQLStatement(sourceName, pqlStatement);
				if (optLogicalOp.isPresent()) {
					synchronized (importedSourcesBiMap) {
						importedSourcesBiMap.put(addedData.getUUID(), userName + "." + sourceName);
					}
					getDataDictionary().setStream(sourceName, optLogicalOp.get(), getActiveSession());
				} else {
					LOG.error("No data source from distributed data created");
				}

			} catch (JSONException e) {
				LOG.error("Could not process json-data", e);
			}
		}
	}

	@Override
	public void distributedDataModified(IDistributedDataManager sender, IDistributedData oldData, IDistributedData newData) {
		distributedDataRemoved(sender, oldData);
		distributedDataAdded(sender, newData);
	}

	@Override
	public void distributedDataRemoved(IDistributedDataManager sender, IDistributedData removedData) {
		if (removedData.getName().equals(DATA_SOURCE_DISTRIBUTION_NAME) && !removedData.isCreatorLocal()) {
			String fullSourceName = importedSourcesBiMap.get(removedData.getUUID());
			if (!Strings.isNullOrEmpty(fullSourceName)) {
				synchronized (importedSourcesBiMap) {
					importedSourcesBiMap.remove(removedData.getUUID());
				}
				getDataDictionary().removeViewOrStream(fullSourceName, getActiveSession());
			}
		}
	}

	@Override
	public void distributedDataManagerStopped(IDistributedDataManager sender) {
		DataDictionaryProvider.unsubscribe(this);
	}

	@Override
	public void newDatadictionary(IDataDictionary dd) {
		dd.addListener(this);

		Set<Entry<Resource, ILogicalOperator>> streamsAndViews = dd.getStreamsAndViews(getActiveSession());
		for (Entry<Resource, ILogicalOperator> streamOrView : streamsAndViews) {
			String name = streamOrView.getKey().getResourceName();
			String username = streamOrView.getKey().getUser();

			ILogicalOperator operator = streamOrView.getValue();

			createDistributedData(name, operator, username);
		}
	}

	@Override
	public void removedDatadictionary(IDataDictionary dd) {
		dd.removeListener(this);

		Set<Entry<Resource, ILogicalOperator>> streamsAndViews = dd.getStreamsAndViews(getActiveSession());
		for (Entry<Resource, ILogicalOperator> streamOrView : streamsAndViews) {
			String name = streamOrView.getKey().getResourceName();
			String username = streamOrView.getKey().getUser();

			destroyDistributedData(name, username);
		}
	}

	@Override
	public void addedViewDefinition(IDataDictionary sender, String name, ILogicalOperator op, boolean isView, ISession session) {
		synchronized (importedSourcesBiMap) {
			if (!importedSourcesBiMap.containsValue(name)) {
				createDistributedData(name, op, session.getUser().getName());
			}
		}
	}

	@Override
	public void removedViewDefinition(IDataDictionary sender, String name, ILogicalOperator op, boolean isView, ISession session) {
		synchronized (importedSourcesBiMap) {
			if (!importedSourcesBiMap.containsValue(name)) {
				destroyDistributedData(name, session.getUser().getName());
			}
		}
	}

	private void createDistributedData(String name, ILogicalOperator op, String username) {
		String realSourceName = removeUserFromName(name);
		String pqlStatement = pqlGenerator.generatePQLStatement(op);

		try {
			JSONObject json = new JSONObject();
			json.put("name", realSourceName);
			json.put("user", username);
			json.put("pql", pqlStatement);

			IDistributedData distributedData = dataManager.create(json, DATA_SOURCE_DISTRIBUTION_NAME, false);
			synchronized (createdSourcesMap) {
				createdSourcesMap.put(username + "." + realSourceName, distributedData.getUUID());
			}

		} catch (JSONException e) {
			LOG.error("Could not create json object", e);
		} catch (DistributedDataException e) {
			LOG.error("Could not create distributed data", e);
		}
	}

	private void destroyDistributedData(String name, String username) {
		String realSourceName = removeUserFromName(name);

		synchronized (createdSourcesMap) {
			UUID uuid = createdSourcesMap.get(username + "." + realSourceName);
			if (uuid != null) {
				try {
					dataManager.destroy(uuid);
					createdSourcesMap.remove(username + "." + realSourceName);
				} catch (DistributedDataException e) {
					LOG.error("Could not destroy distributed data for source {}", name);
				}
			}
		}
	}

	@Override
	public void dataDictionaryChanged(IDataDictionary sender) {
		// do nothing
	}

	private static IDataDictionaryWritable getDataDictionary() {
		return (IDataDictionaryWritable) DataDictionaryProvider.getDataDictionary(getActiveSession().getTenant());
	}

	private static String removeUserFromName(String streamName) {
		int pos = streamName.indexOf(".");
		if (pos >= 0) {
			return streamName.substring(pos + 1);
		}
		return streamName;
	}

	private static ISession getActiveSession() {
		if (currentSession == null || !currentSession.isValid()) {
			currentSession = UserManagementProvider.getSessionmanagement().loginSuperUser(null, UserManagementProvider.getDefaultTenant().getName());
		}
		return currentSession;
	}

	private static Optional<ILogicalOperator> parsePQLStatement(String sourcename, String pqlStatement) {
		if (pqlParser == null) {
			LOG.error("No pql-parser bound!");
			return Optional.absent();
		}

		try {
			LOG.debug("Parsing with pql-Parser");

			IMetaAttribute metaAttribute = null;
			List<IExecutorCommand> commands = pqlParser.parse(pqlStatement, getActiveSession(), getDataDictionary(), Context.empty(), metaAttribute, null);
			ILogicalQuery query = null;

			for (IExecutorCommand cmd : commands) {

				if (!(cmd instanceof CreateQueryCommand)) {
					LOG.error("PQL parser did return an executor command, which is not a create query command!");
					continue;
				}

				CreateQueryCommand createCmd = (CreateQueryCommand) cmd;
				if (query != null) {
					LOG.error("PQLParser returned multiple logical queries");
					return Optional.absent();
				}
				query = createCmd.getQuery();
			}

			if (query != null) {
				Collection<File> files = determineNeededFiles(query);
				LOG.info("Query is dependent from {} files.", files.size());
				for (File file : files) {
					if (!file.exists()) {
						LOG.error("Could not create datasource since the specified file '" + file.getName() + "' does not exist.");
						return Optional.absent();
					}
				}

				ILogicalOperator op = query.getLogicalPlan();
				SDFSchema outputSchema = op.getOutputSchema();
				// outputSchema.setMetaSchema(advertisement.getMetaSchemata());
				op.setOutputSchema(outputSchema);
				return Optional.of(op);
				// getDataDictionary().setStream(sourcename,
				// query.getLogicalPlan(), getActiveSession());
			}
			LOG.error("Could not import '" + sourcename + "' since the parser did not return a runnable query");
		} catch (QueryParseException e) {
			LOG.error("Could not import source {}", sourcename, e);
		}

		return Optional.absent();
	}

	private static Collection<File> determineNeededFiles(ILogicalQuery query) {
		Collection<ILogicalOperator> operators = getAllOperators(query);
		Collection<File> result = Lists.newArrayList();

		for (ILogicalOperator operator : operators) {
			if (operator instanceof AbstractAccessAO) {
				AbstractAccessAO fileAccess = (AbstractAccessAO) operator;

				String filename = fileAccess.getOptionsMap().get(FileHandler.FILENAME);
				if (!Strings.isNullOrEmpty(filename)) {
					result.add(new File(filename));
				}
			}
		}

		return result;
	}

	private static Collection<ILogicalOperator> getAllOperators(ILogicalQuery plan) {
		return getAllOperators(plan.getLogicalPlan());
	}

	private static Collection<ILogicalOperator> getAllOperators(ILogicalOperator operator) {
		List<ILogicalOperator> operators = Lists.newArrayList();
		collectOperatorsImpl(operator, operators);
		return operators;
	}

	private static void collectOperatorsImpl(ILogicalOperator currentOperator, Collection<ILogicalOperator> list) {
		if (!list.contains(currentOperator)) {
			list.add(currentOperator);
			for (final LogicalSubscription subscription : currentOperator.getSubscriptions()) {
				collectOperatorsImpl(subscription.getTarget(), list);
			}

			for (final LogicalSubscription subscription : currentOperator.getSubscribedToSource()) {
				collectOperatorsImpl(subscription.getTarget(), list);
			}
		}
	}
}
