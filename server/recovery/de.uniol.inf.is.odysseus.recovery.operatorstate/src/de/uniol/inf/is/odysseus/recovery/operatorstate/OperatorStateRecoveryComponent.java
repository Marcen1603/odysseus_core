package de.uniol.inf.is.odysseus.recovery.operatorstate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.core.ISubscriber;
import de.uniol.inf.is.odysseus.core.ISubscription;
import de.uniol.inf.is.odysseus.core.collection.IPair;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.physicaloperator.ControllablePhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulPO;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEventType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.recovery.IRecoveryComponent;
import de.uniol.inf.is.odysseus.core.server.util.GenericGraphWalker;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.util.IGraphNodeVisitor;
import de.uniol.inf.is.odysseus.recovery.incomingelements.sourcesync.physicaloperator.SourceRecoveryPO;
import de.uniol.inf.is.odysseus.recovery.protectionpoints.IProtectionPointHandler;
import de.uniol.inf.is.odysseus.recovery.protectionpoints.ProtectionPointManagerRegistry;

/**
 * The operator state recovery component handles the backup and recovery of
 * operator states.
 * 
 * @author Michael Brand
 *
 */
@SuppressWarnings(value = { "nls" })
public class OperatorStateRecoveryComponent
		implements IRecoveryComponent, IProtectionPointHandler, IPlanModificationListener {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = -4945704252875069921L;

	/**
	 * The logger for this class.
	 */
	private static final Logger cLog = LoggerFactory.getLogger(OperatorStateRecoveryComponent.class);

	@Override
	public String getName() {
		return "Operator State";
	}

	/**
	 * The server executor, if bound.
	 */
	private static Optional<IServerExecutor> cExecutor = Optional.absent();

	/**
	 * Binds an implementation of the server executor.
	 * 
	 * @param executor
	 *            The implementation to bind.
	 */
	public void bindServerExecutor(IExecutor executor) {
		if (IServerExecutor.class.isInstance(executor)) {
			cExecutor = Optional.of((IServerExecutor) executor);
			((IServerExecutor) executor).addPlanModificationListener(this);
		}
	}

	/**
	 * Unbinds an implementation of the server executor.
	 * 
	 * @param executor
	 *            The implementation to unbind.
	 */
	public void unbindServerExecutor(IExecutor executor) {
		if (cExecutor.isPresent() && cExecutor.get() == executor) {
			cExecutor = Optional.absent();
			((IServerExecutor) executor).removePlanModificationListener(this);
		}
	}

	@Override
	public IRecoveryComponent newInstance(Properties config) {
		return new OperatorStateRecoveryComponent();
	}

	/**
	 * The ids of all queries, recovery is activated for.
	 */
	private static final Set<Integer> cQueryIdsForRecovery = Sets.newHashSet();

	@Override
	public List<ILogicalQuery> recover(QueryBuildConfiguration qbConfig, ISession caller, List<ILogicalQuery> queries) {
		for (ILogicalQuery query : queries) {
			cQueryIdsForRecovery.add(new Integer(query.getID()));
		}
		return queries;
	}

	/**
	 * The ids of all queries (and the session, which called the creation),
	 * backup is activated for.
	 */
	private static final Set<IPair<Integer, ISession>> cQueryIdsForBackup = Sets.newHashSet();

	@Override
	public List<ILogicalQuery> activateBackup(QueryBuildConfiguration qbConfig, ISession caller,
			List<ILogicalQuery> queries) {
		for (ILogicalQuery query : queries) {
			int queryId = query.getID();
			cQueryIdsForBackup.add(new Pair<>(new Integer(queryId), caller));
			ProtectionPointManagerRegistry.getInstance(queryId).addHandler(this);
		}
		return queries;
	}

	@Override
	public void onProtectionPointReached() throws Exception {
		if (!cExecutor.isPresent()) {
			cLog.error("No executor bound!");
			return;
		}
		for (IPair<Integer, ISession> queryId : cQueryIdsForBackup) {
			ILogicalQuery logQuery = cExecutor.get().getLogicalQueryById(queryId.getE1().intValue(), queryId.getE2());
			IPhysicalQuery physQuery = cExecutor.get().getExecutionPlan().getQueryById(queryId.getE1().intValue());
			IPair<List<IStatefulPO>, List<ControllablePhysicalSubscription<? extends IPhysicalOperator>>> opsAndSubs = collectStateFullOperatorsAndSubscriptions(
					physQuery.getRoots());
			OperatorStateStore.store(opsAndSubs.getE1(), logQuery);
			QueueStateStore.store(opsAndSubs.getE2(), logQuery);
		}
	}

	/**
	 * Collects all statefull operators and controllable subscriptions of a
	 * query plan.
	 * 
	 * @param roots
	 *            The physical roots of the plan.
	 * @return All operators, which implement {@link IStatefulPO}, and all
	 *         {@link ControllablePhysicalSubscription}s.
	 */
	private static IPair<List<IStatefulPO>, List<ControllablePhysicalSubscription<? extends IPhysicalOperator>>> collectStateFullOperatorsAndSubscriptions(
			List<IPhysicalOperator> roots) {
		List<IStatefulPO> statefulOperators = new ArrayList<>();
		List<ControllablePhysicalSubscription<? extends IPhysicalOperator>> controllableSubscriptions = new ArrayList<>();
		IPair<List<IStatefulPO>, List<ControllablePhysicalSubscription<? extends IPhysicalOperator>>> retVal = new Pair<>(
				statefulOperators, controllableSubscriptions);
		for (IPhysicalOperator root : roots) {
			collectStateFullOperatorsAndSubscriptions(root, retVal);
		}
		return retVal;
	}

	/**
	 * Collects recursively all statefull operators and controllable
	 * subscriptions of a query plan.
	 * 
	 * @param operator
	 *            The current operator to process.
	 * @param retVal
	 *            All already collected operators, which implement
	 *            {@link IStatefulPO}, and all already collected
	 *            {@link ControllablePhysicalSubscription}s.
	 */
	private static void collectStateFullOperatorsAndSubscriptions(IPhysicalOperator operator,
			IPair<List<IStatefulPO>, List<ControllablePhysicalSubscription<? extends IPhysicalOperator>>> retVal) {
		if (operator instanceof IStatefulPO) {
			retVal.getE1().add((IStatefulPO) operator);
		}
		if (operator instanceof ISubscriber) {
			@SuppressWarnings("unchecked")
			ISubscriber<? extends IPhysicalOperator, ISubscription<? extends IPhysicalOperator>> sink = (ISubscriber<? extends IPhysicalOperator, ISubscription<? extends IPhysicalOperator>>) operator;
			for (ISubscription<? extends IPhysicalOperator> sub : sink.getSubscribedToSource()) {
				if (sub instanceof ControllablePhysicalSubscription) {
					retVal.getE2().add((ControllablePhysicalSubscription<? extends IPhysicalOperator>) sub);
				}
				collectStateFullOperatorsAndSubscriptions(sub.getTarget(), retVal);
			}
		}
	}

	@Override
	public void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {
		PlanModificationEventType eventType = (PlanModificationEventType) eventArgs.getEventType();
		IPhysicalQuery query = (IPhysicalQuery) eventArgs.getValue();
		Integer queryId = new Integer(query.getID());
		if (cQueryIdsForRecovery.contains(queryId) && PlanModificationEventType.QUERY_START.equals(eventType)) {
			try {
				IPair<List<IStatefulPO>, List<ControllablePhysicalSubscription<? extends IPhysicalOperator>>> opsAndSubs = collectStateFullOperatorsAndSubscriptions(
						query.getRoots());
				OperatorStateStore.load(opsAndSubs.getE1(), query.getLogicalQuery());
				QueueStateStore.load(opsAndSubs.getE2(), query.getLogicalQuery());
				cQueryIdsForRecovery.remove(queryId);

				// First query needs to be started, because process_open
				// clears operator states. Afterwards, the states from backup
				// file can be loaded and set. Last, start consuming from BaDaSt
				GenericGraphWalker<?> walker = new GenericGraphWalker<>();
				walker.prefixWalkPhysical(query.getRoots().get(0),
						new IGraphNodeVisitor<IPhysicalOperator, IPhysicalOperator>() {

							@Override
							public void nodeAction(IPhysicalOperator node) {
								if (node instanceof SourceRecoveryPO<?>) {
									((SourceRecoveryPO<?>) node).openBaDaSt();
								}
							}

							@Override
							public void beforeFromSinkToSourceAction(IPhysicalOperator sink, IPhysicalOperator source) {
								// Nothing to do
							}

							@Override
							public void afterFromSinkToSourceAction(IPhysicalOperator sink, IPhysicalOperator source) {
								// Nothing to do
							}

							@Override
							public void beforeFromSourceToSinkAction(IPhysicalOperator source, IPhysicalOperator sink) {
								// Nothing to do
							}

							@Override
							public void afterFromSourceToSinkAction(IPhysicalOperator source, IPhysicalOperator sink) {
								// Nothing to do
							}

							@Override
							public IPhysicalOperator getResult() {
								return null;
							}
						});
			} catch (ClassNotFoundException | IOException e) {
				cLog.error("Could not load operator state!", e);
			}
		} else if (queryIdsForBackupContainsId(queryId) && PlanModificationEventType.QUERY_REMOVE.equals(eventType)) {
			try {
				OperatorStateStore.backupFile(query.getLogicalQuery());
				OperatorStateStore.deleteFile(query.getLogicalQuery());
				QueueStateStore.backupFile(query.getLogicalQuery());
				QueueStateStore.deleteFile(query.getLogicalQuery());
			} catch (ClassNotFoundException | IOException e) {
				cLog.error("Could not load operator state!", e);
			}
		}
	}

	/**
	 * Checks, if {@link #cQueryIdsForBackup} contains an entry with a given
	 * query id.
	 * 
	 * @param queryId
	 *            The query id.
	 * @return True, if {@link #cQueryIdsForBackup} contains an entry with a
	 *         given query id; false else.
	 */
	private static boolean queryIdsForBackupContainsId(Integer queryId) {
		for (IPair<Integer, ISession> entry : cQueryIdsForBackup) {
			if (entry.getE1().equals(queryId)) {
				return true;
			}
		}
		return false;
	}

}