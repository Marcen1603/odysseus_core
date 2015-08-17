package de.uniol.inf.is.odysseus.recovery.operatorstate;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.core.collection.IPair;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulPO;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IPipe;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEventType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.recovery.IRecoveryComponent;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
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
	private final Set<IPair<Integer, ISession>> mQueryIdsForBackup = Sets.newHashSet();

	@Override
	public List<ILogicalQuery> activateBackup(QueryBuildConfiguration qbConfig, ISession caller,
			List<ILogicalQuery> queries) {
		for (ILogicalQuery query : queries) {
			int queryId = query.getID();
			this.mQueryIdsForBackup.add(new Pair<Integer, ISession>(new Integer(queryId), caller));
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
		for (IPair<Integer, ISession> queryId : this.mQueryIdsForBackup) {
			OperatorStateStore.store(
					collectStateFulOperators(
							cExecutor.get().getPhysicalRoots(queryId.getE1().intValue(), queryId.getE2())),
					queryId.getE1().intValue());
		}
	}

	/**
	 * Collects all stateful operators of a query plan.
	 * 
	 * @param physicalRoots
	 *            The roots of the plan.
	 * @return A set of operators each implementing {@link IStatefulPO}.
	 */
	private Set<IStatefulPO> collectStateFulOperators(List<IPhysicalOperator> physicalRoots) {
		Set<IStatefulPO> statefulOperators = Sets.newHashSet();
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
	 * @return A set of operators each implementing {@link IStatefulPO}.
	 */
	private void collectStateFulOperatorsRecursive(IPhysicalOperator operator, Set<IStatefulPO> operators) {
		if (IStatefulPO.class.isInstance(operator)) {
			IStatefulPO statefulOperator = (IStatefulPO) operator;
			if (!operators.contains(statefulOperator)) {
				operators.add(statefulOperator);
			}
		}
		if (IPipe.class.isInstance(operator)) {
			for (Object sub : ((IPipe<?, ?>) operator).getSubscribedToSource()) {
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
		if (cQueryIdsForRecovery.contains(queryId) && PlanModificationEventType.QUERY_ADDED.equals(eventType)) {
			try {
				OperatorStateStore.load(collectStateFulOperators(query.getRoots()), queryId.intValue());
				cQueryIdsForRecovery.remove(queryId);
			} catch (ClassNotFoundException | IOException e) {
				cLog.error("Could not load operator state!", e);
			}
		}
	}

}