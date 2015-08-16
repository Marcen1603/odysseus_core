package de.uniol.inf.is.odysseus.recovery.operatorstate;

import java.io.IOException;
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
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulPO;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IPipe;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
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
public class OperatorStateRecoveryComponent implements IRecoveryComponent, IProtectionPointHandler {

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
	public static void bindServerExecutor(IExecutor executor) {
		if (IServerExecutor.class.isInstance(executor)) {
			cExecutor = Optional.of((IServerExecutor) executor);
		}
	}

	/**
	 * Unbinds an implementation of the server executor.
	 * 
	 * @param executor
	 *            The implementation to unbind.
	 */
	public static void unbindServerExecutor(IExecutor executor) {
		if (cExecutor.isPresent() && cExecutor.get() == executor) {
			cExecutor = Optional.absent();
		}
	}

	@Override
	public IRecoveryComponent newInstance(Properties config) {
		return new OperatorStateRecoveryComponent();
	}

	@Override
	public List<ILogicalQuery> recover(QueryBuildConfiguration qbConfig, ISession caller, List<ILogicalQuery> queries) {
		if (!cExecutor.isPresent()) {
			cLog.error("No executor bound!");
			return queries;
		}
		// FIXME Test, if recovery of operator states works
		List<ILogicalQuery> modifiedQueries = Lists.newArrayList(queries);
		for (ILogicalQuery query : modifiedQueries) {
			try {
				int queryId = query.getID();
				OperatorStateStore.load(collectStateFulOperators(cExecutor.get().getPhysicalRoots(queryId, caller)),
						queryId);
			} catch (IOException | ClassNotFoundException e) {
				cLog.error("Could not load operator!", e);
			}
		}
		return modifiedQueries;
	}

	/**
	 * The ids of all queries (and the session, which called the creation),
	 * backup is activated for.
	 */
	private final Set<IPair<Integer, ISession>> mQueryIds = Sets.newHashSet();

	@Override
	public List<ILogicalQuery> activateBackup(QueryBuildConfiguration qbConfig, ISession caller,
			List<ILogicalQuery> queries) {
		for (ILogicalQuery query : queries) {
			int queryId = query.getID();
			this.mQueryIds.add(new Pair<Integer, ISession>(new Integer(queryId), caller));
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
		for (IPair<Integer, ISession> queryId : this.mQueryIds) {
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

}