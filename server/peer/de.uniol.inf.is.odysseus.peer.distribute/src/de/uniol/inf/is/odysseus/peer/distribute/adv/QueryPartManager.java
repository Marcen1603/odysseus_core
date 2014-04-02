package de.uniol.inf.is.odysseus.peer.distribute.adv;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryProvider;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryListener;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.planmanagement.ICompiler;
import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.IQueryBuildConfigurationTemplate;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.CreateQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterDoRewrite;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.peer.distribute.PeerDistributePlugIn;
import de.uniol.inf.is.odysseus.peer.distribute.message.AddQueryPartMessage;

public class QueryPartManager implements IDataDictionaryListener {

	private static final Logger LOG = LoggerFactory.getLogger(QueryPartManager.class);

	private static QueryPartManager instance;

	private static IServerExecutor executor;
	private static ICompiler compiler;
	private static IP2PNetworkManager p2pNetworkManager;

	private ConcurrentMap<AddQueryPartMessage, List<String>> neededSourcesMap = Maps.newConcurrentMap();

	public QueryPartManager() {
		instance = this;
	}

	// called by OSGi-DS
	public static void bindCompiler(ICompiler serv) {
		compiler = serv;
	}

	// called by OSGi-DS
	public static void unbindCompiler(ICompiler serv) {
		if (compiler == serv) {
			compiler = null;
		}
	}

	// called by OSGi-DS
	public static void bindExecutor(IExecutor exe) {
		executor = (IServerExecutor) exe;
	}

	// called by OSGi-DS
	public static void unbindExecutor(IExecutor exe) {
		if (executor == exe) {
			executor = null;
		}
	}

	// called by OSGi-DS
	public static void bindP2PNetworkManager(IP2PNetworkManager serv) {
		p2pNetworkManager = serv;
	}

	// called by OSGi-DS
	public static void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		if (p2pNetworkManager == serv) {
			p2pNetworkManager = null;
		}
	}
	
	public void addQueryPart( AddQueryPartMessage message ) {
		LOG.debug("PQL statement to be executed: {}", message.getPqlStatement());
		List<String> neededSources = determineNeededSources(message);
		
		if (neededSources.isEmpty()) {
			LOG.debug("All source available");
			
			callExecutor(message);
		} else {
			LOG.debug("Not all sources are available: {}", neededSources);
			
			synchronized (neededSourcesMap) {
				neededSourcesMap.put(message, neededSources);
			}
		}
	}

	private List<String> determineNeededSources(AddQueryPartMessage message) {
		final List<String> neededSources = Lists.newArrayList();
		neededSourcesMap.putIfAbsent(message, neededSources);
		
		final List<IExecutorCommand> queries = compiler.translateQuery(message.getPqlStatement(), "PQL", PeerDistributePlugIn.getActiveSession(), getDataDictionary(), Context.empty());
		for (IExecutorCommand q : queries) {

			if (q instanceof CreateQueryCommand) {
				ILogicalQuery query = ((CreateQueryCommand) q).getQuery();

				final List<ILogicalOperator> operators = Lists.newArrayList();
				RestructHelper.collectOperators(query.getLogicalPlan(), operators);

				for (ILogicalOperator operator : operators) {

					if (!(operator instanceof AbstractAccessAO))
						continue;
					String source = ((AbstractAccessAO) operator).getName();

					List<String> oldNeededSources;
					do {
						oldNeededSources = neededSourcesMap.get(message);

						// TODO not a good solution to concatenate user name and
						// source name
						ISession session = PeerDistributePlugIn.getActiveSession();
						if (getDataDictionary().containsViewOrStream(session.getUser().getName() + "." + source, session) || neededSourcesMap.get(message).contains(source)) {
							break;
						}

						neededSources.add(source);
						LOG.debug("Source {} needed for query {}", source, message.getPqlStatement());

					} while (!neededSourcesMap.replace(message, oldNeededSources, neededSources));

				}
			}

		}
		return neededSources;
	}

	private void callExecutor(AddQueryPartMessage message) {
		try {
			final List<IQueryBuildSetting<?>> configuration = determineQueryBuildSettings(executor, message.getTransCfgName());
			final Collection<Integer> ids = executor.addQuery(message.getPqlStatement(), "PQL", PeerDistributePlugIn.getActiveSession(), message.getTransCfgName(), Context.empty(), configuration);

			QueryPartController.getInstance().registerAsSlave(ids, message.getSharedQueryID());

		} catch (final Throwable t) {
			LOG.error("Could not execute query part", t);
		}
	}

	public IDataDictionary getDataDictionary() {
		return DataDictionaryProvider.getDataDictionary(PeerDistributePlugIn.getActiveSession().getTenant());
	}

	public static QueryPartManager getInstance() {
		return instance;
	}

	private static List<IQueryBuildSetting<?>> determineQueryBuildSettings(IServerExecutor executor, String cfgName) {
		final IQueryBuildConfigurationTemplate qbc = executor.getQueryBuildConfiguration(cfgName);
		final List<IQueryBuildSetting<?>> configuration = qbc.getConfiguration();

		final List<IQueryBuildSetting<?>> settings = Lists.newArrayList();
		settings.addAll(configuration);
		settings.add(ParameterDoRewrite.FALSE);
		return settings;
	}

	@Override
	public void addedViewDefinition(IDataDictionary sender, String name, ILogicalOperator op) {

		if (sender != getDataDictionary())
			return;

		/*
		 * XXX split doesn't work for a reason I don't know. TODO Make sure,
		 * that a username can not contain dots.
		 */
		String source = name.substring(name.indexOf(".") + 1);

		synchronized (neededSourcesMap) {

			if (!neededSourcesMap.values().contains(source))
				return;

		}

		for (AddQueryPartMessage message : neededSourcesMap.keySet()) {

			List<String> oldNeededSources;
			List<String> newNeededSources;

			if (neededSourcesMap.get(message).contains(source)) {

				do {

					newNeededSources = neededSourcesMap.get(message);
					oldNeededSources = ImmutableList.copyOf(newNeededSources);
					newNeededSources.remove(source);
					LOG.debug("Needed Source {} available for advertisement {}", name, message);

				} while (!neededSourcesMap.replace(message, oldNeededSources, newNeededSources));

				if ((oldNeededSources = neededSourcesMap.get(message)).isEmpty() && neededSourcesMap.remove(message, oldNeededSources))
					callExecutor(message);

			}

		}

	}

	@Override
	public void removedViewDefinition(IDataDictionary sender, String name, ILogicalOperator op) {
		// Nothing do do.
	}

	@Override
	public void dataDictionaryChanged(IDataDictionary sender) {
		// Nothing do do.
	}
}
