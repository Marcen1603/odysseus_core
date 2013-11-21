package de.uniol.inf.is.odysseus.p2p_new.distribute.queryPart;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import net.jxta.document.Advertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryProvider;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryListener;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.IQueryBuildConfigurationTemplate;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.CreateQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterDoRewrite;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementListener;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementManager;
import de.uniol.inf.is.odysseus.p2p_new.distribute.service.AdvertisementManagerService;
import de.uniol.inf.is.odysseus.p2p_new.distribute.service.CompilerService;
import de.uniol.inf.is.odysseus.p2p_new.distribute.service.P2PDictionaryService;
import de.uniol.inf.is.odysseus.p2p_new.distribute.service.SessionManagementService;

// TODO javaDoc M.B.
public class QueryPartManager implements IAdvertisementListener,
		IDataDictionaryListener {

	private static final Logger LOG = LoggerFactory
			.getLogger(QueryPartManager.class);

	private static QueryPartManager instance;

	private IServerExecutor executor;

	private ConcurrentMap<QueryPartAdvertisement, List<String>> neededSourcesMap = Maps
			.newConcurrentMap();

	public QueryPartManager() {
		instance = this;
	}

	@Override
	public void advertisementAdded(IAdvertisementManager sender,
			Advertisement advertisement) {

		if (advertisement instanceof QueryPartAdvertisement) {

			final QueryPartAdvertisement adv = (QueryPartAdvertisement) advertisement;

			if (adv.getPeerID().equals(
					P2PDictionaryService.get().getLocalPeerID())) {

				LOG.debug("PQL statement to be executed on peer {}: {}",
						P2PDictionaryService.get().getLocalPeerName(),
						((QueryPartAdvertisement) advertisement)
								.getPqlStatement());

				final List<String> neededSources = determineNeededSources(adv);

				if (neededSources.isEmpty()) {

					callExecutor(adv);
					LOG.debug("All source available for advertisement {}", adv);

				} else
					synchronized (neededSourcesMap) {

						neededSourcesMap.put(adv, neededSources);

					}

			}
		}
	}

	private List<String> determineNeededSources(final QueryPartAdvertisement adv) {
		final List<String> neededSources = Lists.newArrayList();
		neededSourcesMap.putIfAbsent(adv, neededSources);
		final List<IExecutorCommand> queries = CompilerService.get()
				.translateQuery(adv.getPqlStatement(), "PQL",
						SessionManagementService.getActiveSession(),
						getDataDictionary(), null);
		for (IExecutorCommand q : queries) {

			if (q instanceof CreateQueryCommand) {
				ILogicalQuery query = ((CreateQueryCommand) q).getQuery();

				final List<ILogicalOperator> operators = Lists.newArrayList();
				RestructHelper.collectOperators(query.getLogicalPlan(),
						operators);

				for (ILogicalOperator operator : operators) {

					if (!(operator instanceof AbstractAccessAO))
						continue;
					String source = ((AbstractAccessAO) operator).getName();

					List<String> oldNeededSources;
					do {

						oldNeededSources = neededSourcesMap.get(adv);

						// TODO not a good solution to concatenate user name and source name
						ISession session = SessionManagementService.getActiveSession();
						if (getDataDictionary().containsViewOrStream(session.getUser().getName() + "." + source,
								session)
								|| neededSourcesMap.get(adv).contains(source))
							break;

						neededSources.add(source);
						LOG.debug("Source {} needed for query {}", source,
								adv.getPqlStatement());
						AdvertisementManagerService.get()
								.refreshAdvertisements();

					} while (!neededSourcesMap.replace(adv, oldNeededSources,
							neededSources));

				}
			}

		}
		return neededSources;
	}

	private void callExecutor(QueryPartAdvertisement adv) {

		try {

			final List<IQueryBuildSetting<?>> configuration = determineQueryBuildSettings(
					executor, adv.getTransCfgName());
			final Collection<Integer> ids = executor.addQuery(
					adv.getPqlStatement(), "PQL",
					SessionManagementService.getActiveSession(),
					adv.getTransCfgName(), null, configuration);

			QueryPartController.getInstance().registerAsSlave(ids,
					adv.getSharedQueryID());

		} catch (final Throwable t) {

			LOG.error("Could not execute query part", t);

		}

	}

	/**
	 * Not implemented.
	 */
	@Override
	public void advertisementRemoved(IAdvertisementManager sender,
			Advertisement adv) {
		// do nothing
	}

	/**
	 * Binds the referenced {@link IExecutor}. <br />
	 * Called by OSGI-DS.
	 * @see #unbindExecutor(IExecutor)
	 * @param exe An instance of an {@link IExecutor} implementation.
	 */
	public void bindExecutor(IExecutor exe) {
		if (exe instanceof IServerExecutor) {
			executor = (IServerExecutor) exe;

			LOG.debug("Bound ServerExecutor {}", exe);
		} else {
			throw new IllegalArgumentException("Executor " + exe
					+ " is not a ServerExecutor");
		}
	}

	/**
	 * Unbinds an referenced {@link IExecutor}, if <code>exe</code> is the binded one. <br />
	 * Called by OSGI-DS.
	 * @see #bindExecutor(IExecutor)
	 * @param exe An instance of an {@link IExecutor} implementation.
	 */
	public void unbindExecutor(IExecutor exe) {
		if (executor == exe) {
			LOG.debug("Unbound Executor {}", exe);

			executor = null;
		}
	}

	/**
	 * Returns the used {@link IDataDictionary}.
	 */
	public IDataDictionary getDataDictionary(){
		return DataDictionaryProvider.getDataDictionary(SessionManagementService.getActiveSession().getTenant());
	}

	/**
	 * Returns the single instance of this class.
	 */
	public static QueryPartManager getInstance() {
		return instance;
	}

	/**
	 * Determine the needed settings to call {@link IServerExecutor#addQuery(String, String, ISession, String, List)}.
	 * @param executor The {@link IServerExecutor}.
	 * @param cfgName The name of the {@link QueryBuildConfiguration} to load. See {@link IServerExecutor#getQueryBuildConfiguration(String)}.
	 */
	private static List<IQueryBuildSetting<?>> determineQueryBuildSettings(
			IServerExecutor executor, String cfgName) {
		final IQueryBuildConfigurationTemplate qbc = executor
				.getQueryBuildConfiguration(cfgName);
		final List<IQueryBuildSetting<?>> configuration = qbc
				.getConfiguration();

		final List<IQueryBuildSetting<?>> settings = Lists.newArrayList();
		settings.addAll(configuration);
		settings.add(ParameterDoRewrite.FALSE);
		return settings;
	}

	@Override
	public void addedViewDefinition(IDataDictionary sender, String name,
			ILogicalOperator op) {

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
					LOG.debug(
							"Needed Source {} available for advertisement {}",
							name, adv);

				} while (!neededSourcesMap.replace(adv, oldNeededSources,
						newNeededSources));

				if ((oldNeededSources = neededSourcesMap.get(adv)).isEmpty()
						&& neededSourcesMap.remove(adv, oldNeededSources))
					callExecutor(adv);

			}

		}

	}

	/**
	 * Not implemented.
	 */
	@Override
	public void removedViewDefinition(IDataDictionary sender, String name, ILogicalOperator op) {
		
		// Nothing do do.
		
	}

	/**
	 * Not implemented.
	 */
	@Override
	public void dataDictionaryChanged(IDataDictionary sender) {
		
		// Nothing do do.
		
	}

}
