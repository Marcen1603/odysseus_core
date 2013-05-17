package de.uniol.inf.is.odysseus.p2p_new.distribute.user;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.jxta.document.Advertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.MetadataUpdatePO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.IQueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterDoRewrite;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementListener;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementManager;
import de.uniol.inf.is.odysseus.p2p_new.P2PNewPlugIn;

public class QueryPartManager implements IAdvertisementListener {

	private static final Logger LOG = LoggerFactory.getLogger(QueryPartManager.class);

	private static QueryPartManager instance;

	private IServerExecutor executor;

	public QueryPartManager() {
		instance = this;
	}

	@Override
	public void advertisementAdded(IAdvertisementManager sender, Advertisement advertisement) {
		if (advertisement instanceof QueryPartAdvertisement) {
			final QueryPartAdvertisement adv = (QueryPartAdvertisement) advertisement;
			if (adv.getPeerID().equals(P2PNewPlugIn.getOwnPeerID())) {
				try {
					final TransformationConfiguration transformationConfiguration = determineTransformationConfiguration(executor, adv.getTransCfgName());
					final List<IQueryBuildSetting<?>> configuration = determineQueryBuildSettings(executor, adv.getTransCfgName());
					transformationConfiguration.setOption("NO_METADATA", true);

					final Collection<Integer> ids = executor.addQuery(adv.getPqlStatement(), "PQL", SessionManagementService.getActiveSession(), adv.getTransCfgName(), configuration);

					transformationConfiguration.removeOption("NO_METADATA");
					removeUnnededOperators(executor, ids);

					QueryPartController.getInstance().registerAsSlave(ids, adv.getSharedQueryID());

				} catch (final Throwable t) {
					LOG.error("Could not execute query part", t);
				}
			}
		}
	}

	@Override
	public void advertisementRemoved(IAdvertisementManager sender, Advertisement adv) {
		// do nothing
	}

	// called by OSGi-DS
	public void bindExecutor(IExecutor exe) {
		if (exe instanceof IServerExecutor) {
			executor = (IServerExecutor) exe;

			LOG.debug("Bound ServerExecutor {}", exe);
		} else {
			throw new IllegalArgumentException("Executor " + exe + " is not a ServerExecutor");
		}
	}

	// called by OSGi-DS
	public void unbindExecutor(IExecutor exe) {
		if (executor == exe) {
			LOG.debug("Unbound Executor {}", exe);

			executor = null;
		}
	}

	public static QueryPartManager getInstance() {
		return instance;
	}

	private static List<IQueryBuildSetting<?>> determineQueryBuildSettings(IServerExecutor executor, String cfgName) {
		final IQueryBuildConfiguration qbc = executor.getQueryBuildConfiguration(cfgName);
		final List<IQueryBuildSetting<?>> configuration = qbc.getConfiguration();

		final List<IQueryBuildSetting<?>> settings = Lists.newArrayList();
		settings.addAll(configuration);
		settings.add(ParameterDoRewrite.FALSE);
		return settings;
	}

	private static TransformationConfiguration determineTransformationConfiguration(IServerExecutor executor, String cfgName) {
		final IQueryBuildConfiguration qbc = executor.getQueryBuildConfiguration(cfgName);
		final List<IQueryBuildSetting<?>> configuration = qbc.getConfiguration();
		final QueryBuildConfiguration qbc2 = new QueryBuildConfiguration(configuration.toArray(new IQueryBuildSetting[0]), cfgName);
		final TransformationConfiguration transformationConfiguration = qbc2.getTransformationConfiguration();
		return transformationConfiguration;
	}

	private static void removeUnnededOperators(IServerExecutor serverExecutor, Collection<Integer> ids) {
		for (final Integer id : ids) {
			final IPhysicalQuery physicalQuery = serverExecutor.getExecutionPlan().getQueryById(id);
			removeUnneededOperators(physicalQuery);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void removeUnneededOperators(IPhysicalQuery physicalQuery) {
		final Set<IPhysicalOperator> operators = Sets.newHashSet(physicalQuery.getAllOperators());
		for (final IPhysicalOperator operator : operators) {
			if (operator instanceof MetadataUpdatePO) {
				final MetadataUpdatePO updatePO = (MetadataUpdatePO) operator;

				final List<?> subscribedToSource = updatePO.getSubscribedToSource();
				final Map<ISource<?>, Integer> sourcePortMap = Maps.newHashMap();
				for (final Object subSource : subscribedToSource) {
					final PhysicalSubscription<ISource<?>> physSub = (PhysicalSubscription<ISource<?>>) subSource;
					sourcePortMap.put(physSub.getTarget(), physSub.getSourceOutPort());

					physSub.getTarget().disconnectSink(updatePO, physSub.getSinkInPort(), physSub.getSourceOutPort(), physSub.getTarget().getOutputSchema());
				}
				updatePO.unsubscribeFromAllSources();

				final List<?> subscriptions = updatePO.getSubscriptions();
				final Map<ISink<?>, Integer> sinkPortMap = Maps.newHashMap();
				for (final Object subSink : subscriptions) {
					final PhysicalSubscription<ISink<?>> physSub = (PhysicalSubscription<ISink<?>>) subSink;
					sinkPortMap.put(physSub.getTarget(), physSub.getSinkInPort());

					physSub.getTarget().unsubscribeFromSource(updatePO, physSub.getSinkInPort(), physSub.getSourceOutPort(), updatePO.getOutputSchema());
				}
				updatePO.unsubscribeFromAllSinks();

				for (final ISource source : sourcePortMap.keySet()) {
					for (final ISink sink : sinkPortMap.keySet()) {
						source.subscribeSink(sink, sinkPortMap.get(sink), sourcePortMap.get(source), source.getOutputSchema());
					}
				}
			}
		}
	}
}
