package de.uniol.inf.is.odysseus.recovery.queuestate;

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
import de.uniol.inf.is.odysseus.core.physicaloperator.ControllablePhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
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
 * The queue state recovery component handles the backup and recovery of queue
 * states ({@link ControllablePhysicalSubscription}).
 * 
 * @author Michael Brand
 *
 */
@SuppressWarnings(value = { "nls" })
public class QueueStateRecoveryComponent implements IRecoveryComponent, IProtectionPointHandler {

	/**
	 * The logger for this class.
	 */
	private static final Logger cLog = LoggerFactory.getLogger(QueueStateRecoveryComponent.class);

	@Override
	public String getName() {
		return "Queue State";
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
		return new QueueStateRecoveryComponent();
	}

	@Override
	public List<ILogicalQuery> recover(QueryBuildConfiguration qbConfig, ISession caller, List<ILogicalQuery> queries) {
		if (!cExecutor.isPresent()) {
			cLog.error("No executor bound!");
			return queries;
		}
		// FIXME Test, if recovery of queue states works
		List<ILogicalQuery> modifiedQueries = Lists.newArrayList(queries);
		for (ILogicalQuery query : modifiedQueries) {
			try {
				int queryId = query.getID();
				QueueStateStore.load(
						collectControllableSubscriptions(cExecutor.get().getPhysicalRoots(queryId, caller)), queryId);
			} catch (IOException | ClassNotFoundException e) {
				cLog.error("Could not load queues!", e);
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
		// FIXME Test, if backup of queue states works
		for (ILogicalQuery query : queries) {
			int queryId = query.getID();
			this.mQueryIds.add(new Pair<Integer, ISession>(new Integer(queryId), caller));
			ProtectionPointManagerRegistry.getInstance(queryId).addHandler(this);
		}
		return queries;
	}

	@Override
	public void onProtectionPointReached() throws Exception {
		// FIXME Test, if backup of queue states works
		if (!cExecutor.isPresent()) {
			cLog.error("No executor bound!");
			return;
		}
		for (IPair<Integer, ISession> queryId : this.mQueryIds) {
			QueueStateStore.store(
					collectControllableSubscriptions(
							cExecutor.get().getPhysicalRoots(queryId.getE1().intValue(), queryId.getE2())),
					queryId.getE1().intValue());
		}
	}

	/**
	 * Collects all controllable subscriptions of a query plan.
	 * 
	 * @param physicalRoots
	 *            The roots of the plan.
	 * @return A set of controllable subscriptions.
	 */
	private <K> Set<ControllablePhysicalSubscription<K>> collectControllableSubscriptions(
			List<IPhysicalOperator> physicalRoots) {
		Set<ControllablePhysicalSubscription<K>> controllableSubs = Sets.newHashSet();
		for (IPhysicalOperator root : physicalRoots) {
			collectControllableSubscriptionsRecursive(root, controllableSubs);
		}
		return controllableSubs;
	}

	/**
	 * Collects all controllable subscriptions of a query plan recursively.
	 * 
	 * @param operator
	 *            The current operator to check.
	 * @param queues
	 *            All already collected controllable subscriptions.
	 * @return A set of controllable subscriptions.
	 */
	@SuppressWarnings("unchecked")
	private <K> void collectControllableSubscriptionsRecursive(IPhysicalOperator operator,
			Set<ControllablePhysicalSubscription<K>> queues) {
		if (IPipe.class.isInstance(operator)) {
			for (Object sub : ((IPipe<?, ?>) operator).getSubscribedToSource()) {
				if (ControllablePhysicalSubscription.class.isInstance(sub)) {
					queues.add((ControllablePhysicalSubscription<K>) sub);
					collectControllableSubscriptionsRecursive(
							(IPhysicalOperator) ((AbstractPhysicalSubscription<?>) sub).getTarget(), queues);
				}
			}
		}
	}

}