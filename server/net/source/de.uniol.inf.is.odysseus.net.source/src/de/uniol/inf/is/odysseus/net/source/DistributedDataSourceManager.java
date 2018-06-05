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
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.FileHandler;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalPlan;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalPlan;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryProvider;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryListener;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDatadictionaryProviderListener;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataRegistry;
import de.uniol.inf.is.odysseus.core.server.planmanagement.IQueryParser;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.CreateQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionManagement;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;
import de.uniol.inf.is.odysseus.net.config.OdysseusNetConfiguration;
import de.uniol.inf.is.odysseus.net.data.DistributedDataException;
import de.uniol.inf.is.odysseus.net.data.IDistributedData;
import de.uniol.inf.is.odysseus.net.data.IDistributedDataListener;
import de.uniol.inf.is.odysseus.net.data.IDistributedDataManager;
import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLGenerator;
import de.uniol.inf.is.odysseus.relational_interval.StandardQueryBuildConfigurationTemplate;

public class DistributedDataSourceManager
		implements IDistributedDataListener, IDatadictionaryProviderListener, IDataDictionaryListener {

	private static final Logger LOG = LoggerFactory.getLogger(DistributedDataSourceManager.class);
	private static final String DATA_SOURCE_DISTRIBUTION_NAME = "net.data.source";
	private static final String DISTRIBUTED_DATA_LIFETIME_CONFIG_KEY = "net.source.lifetime";
	private static final long DEFAULT_DISTRIBUTED_DATA_LIFETIME_MILLIS = 60 * 60 * 1000; // 1
																							// hour

	private static IDistributedDataManager dataManager;
	private static IPQLGenerator pqlGenerator;
	private static IQueryParser pqlParser;

	private static ISession currentSession;

	private final Map<String, UUID> createdSourcesMap = Maps.newHashMap();
	private final BiMap<UUID, String> importedSourcesBiMap = HashBiMap.create();
	@SuppressWarnings("unused")
	private DataDictionaryProvider dataDictionaryProvider;
	@SuppressWarnings("unused")
	private UserManagementProvider userManagementProvider;

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
		ITenant tenant = UserManagementProvider.instance.getDefaultTenant();
		DataDictionaryProvider.instance.subscribe(tenant, this);

		IDataDictionary dd = DataDictionaryProvider.instance.getDataDictionary(tenant);
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
				boolean isStream = data.getBoolean("isStream");

				if (!getDataDictionary().containsViewOrStream(sourceName, getActiveSession())) {
					Optional<ILogicalOperator> optTopAO = parsePQLStatement(sourceName, pqlStatement);
					if (optTopAO.isPresent()) {
						synchronized (importedSourcesBiMap) {
							importedSourcesBiMap.put(addedData.getUUID(), userName + "." + sourceName);
						}

						ILogicalOperator streamOrViewAO = determineStreamOrViewAO(optTopAO.get());
						if (isStream) {
							getDataDictionary().setStream(sourceName, new LogicalPlan(streamOrViewAO), getActiveSession());
						} else {
							getDataDictionary().setView(sourceName, new LogicalPlan(streamOrViewAO), getActiveSession());
						}

					} else {
						LOG.error("No data source from distributed data created");
					}
				}
			} catch (JSONException e) {
				LOG.error("Could not process json-data", e);
			}
		}
	}

	/**
	 * PQL parser generates TopAO on top of stream or view. This method returns the
	 * operator subscribed to the TopAO. If there are more than one subscriptions,
	 * it returns the first found target. If there is no subscription, it returns
	 * <code>topAO</code>.
	 */
	private ILogicalOperator determineStreamOrViewAO(ILogicalOperator topAO) {
		if (topAO.getSubscribedToSource().isEmpty()) {
			return topAO;
		}
		return topAO.getSubscribedToSource(0).getSource();
	}

	@Override
	public void distributedDataModified(IDistributedDataManager sender, IDistributedData oldData,
			IDistributedData newData) {
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
		getDataDictionary().removeListener(this);

		DataDictionaryProvider.instance.unsubscribe(this);
	}

	@Override
	public void newDatadictionary(IDataDictionary dd) {
		dd.addListener(this);

		Set<Entry<Resource, ILogicalPlan>> streamsAndViews = dd.getStreamsAndViews(getActiveSession());
		for (Entry<Resource, ILogicalPlan> streamOrView : streamsAndViews) {
			String name = streamOrView.getKey().getResourceName();
			String username = streamOrView.getKey().getUser();

			ILogicalPlan plan = streamOrView.getValue();

			boolean isStream = (dd.getStreamForTransformation(streamOrView.getKey(), getActiveSession()) != null);

			createDistributedData(name, plan.getRoot(), username, isStream, isStream);
		}
	}

	@Override
	public void removedDatadictionary(IDataDictionary dd) {
		dd.removeListener(this);

		Set<Entry<Resource, ILogicalPlan>> streamsAndViews = dd.getStreamsAndViews(getActiveSession());
		for (Entry<Resource, ILogicalPlan> streamOrView : streamsAndViews) {
			String name = streamOrView.getKey().getResourceName();
			String username = streamOrView.getKey().getUser();

			destroyDistributedData(name, username);
		}
	}

	@Override
	public void addedViewDefinition(IDataDictionary sender, String name, ILogicalPlan op, boolean isView,
			ISession session) {
		synchronized (importedSourcesBiMap) {
			// FIXME Distributed views do not work. See ODY-1068
			if (!importedSourcesBiMap.containsValue(name) && !isView) {
				createDistributedData(name, op.getRoot(), session.getUser().getName(), !isView, !isView);
			}
		}
	}

	@Override
	public void removedViewDefinition(IDataDictionary sender, String name, ILogicalPlan op, boolean isView,
			ISession session) {
		synchronized (importedSourcesBiMap) {
			if (!importedSourcesBiMap.containsValue(name)) {
				destroyDistributedData(name, session.getUser().getName());
			}
		}
	}

	private void createDistributedData(String name, ILogicalOperator op, String username, boolean isStream,
			boolean isPersistent) {
		new Thread("SourceDistributionThread") {

			@Override
			public void run() {
				String realSourceName = removeUserFromName(name);
				String pqlStatement = pqlGenerator.generatePQLStatement(op);

				try {
					JSONObject json = new JSONObject();
					json.put("name", realSourceName);
					json.put("user", username);
					json.put("pql", pqlStatement);
					json.put("isStream", isStream);

					long lifetime = determineLifetime();
					IDistributedData distributedData = dataManager.create(json, DATA_SOURCE_DISTRIBUTION_NAME,
							isPersistent, lifetime);
					synchronized (createdSourcesMap) {
						createdSourcesMap.put(username + "." + realSourceName, distributedData.getUUID());
					}

				} catch (JSONException e) {
					LOG.error("Could not create json object", e);
				} catch (DistributedDataException e) {
					LOG.error("Could not create distributed data", e);
				}
			}
		}.start();
	}

	private static long determineLifetime() {
		String lifetimeStr = OdysseusNetConfiguration.get(DISTRIBUTED_DATA_LIFETIME_CONFIG_KEY,
				"" + DEFAULT_DISTRIBUTED_DATA_LIFETIME_MILLIS);
		try {
			return Long.valueOf(lifetimeStr);
		} catch (Throwable t) {
			return DEFAULT_DISTRIBUTED_DATA_LIFETIME_MILLIS;
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
		return (IDataDictionaryWritable) DataDictionaryProvider.instance
				.getDataDictionary(getActiveSession().getTenant());
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
			currentSession = SessionManagement.instance.loginSuperUser(null, UserManagementProvider.instance.getDefaultTenant().getName());
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

			// Get metadata
			List<IQueryBuildSetting<?>> settings = new StandardQueryBuildConfigurationTemplate().getConfiguration();
			QueryBuildConfiguration buildCfg = new QueryBuildConfiguration(settings, "Standard");
			IMetaAttribute metaAttribute = MetadataRegistry
					.getMetadataType(buildCfg.getTransformationConfiguration().getDefaultMetaTypeSet());

			List<IExecutorCommand> commands = pqlParser.parse(pqlStatement, getActiveSession(), getDataDictionary(),
					Context.empty(), metaAttribute, null);
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
						LOG.error("Could not create datasource since the specified file '" + file.getName()
								+ "' does not exist.");
						return Optional.absent();
					}
				}

				ILogicalOperator op = query.getLogicalPlan().getRoot();
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
		return plan.getLogicalPlan().getOperators();
	}

	void setDataDictionaryProvider(DataDictionaryProvider ddp) {
		this.dataDictionaryProvider = ddp;
	}

	void setUserManagementProvider(UserManagementProvider ump) {
		this.userManagementProvider = ump;
	}
}
