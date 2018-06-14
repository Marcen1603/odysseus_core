package de.uniol.inf.is.odysseus.recovery.processingimage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEventType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.recovery.IRecoveryComponent;
import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionManagement;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.util.OperatorCollector;
import de.uniol.inf.is.odysseus.recovery.checkpointing.CheckpointManagerRegistry;
import de.uniol.inf.is.odysseus.recovery.checkpointing.ICheckpointListener;

/**
 * The processing image recovery component backs up operator and queue
 * (subscription) states if a checkpoint is reached, and recovers them if
 * needed.
 *
 * @author Michael Brand
 *
 */
public class ProcessingImageRecoveryComponent
		implements IRecoveryComponent, IPlanModificationListener, ICheckpointListener {


	static private final ISession superUser = SessionManagement.instance.loginSuperUser(null);

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = -6173355064288873029L;

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ProcessingImageRecoveryComponent.class);

	/**
	 * The bound executor
	 */
	private static IServerExecutor executor;

	/**
	 * Binds an executor.
	 */
	public void bindExecutor(IExecutor exe) {
		if (exe instanceof IServerExecutor) {
			executor = (IServerExecutor) exe;
			executor.addPlanModificationListener(this);
		}
	}

	/**
	 * Unbinds an executor.
	 */
	public void unbindExecutor(IExecutor exe) {
		if (exe == executor) {
			executor.removePlanModificationListener(this);
			executor = null;
		}
	}

	/**
	 * The ids of all queries recovery is activated for.
	 */
	private static final Set<Integer> queryIdsForRecovery = new HashSet<>();

	/**
	 * The ids and creator sessions of all queries backup is activated for.
	 */
	private static final Map<Integer, ISession> queryIdsForBackup = new HashMap<>();

	@Override
	public void initialize(Properties config) {
		// Nothing to do.
	}

	@Override
	public List<ILogicalQuery> activateRecovery(QueryBuildConfiguration qbConfig, ISession session,
			List<ILogicalQuery> queries, IExecutor caller) {
		for (ILogicalQuery query : queries) {
			queryIdsForRecovery.add(query.getID());
		}
		return queries;
	}

	@Override
	public List<ILogicalQuery> activateBackup(QueryBuildConfiguration qbConfig, ISession session,
			List<ILogicalQuery> queries, IExecutor caller) {
		for (ILogicalQuery query : queries) {
			int queryId = query.getID();
			queryIdsForBackup.put(queryId, session);
			CheckpointManagerRegistry.getInstance(queryId).addListener(this);
		}
		return queries;
	}

	@Override
	public void onCheckpointReached() throws Exception {
		for (int queryId : queryIdsForBackup.keySet()) {
			LOG.info("Backing up processing image of query {} ...", queryId);
			IPhysicalQuery physQuery = executor.getExecutionPlan(superUser).getQueryById(queryId, superUser);
			ProcessingImageStore.storeOperators(OperatorCollector.collect(physQuery.getRoots()), queryId);
			ProcessingImageStore.storeQueues(OperatorCollector.collectSubcriptions(physQuery.getRoots()), queryId);
			LOG.info("... done.");
		}
	}

	@Override
	public void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {
		PlanModificationEventType eventType = (PlanModificationEventType) eventArgs.getEventType();
		IPhysicalQuery physQuery = (IPhysicalQuery) eventArgs.getValue();
		Integer queryId = new Integer(physQuery.getID());
		if (queryIdsForRecovery.contains(queryId) && PlanModificationEventType.QUERY_ADDED.equals(eventType)) {
			try {
				LOG.info("Recovering processing image of query {} ...", queryId);
				ProcessingImageStore.loadOperators(OperatorCollector.collect(physQuery.getRoots()), queryId);
				ProcessingImageStore.loadQueues(OperatorCollector.collectSubcriptions(physQuery.getRoots()), queryId);
				LOG.info("... done.");
				queryIdsForRecovery.remove(queryId);
			} catch (Exception e) {
				LOG.error("Could not recover processing image!", e);
			}
		} else if (queryIdsForBackup.containsKey(queryId) && PlanModificationEventType.QUERY_REMOVE.equals(eventType)) {
			try {
				// We don't need the processing image any more
				ProcessingImageStore.deleteProcessingImage(queryId);
			} catch (Exception e) {
				LOG.error("Could not delete processing image!", e);
			}
		}
	}

}
