package de.uniol.inf.is.odysseus.peer.distribute.adv;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import net.jxta.document.Advertisement;

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
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementListener;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementManager;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.peer.distribute.PeerDistributePlugIn;

public class QueryPartManager implements IAdvertisementListener, IDataDictionaryListener {

	private static final Logger LOG = LoggerFactory.getLogger(QueryPartManager.class);

	private static QueryPartManager instance;

	private static IServerExecutor executor;
	private static ICompiler compiler;
	private static IAdvertisementManager advertisementManager;
	private static IP2PNetworkManager p2pNetworkManager;

	private ConcurrentMap<QueryPartAdvertisement, List<String>> neededSourcesMap = Maps.newConcurrentMap();

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
	public static void bindAdvertisementManager(IAdvertisementManager serv) {
		advertisementManager = serv;
	}

	// called by OSGi-DS
	public static void unbindAdvertisementManager(IAdvertisementManager serv) {
		if (advertisementManager == serv) {
			advertisementManager = null;
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

	@Override
	public void advertisementAdded(IAdvertisementManager sender, Advertisement advertisement) {
		if (advertisement instanceof QueryPartAdvertisement) {
			final QueryPartAdvertisement adv = (QueryPartAdvertisement) advertisement;

			if (adv.getPeerID().equals(p2pNetworkManager.getLocalPeerID())) {
				LOG.debug("PQL statement to be executed on peer {}: {}", p2pNetworkManager.getLocalPeerName(), ((QueryPartAdvertisement) advertisement).getPqlStatement());
				final List<String> neededSources = determineNeededSources(adv);

				if (neededSources.isEmpty()) {
					callExecutor(adv);
					LOG.debug("All source available for advertisement {}", adv);
				} else {
					synchronized (neededSourcesMap) {
						neededSourcesMap.put(adv, neededSources);
					}
				}
			}
		}
	}

	private List<String> determineNeededSources(final QueryPartAdvertisement adv) {
		final List<String> neededSources = Lists.newArrayList();
		neededSourcesMap.putIfAbsent(adv, neededSources);
		final List<IExecutorCommand> queries = compiler.translateQuery(adv.getPqlStatement(), "PQL", PeerDistributePlugIn.getActiveSession(), getDataDictionary(), Context.empty());
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

						oldNeededSources = neededSourcesMap.get(adv);

						// TODO not a good solution to concatenate user name and
						// source name
						ISession session = PeerDistributePlugIn.getActiveSession();
						if (getDataDictionary().containsViewOrStream(session.getUser().getName() + "." + source, session) || neededSourcesMap.get(adv).contains(source))
							break;

						neededSources.add(source);
						LOG.debug("Source {} needed for query {}", source, adv.getPqlStatement());
						advertisementManager.refreshAdvertisements();

					} while (!neededSourcesMap.replace(adv, oldNeededSources, neededSources));

				}
			}

		}
		return neededSources;
	}

	private void callExecutor(QueryPartAdvertisement adv) {
		try {
			final List<IQueryBuildSetting<?>> configuration = determineQueryBuildSettings(executor, adv.getTransCfgName());
			final Collection<Integer> ids = executor.addQuery(adv.getPqlStatement(), "PQL", PeerDistributePlugIn.getActiveSession(), adv.getTransCfgName(), Context.empty(), configuration);

			QueryPartController.getInstance().registerAsSlave(ids, adv.getSharedQueryID());

		} catch (final Throwable t) {
			LOG.error("Could not execute query part", t);
		}
	}

	@Override
	public void advertisementRemoved(IAdvertisementManager sender, Advertisement adv) {
		// do nothing
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

		for (QueryPartAdvertisement adv : neededSourcesMap.keySet()) {

			List<String> oldNeededSources;
			List<String> newNeededSources;

			if (neededSourcesMap.get(adv).contains(source)) {

				do {

					newNeededSources = neededSourcesMap.get(adv);
					oldNeededSources = ImmutableList.copyOf(newNeededSources);
					newNeededSources.remove(source);
					LOG.debug("Needed Source {} available for advertisement {}", name, adv);

				} while (!neededSourcesMap.replace(adv, oldNeededSources, newNeededSources));

				if ((oldNeededSources = neededSourcesMap.get(adv)).isEmpty() && neededSourcesMap.remove(adv, oldNeededSources))
					callExecutor(adv);

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
