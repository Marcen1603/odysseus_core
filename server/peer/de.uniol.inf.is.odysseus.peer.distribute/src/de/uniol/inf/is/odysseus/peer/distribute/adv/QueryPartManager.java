package de.uniol.inf.is.odysseus.peer.distribute.adv;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryProvider;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.distribution.QueryDistributionException;
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

public class QueryPartManager {

	private static final Logger LOG = LoggerFactory.getLogger(QueryPartManager.class);

	private static QueryPartManager instance;

	private static IServerExecutor executor;
	private static ICompiler compiler;
	private static IP2PNetworkManager p2pNetworkManager;

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

	public void addQueryPart(AddQueryPartMessage message) throws QueryDistributionException {
		LOG.debug("PQL statement to be executed: {}", message.getPqlStatement());
		List<String> neededSources = determineNeededSources(message);

		if (neededSources.isEmpty()) {
			try {
				LOG.debug("All source available. Calling executor.");
				callExecutor(message);
			} catch (Throwable t) {
				throw new QueryDistributionException("Could not execute query: " + t.getMessage());
			}
		} else {
			throw new QueryDistributionException("Not all sources are available: " + neededSources);
		}
	}

	private List<String> determineNeededSources(AddQueryPartMessage message) {
		List<String> neededSources = Lists.newArrayList();
		ISession session = PeerDistributePlugIn.getActiveSession();

		List<IExecutorCommand> queries = compiler.translateQuery(message.getPqlStatement(), "PQL", PeerDistributePlugIn.getActiveSession(), getDataDictionary(), Context.empty());
		for (IExecutorCommand q : queries) {

			if (q instanceof CreateQueryCommand) {
				ILogicalQuery query = ((CreateQueryCommand) q).getQuery();

				List<ILogicalOperator> operators = Lists.newArrayList();
				RestructHelper.collectOperators(query.getLogicalPlan(), operators);

				for (ILogicalOperator operator : operators) {

					if (!(operator instanceof AbstractAccessAO)) {
						continue;
					}
					String source = ((AbstractAccessAO) operator).getName();

					// TODO not a good solution to concatenate user name and
					// source name
					if (getDataDictionary().containsViewOrStream(session.getUser().getName() + "." + source, session)) {
						break;
					}

					neededSources.add(source);
					LOG.debug("Source {} needed for query {}", source, message.getPqlStatement());
				}
			}

		}
		return neededSources;
	}

	private void callExecutor(AddQueryPartMessage message) {
		List<IQueryBuildSetting<?>> configuration = determineQueryBuildSettings(executor, message.getTransCfgName());
		Collection<Integer> ids = executor.addQuery(message.getPqlStatement(), "PQL", PeerDistributePlugIn.getActiveSession(), message.getTransCfgName(), Context.empty(), configuration);

		QueryPartController.getInstance().registerAsSlave(ids, message.getSharedQueryID());
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
}
