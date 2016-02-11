package de.uniol.inf.is.odysseus.recovery.operatorstate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.core.collection.IPair;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.ControllablePhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
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
			// Avoid ConcurrentModificationExceptions
			List<IPhysicalOperator> physRoots = Lists
					.newArrayList(cExecutor.get().getPhysicalRoots(queryId.getE1().intValue(), queryId.getE2()));
			ILogicalQuery logQuery = cExecutor.get().getLogicalQueryById(queryId.getE1().intValue(), queryId.getE2());
			OperatorStateStore.store(collectStateFulOperators(physRoots), logQuery);
			QueueStateStore.store(collectControllableSubscriptions(physRoots), logQuery);
		}
	}

	/**
	 * Collects all controllable physical subscriptions of a query plan.
	 * 
	 * @param physicalRoots
	 *            The roots of the plan.
	 * @return A list of physical subscriptions each implementing
	 *         {@link ControllablePhysicalSubscription}.
	 */
	private static <K> List<ControllablePhysicalSubscription<K>> collectControllableSubscriptions(
			List<IPhysicalOperator> physicalRoots) {
		List<ControllablePhysicalSubscription<K>> subscriptions = new ArrayList<>();
		for (IPhysicalOperator root : physicalRoots) {
			collectControllableSubscriptionsRecursive(root, subscriptions);
		}
		return subscriptions;
	}

	/**
	 * Collects all controllable physical subscriptions of a query plan
	 * recursively.
	 * 
	 * @param operator
	 *            The current operator to check.
	 * @param subscriptions
	 *            All already controllable physical subscriptions.
	 */
	@SuppressWarnings("unchecked")
	private static <K> void collectControllableSubscriptionsRecursive(IPhysicalOperator operator,
			List<ControllablePhysicalSubscription<K>> subscriptions) {
		if (ISink.class.isInstance(operator)) {
			for (Object obj : ((ISink<?>) operator).getSubscribedToSource()) {
				if (obj instanceof AbstractPhysicalSubscription) {
					AbstractPhysicalSubscription<?> sub = (AbstractPhysicalSubscription<?>) obj;
					if (sub instanceof ControllablePhysicalSubscription) {
						subscriptions.add((ControllablePhysicalSubscription<K>) sub);
					}
					collectControllableSubscriptionsRecursive((IPhysicalOperator) sub.getTarget(), subscriptions);
				}
			}
		}
	}

	/**
	 * Collects all stateful operators of a query plan.
	 * 
	 * @param physicalRoots
	 *            The roots of the plan.
	 * @return A list of operators each implementing {@link IStatefulPO}.
	 */
	private List<IStatefulPO> collectStateFulOperators(List<IPhysicalOperator> physicalRoots) {
		List<IStatefulPO> statefulOperators = Lists.newArrayList();
		for (IPhysicalOperator root : physicalRoots) {
			collectStateFulOperatorsRecursive(root, statefulOperators);
		}
		return statefulOperators;
	}

	/**
	 * Collects all stateful operators of a query plan recursively.
	 * 
	 * @param operator
	 *            The current operator to add and check.
	 * @param operators
	 *            All already collected stateful operators.
	 */
	private void collectStateFulOperatorsRecursive(IPhysicalOperator operator, List<IStatefulPO> operators) {
		if (IStatefulPO.class.isInstance(operator)) {
			IStatefulPO statefulOperator = (IStatefulPO) operator;
			if (!operators.contains(statefulOperator)) {
				operators.add(statefulOperator);
			}
		}
		if (ISink.class.isInstance(operator)) {
			for (Object sub : ((ISink<?>) operator).getSubscribedToSource()) {
				if (AbstractPhysicalSubscription.class.isInstance(sub)) {
					collectStateFulOperatorsRecursive(
							(IPhysicalOperator) ((AbstractPhysicalSubscription<?>) sub).getTarget(), operators);
				}
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
				OperatorStateStore.load(collectStateFulOperators(query.getRoots()), query.getLogicalQuery());
				QueueStateStore.load(collectControllableSubscriptions(query.getRoots()), query.getLogicalQuery());
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