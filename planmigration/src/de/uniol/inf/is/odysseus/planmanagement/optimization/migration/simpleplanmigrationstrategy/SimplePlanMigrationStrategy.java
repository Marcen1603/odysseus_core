/** Copyright [2011] [The Odysseus Team]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.planmanagement.optimization.migration.simpleplanmigrationstrategy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.IOwnedOperator;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.MigrationRouterPO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.buffer.BufferPO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.lock.IMyLock;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.lock.LockingLock;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.exception.SchedulerException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.IPlanMigratable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planmigration.IMigrationEventSource;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planmigration.IMigrationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planmigration.IPlanMigrationStrategy;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planmigration.MigrationHelper;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planmigration.exception.MigrationException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.scheduler.exception.NoSchedulerLoadedException;
import de.uniol.inf.is.odysseus.core.server.util.AbstractTreeWalker;
import de.uniol.inf.is.odysseus.core.server.util.CollectOperatorPhysicalGraphVisitor;
import de.uniol.inf.is.odysseus.core.server.util.GenericGraphWalker;
import de.uniol.inf.is.odysseus.core.server.util.PhysicalPlanToStringVisitor;
import de.uniol.inf.is.odysseus.core.server.util.PhysicalRestructHelper;
import de.uniol.inf.is.odysseus.core.server.util.RemoveOwnersGraphVisitor;
import de.uniol.inf.is.odysseus.core.util.SetOwnerGraphVisitor;

/**
 * SimplePlanMigrationStrategy transfers a currently running physical plan into
 * a new given plan that has empty operator states. Therefore, both plans are
 * running parallel, while only the old plan does the output. When all states in
 * the new plan are filled, the old one is removed and the new plan continues
 * query processing. This strategy is based on the "Generalized Parallel Track"
 * strategy by ZHU, Yali ; RUNDENSTEINER, Elke A. ; HEINEMAN, George T..
 * 
 * @author Tobias Witt, Merlin Wasmann
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class SimplePlanMigrationStrategy implements IPlanMigrationStrategy {

	private static final Logger LOG = LoggerFactory
			.getLogger(SimplePlanMigrationStrategy.class);

	private Map<MigrationRouterPO<?>, StrategyContext> routerStrategy;

	private Set<IMigrationListener> listener;

	private IPhysicalQuery physicalQuery;

	public SimplePlanMigrationStrategy() {
		this.routerStrategy = new HashMap<MigrationRouterPO<?>, StrategyContext>();
		this.listener = new HashSet<IMigrationListener>();
	}

	@Override
	public String getName() {
		return "Simple Plan Migration Strategy";
	}

	@Override
	public IExecutionPlan migratePlan(IPlanMigratable sender,
			IExecutionPlan newExecutionPlan) {
		// no global plan migration
		return newExecutionPlan;
	}

	@Override
	public void migrateQuery(IServerExecutor sender,
			IPhysicalQuery runningQuery, List<IPhysicalOperator> newPlanRoots)
			throws QueryOptimizationException, MigrationException {
		if (runningQuery.containsCycles()) {
			throw new RuntimeException("Planmigration assumes acyclic trees");
		}

		long migrationStart = System.currentTimeMillis();

		// @Marco: Bitte so umbauen, dass beachtet wird,
		// dass ein Anfrageplan mehrere Roots haben kann.
		LOG.debug("Start planmigration.");

		this.physicalQuery = runningQuery;

		// install both plans for parallel execution
		IPhysicalOperator oldPlanRoot = runningQuery.getRoots().get(0);
		List<ISource<?>> oldPlanSources = MigrationHelper
				.getPseudoSources(oldPlanRoot);
		List<ISource<?>> metaDataUpdates = MigrationHelper
				.getMetaDataUpdatePOs(oldPlanSources);
		List<IPhysicalOperator> oldPlanOperatorsBeforeSources = MigrationHelper
				.getOperatorsBeforeSources(oldPlanRoot, metaDataUpdates);
		List<IPhysicalOperator> newPlanOperatorsBeforeSources = MigrationHelper
				.getOperatorsBeforeSources(newPlanRoots.get(0), metaDataUpdates);

		// get last operators before output sink
		IPhysicalOperator lastOperatorOldPlan;
		// if (oldPlanRoot.isSource()) {
		// Operators can be both source and sink
		if (!oldPlanRoot.isSink()) {
			throw new QueryOptimizationException(
					"Migration strategy needs a sink only as operator root.");
		} else {
			// expects last sink to have exactly one subscription
			// lastOperatorOldPlan = ((ISink<?>) oldPlanRoot)
			// .getSubscribedToSource(0).getTarget();
			lastOperatorOldPlan = oldPlanRoot;
		}
		IPhysicalOperator lastOperatorNewPlan;
		// if (newPlanRoots.get(0).isSource()) {
		// Operators can be both source and sink
		if (!newPlanRoots.get(0).isSink()) {
			throw new QueryOptimizationException(
					"Migration strategy needs a sink only as operator root.");
		} else {
			// lastOperatorNewPlan = ((ISink<?>) newPlanRoots.get(0))
			// .getSubscribedToSource(0).getTarget();
			lastOperatorNewPlan = newPlanRoots.get(0);
		}

		StrategyContext context = new StrategyContext(sender, runningQuery,
				newPlanRoots.get(0));
		context.setOldPlanOperatorsBeforeSources(oldPlanOperatorsBeforeSources);
		context.setLastOperatorNewPlan(lastOperatorNewPlan);
		context.setLastOperatorOldPlan(lastOperatorOldPlan);
		context.setMigrationStart(migrationStart);
		// context.setwMax(wMax);

		LOG.debug("Preparing plan for parallel execution.");
		// insert buffers before sources
		// for (ISource<?> source : oldPlanSources) {
		for (IPhysicalOperator metadataUpdatePO : metaDataUpdates) {

			LOG.debug("Insert Blocking-Buffer after source "
					+ metadataUpdatePO.getClass().getSimpleName() + " ("
					+ metadataUpdatePO.hashCode() + ")");

			BufferPO buffer = new BufferPO();
			try {
				buffer.open();
			} catch (OpenFailedException e) {
				// Buffer not connected, so no errors can occur
				LOG.error("Failed to open Buffer", e);
			}
			buffer.setName(metadataUpdatePO.getName());
			
			buffer.block();
			buffer.setOutputSchema(metadataUpdatePO.getOutputSchema());

			// set the source for this buffer in case it is empty
			buffer.setSource((ISource<?>) metadataUpdatePO);
			context.addBufferPO((BufferPO<?>) buffer);

//			Collection<PhysicalSubscription> afterSourceSubs = ((ISource) metadataUpdatePO)
//					.getSubscriptions();
//			List<ISink> sinks = new ArrayList<ISink>();
//			List<Integer> sinkInPorts = new ArrayList<Integer>();
//			for (PhysicalSubscription<?> sub : afterSourceSubs) {
//				IPhysicalOperator sink = (IPhysicalOperator) sub.getTarget();
//				// filter DataSourceObserver
//				if (sink.isPipe()) {
//					sinks.add((ISink) sink);
//					sinkInPorts.add(sub.getSinkInPort());
//				}
//			}

			insertBuffer(buffer, metadataUpdatePO, newPlanOperatorsBeforeSources, oldPlanOperatorsBeforeSources);

			LOG.debug("Insert Blocking-Buffer after source ... done");
		}

		IPipe<?, ?> router = new MigrationRouterPO(metaDataUpdates, 0, 1);

		((MigrationRouterPO<?>) router).addMigrationListener(this);

		context.setRouter(router);
		context.setOldPlanRoot(oldPlanRoot);

		this.routerStrategy.put((MigrationRouterPO<?>) router, context);
		LOG.debug("Router: " + router.getClass().getSimpleName() + " (#"
				+ router.hashCode() + ") has been put to map");

		LOG.debug("Merging Plans ");

		IPhysicalOperator root = null;
		if (!lastOperatorOldPlan.isSource()) {
			// it is a real sink (like FileSink)
			root = insertRouterWithRealSink(lastOperatorOldPlan,
					lastOperatorNewPlan, router);
		} else {
			root = insertRouterWithoutRealSink(lastOperatorOldPlan,
					lastOperatorNewPlan, router);
		}

		SetOwnerGraphVisitor<IOwnedOperator> addVisitor = new SetOwnerGraphVisitor<>(
				runningQuery, true);
		GenericGraphWalker walker = new GenericGraphWalker();
		walker.prefixWalkPhysical(root, addVisitor);

		List<IPhysicalOperator> roots = new ArrayList<IPhysicalOperator>();
		roots.add(root);
		runningQuery.initializePhysicalRoots(roots);

		LOG.debug("Root has been initialized with owner " + root.getOwnerIDs());

		// open auf dem neuen Plan aufrufen
		LOG.debug("Calling open on new Plan ");
		try {
			((ISink) root).open();
		} catch (OpenFailedException e1) {
			LOG.error("Open root failed", e1);
		}
		LOG.debug("Calling open on new Plan ... done");

		LOG.debug("Result:\n"
				+ AbstractTreeWalker.prefixWalk2(root,
						new PhysicalPlanToStringVisitor()));

		LOG.debug("Initializing parallel execution plan.");

		try {
			sender.executionPlanChanged();
		} catch (SchedulerException | NoSchedulerLoadedException e) {
			throw new MigrationException(e);
		}

		// resume by unblocking buffers
		for (BufferPO<?> buffer : context.getBufferPOs()) {
			buffer.insertMigrationMarkerPunctuation();
			buffer.unblock();
		}

		LOG.debug("Parallel execution started.");
		// we are waiting for the event that is fired.
	}

	/**
	 * Insert the buffer between the metadataUpdatePO and the subscribedSinks to the updatePO
	 * @param buffer Buffer which should be inserted
	 * @param metadataUpdatePO Operator before which the buffer should be inserted.
	 * @param newPlanOperatorsBeforeSources Operators which are after the metadataUpdatePO in the new plan
	 * @param oldPlanOperatorsBeforeSources Operators which are after the metadataUpdatePO in the old plan
	 */
	private void insertBuffer(BufferPO buffer, IPhysicalOperator metadataUpdatePO, List<IPhysicalOperator> newPlanOperatorsBeforeSources, List<IPhysicalOperator> oldPlanOperatorsBeforeSources) {
		buffer.subscribeToSource(metadataUpdatePO, 0, 0,
				metadataUpdatePO.getOutputSchema());
		
		// get the corresponding subscribtion
		PhysicalSubscription<?> bufferSub = null;
		for (PhysicalSubscription<?> sub : ((ISource<?>) metadataUpdatePO)
				.getSubscriptions()) {
			if (sub.getTarget().equals(buffer)) {
				bufferSub = sub;
			}
		}
		// replace the activeSinkSubscriptions between updatePO and beforeSourceOperators with updatePO -> buffer
		Set<PhysicalSubscription<?>> unSubscribe = new HashSet<PhysicalSubscription<?>>();
		for (PhysicalSubscription<?> sub : ((ISource<?>) metadataUpdatePO)
				.getSubscriptions()) {
			IPhysicalOperator op = (IPhysicalOperator) sub.getTarget();
			if (newPlanOperatorsBeforeSources.contains(op)
					|| oldPlanOperatorsBeforeSources.contains(op)) {
				// activate connection between source and buffer
				// deactivate connection between source and subscriptions
				((AbstractPipe) metadataUpdatePO)
						.replaceActiveSubscription(sub, bufferSub);
				unSubscribe.add(sub);
			} else {
				LOG.debug("Operator: " + op.getClass().getSimpleName()
						+ " (" + op.hashCode()
						+ ") will not be replaced by buffer.");
			}
		}
		// unsubscribe all subscriptions from source
		// subscribe all subscriptions to buffer
		for (PhysicalSubscription<?> sub : unSubscribe) {
			IPhysicalOperator op = (IPhysicalOperator) sub.getTarget();
			((AbstractPipe) op).unsubscribeFromSource(metadataUpdatePO, 0,
					0, metadataUpdatePO.getOutputSchema());

			LOG.debug("Operator: " + op.getClass().getSimpleName() + " ("
					+ op.hashCode() + ") was unsubscribed from source");

			((AbstractPipe) op).subscribeToSource(buffer, 0, 0,
					buffer.getOutputSchema());

			LOG.debug("Operator: " + op.getClass().getSimpleName() + " ("
					+ op.hashCode() + ") was subscribed to buffer");
		}
	}
	
	/**
	 * Inserts the router between the plans and the real sink which is
	 * lastOperatorOldPlan and returns the lastOperatorOldPlan as new root.
	 * 
	 * @param lastOperatorOldPlan
	 * @param lastOperatorNewPlan
	 * @param router
	 * @return
	 */
	private ISink insertRouterWithRealSink(
			IPhysicalOperator lastOperatorOldPlan,
			IPhysicalOperator lastOperatorNewPlan, IPipe<?, ?> router) {
		// get the real sink
		ISink<?> realSink = (ISink<?>) lastOperatorOldPlan;

		// collect the operators before the sink in the old plan.
		List<AbstractSource<?>> oldOpsBeforeSink = new ArrayList<AbstractSource<?>>();
		List<PhysicalSubscription> oldSubscriptions = new ArrayList<PhysicalSubscription>();
		for (PhysicalSubscription<?> sub : realSink.getSubscribedToSource()) {
			AbstractSource<?> beforeSink = (AbstractSource<?>) sub.getTarget();
			oldOpsBeforeSink.add(beforeSink);
			for (PhysicalSubscription<?> sinkSub : beforeSink
					.getSubscriptions()) {
				if (sinkSub.getTarget().equals(realSink)) {
					oldSubscriptions.add(sinkSub);
				}
			}
		}

		// collect the operators before the sink in the new plan.
		List<AbstractSource<?>> newOpsBeforeSink = new ArrayList<AbstractSource<?>>();
		List<PhysicalSubscription> newSubscriptions = new ArrayList<PhysicalSubscription>();
		for (PhysicalSubscription<?> sub : ((ISink<?>) lastOperatorNewPlan)
				.getSubscribedToSource()) {
			AbstractSource<?> beforeSink = (AbstractSource<?>) sub.getTarget();
			newOpsBeforeSink.add(beforeSink);
			for (PhysicalSubscription<?> sinkSub : beforeSink
					.getSubscriptions()) {
				if (sinkSub.getTarget().equals(lastOperatorNewPlan)) {
					newSubscriptions.add(sinkSub);
				}
			}
		}
		
		router.setOutputSchema(oldOpsBeforeSink.get(0).getOutputSchema());

		if (oldOpsBeforeSink.size() > 1 || newOpsBeforeSink.size() > 1) {
			LOG.error("Sink is subscribed to more than one operator");
		}
		// append the router to the operators before the sink.
		PhysicalRestructHelper.appendBinaryOperator(router,
				oldOpsBeforeSink.get(0), newOpsBeforeSink.get(0));

		// block router before we change the active sink connections.
		IMyLock locker = new LockingLock();
		((AbstractSource) router).setLocker(locker);
		router.block();

		for (PhysicalSubscription sub : oldOpsBeforeSink.get(0)
				.getSubscriptions()) {
			if (sub.getTarget().equals(router)) {
				oldOpsBeforeSink.get(0).replaceActiveSubscription(
						oldSubscriptions.get(0), sub);
			}
		}
		for (PhysicalSubscription sub : newOpsBeforeSink.get(0)
				.getSubscriptions()) {
			if (sub.getTarget().equals(router)) {
				newOpsBeforeSink.get(0).replaceActiveSubscription(
						newSubscriptions.get(0), sub);
			}
		}

		realSink.unsubscribeFromAllSources();
		realSink.subscribeToSource((ISource) router, 0, 0,
				router.getOutputSchema());

		// remove the sink from the new plan because it is not needed
		((ISink) lastOperatorNewPlan).unsubscribeFromAllSources();
		lastOperatorNewPlan.removeOwner(this.physicalQuery);

		router.addOwner(this.physicalQuery);

		router.unblock();

		try {
			router.open();
		} catch (OpenFailedException e) {
			LOG.error("Open failed for router", e);
		}

		return realSink;
	}

	/**
	 * Inserts a router as the new root of the plan and returns it.
	 * 
	 * @param lastOperatorOldPlan
	 * @param lastOperatorNewPlan
	 * @param router
	 * @return
	 */
	private ISink insertRouterWithoutRealSink(
			IPhysicalOperator lastOperatorOldPlan,
			IPhysicalOperator lastOperatorNewPlan, IPipe router) {
		router.setOutputSchema(lastOperatorOldPlan.getOutputSchema());
		PhysicalRestructHelper.appendBinaryOperator(router,
				(ISource) lastOperatorOldPlan, (ISource) lastOperatorNewPlan);
		if (LOG.isDebugEnabled()) {
			LOG.debug("Router " + router.getClass().getSimpleName()
					+ " appended to child1: "
					+ lastOperatorOldPlan.getClass().getSimpleName() + " ("
					+ lastOperatorOldPlan.hashCode() + ") and child2: "
					+ lastOperatorNewPlan.getClass().getSimpleName() + " ("
					+ lastOperatorNewPlan.hashCode() + ")");
		}
		router.addOwner(this.physicalQuery);
		return router;
	}

	/**
	 * Proof of concept for another finished migration handling.
	 * 
	 * @param context
	 */
	void finishedParallelExecutionPoC(StrategyContext context)
			throws MigrationException {
		LOG.debug("Handling finished migration");

		LOG.debug("Blocking buffers");
		for (BufferPO buffer : context.getBufferPOs()) {
			buffer.block();
		}
		LOG.debug("All buffers blocked");

//		LOG.debug("Drain tuples out of plans");
//		for (IPhysicalOperator root : context.getRunningQuery().getRoots()) {
//			MigrationHelper.drainTuples(root);
//		}

		LOG.debug("Disconnect old plan from buffer");
		List<IPhysicalOperator> bufferParents = context
				.getOldPlanOperatorsBeforeSources();
		List<PhysicalSubscription<?>> unSub = new ArrayList<PhysicalSubscription<?>>();
		for (BufferPO<?> buffer : context.getBufferPOs()) {
			for (PhysicalSubscription<?> sub : buffer.getSubscriptions()) {
				IPhysicalOperator operator = (IPhysicalOperator) sub
						.getTarget();
				if (bufferParents.contains(operator)) {
					unSub.add(sub);
				}
			}
			for (PhysicalSubscription<?> sub : unSub) {
				ISink sink = (ISink) sub.getTarget();
				buffer.unsubscribeSink(sink, sub.getSinkInPort(),
						sub.getSourceOutPort(), buffer.getOutputSchema());
			}
		}

		LOG.debug("Removing router");
		List<IPhysicalOperator> newRoots = removeRouter(context
				.getRunningQuery().getRoots(),
				(MigrationRouterPO) context.getRouter());

		LOG.debug("Block sources");
		List<AbstractSource<?>> blockedSources = new ArrayList<AbstractSource<?>>();
		try {
			for (BufferPO<?> buffer : context.getBufferPOs()) {
				for (PhysicalSubscription<?> sub : buffer
						.getSubscribedToSource()) {
					AbstractSource<?> source = (AbstractSource<?>) sub
							.getTarget();
					if (source.isLocked()) {
						LOG.error(source.getClass().getSimpleName() + " ("
								+ source.hashCode()
								+ ") is already locked by another thread");
					}
					LOG.debug("Blocking: " + source.getClass().getSimpleName()
							+ " (" + source.hashCode() + ")");
					IMyLock lock = new LockingLock();
					source.setLocker(lock);
					source.block();
					blockedSources.add(source);
				}

				LOG.debug("Unblocking buffer to drain it");
				buffer.unblock();
				LOG.debug("Empty buffer");
				while (buffer.hasNext()) {
					buffer.transferNext();
				}

				LOG.debug("Remove buffer");
				List<ISink> parents = new ArrayList<ISink>();
				List<Integer> parentInports = new ArrayList<Integer>();
				List<ISource> children = new ArrayList<ISource>();
				List<Integer> childOutports = new ArrayList<Integer>();
				for (PhysicalSubscription<?> sub : buffer
						.getSubscribedToSource()) {
					children.add((ISource) sub.getTarget());
					childOutports.add(sub.getSourceOutPort());
				}
				for (PhysicalSubscription<?> sub : buffer.getSubscriptions()) {
					parents.add((ISink) sub.getTarget());
					parentInports.add(sub.getSinkInPort());
				}

				PhysicalRestructHelper.removeOperator(children.get(0), 0,
						parents, parentInports, buffer);
				buffer.removeOwner(context.getRunningQuery());
			}

			LOG.debug("Unsubscribe old plan from source");
			for (IPhysicalOperator oldOperatorBeforeSource : context
					.getOldPlanOperatorsBeforeSources()) {
				((ISink) oldOperatorBeforeSource).unsubscribeFromAllSources();
			}

			LOG.debug("Removing owner from old plan");
			IPhysicalOperator oldRoot = getPlanRoot(context
					.getOldPlanOperatorsBeforeSources().get(0));
			if (oldRoot != null) {
				RemoveOwnersGraphVisitor<IOwnedOperator> removeVisitor = new RemoveOwnersGraphVisitor<IOwnedOperator>(
						this.physicalQuery);
				GenericGraphWalker walker = new GenericGraphWalker();
				walker.prefixWalkPhysical(oldRoot, removeVisitor);
			}

			
			LOG.debug("Calling open on all plan roots");
			for(IPhysicalOperator root : newRoots) {
				((ISink) root).open();
			}

			LOG.debug("Initialize new plan root as physical root");
			context.getRunningQuery().initializePhysicalRoots(newRoots);
		} catch (Exception ex) {
			throw new MigrationException(ex);
		} finally {
			// unblocking of sources must be ensured
			LOG.debug("Unblocking sources");
			for (AbstractSource<?> source : blockedSources) {
				source.unblock();
			}
		}

		try {
			context.getExecutor().executionPlanChanged();
		} catch (SchedulerException | NoSchedulerLoadedException e) {
			throw new MigrationException(e);
		}

		LOG.debug("Result:\n"
				+ AbstractTreeWalker.prefixWalk2(newRoots.get(0),
						new PhysicalPlanToStringVisitor()));

		LOG.debug("Migration is finished");
		LOG.debug("Migration duration was " + (System.currentTimeMillis() - context.getMigrationStart()));

		fireMigrationFinishedEvent(this);
	}

	/**
	 * Removes the router and returns the list of new roots.
	 * 
	 * @param roots
	 *            old roots
	 * @param router
	 * @return
	 */
	private List<IPhysicalOperator> removeRouter(List<IPhysicalOperator> roots,
			MigrationRouterPO router) {
		LOG.debug("Remove router");
		List<PhysicalSubscription> unSub = new ArrayList<PhysicalSubscription>();
		unSub.addAll(router.getSubscribedToSource());
		AbstractSource<?> newPlan = null;
		for (PhysicalSubscription<?> sub : unSub) {
			// router.unsubscribeFromSource(sub);
			AbstractSource<?> target = (AbstractSource<?>) sub.getTarget();
			if (sub.getSinkInPort() == 1) {
				// this is the new plan
				newPlan = target;
			}
			target.unsubscribeSink(router, sub.getSinkInPort(),
					sub.getSourceOutPort(), router.getOutputSchema());
		}

		router.removeOwner(this.physicalQuery);

		// cleanup
		this.routerStrategy.remove(router);
		router.removeMigrationListener(this);

		List<IPhysicalOperator> newRoots = new ArrayList<IPhysicalOperator>();
		// reconnecting the new plan to the sink if necessary
		if (router.getSubscriptions().isEmpty()) {
			LOG.debug("No real sink to reconnect.");
			newRoots.add(newPlan);
		} else {
			List<ISink> sinks = new ArrayList<ISink>();
			for (PhysicalSubscription sub : ((AbstractSource<?>) router)
					.getSubscriptions()) {
				sinks.add((ISink) sub.getTarget());
			}
			for (IPhysicalOperator operator : sinks) {
				ISink sink = (ISink) operator;
				router.unsubscribeSink(sink, 0, 0, router.getOutputSchema());
				newPlan.subscribeSink(sink, 0, 0, newPlan.getOutputSchema());
				newRoots.add(operator);
				sink.open();
			}
		}
		return newRoots;
	}
	
	@SuppressWarnings("unused")
	private Set<BufferPO> fetchBuffersFromAllQueries(StrategyContext context) {
		Set<BufferPO> result = new HashSet<BufferPO>();
		CollectOperatorPhysicalGraphVisitor<IPhysicalOperator> visitor = new CollectOperatorPhysicalGraphVisitor(BufferPO.class);
		GenericGraphWalker walker = new GenericGraphWalker();

		for(IPhysicalQuery query : context.getExecutor().getExecutionPlan().getQueries()) {
			IPhysicalOperator node = query.getRoots().get(0);
			walker.prefixWalkPhysical(node, visitor);
			result.addAll((Collection<? extends BufferPO>) visitor.getResult());
		}
		
		return result;
	}

	/**
	 * returns the plan root
	 * 
	 * @param operator
	 * @return
	 */
	private IPhysicalOperator getPlanRoot(IPhysicalOperator operator) {
		IPhysicalOperator root = operator;
		IPhysicalOperator parent = getParent(operator);
		while (parent != null) {
			root = parent;
			parent = getParent(root);
		}
		return root;
	}

	private IPhysicalOperator getParent(IPhysicalOperator child) {
		for (PhysicalSubscription<?> sub : ((AbstractSource<?>) child)
				.getSubscriptions()) {
			IPhysicalOperator subscribed = (IPhysicalOperator) sub.getTarget();
			if (subscribed.getOwner().contains(this.physicalQuery)) {
				return subscribed;
			}
		}
		return null;
	}

	@Override
	public void migrationFinished(IMigrationEventSource sender) {
		if (!(sender instanceof MigrationRouterPO<?>)) {
			MigrationException ex = new MigrationException(
					"Sender for migrationFinished-event for SimplePlanMigration must be of type MigrationRouterPO");
			fireMigrationFailedEvent(sender, ex);
			return;
		}
		LOG.debug("Migration finished for: "
				+ sender.getClass().getSimpleName() + " (#" + sender.hashCode()
				+ ")");
		MigrationRouterPO<?> router = (MigrationRouterPO<?>) sender;
		// finishedParallelExecution(this.routerStrategy.get(router));
		try {
			finishedParallelExecutionPoC(this.routerStrategy.get(router));
		} catch (MigrationException ex) {
			fireMigrationFailedEvent(sender, ex);
		}
	}

	@Override
	public void migrationFailed(IMigrationEventSource sender, Throwable ex) {
		fireMigrationFailedEvent(sender, ex);
	}

	@Override
	public void addMigrationListener(IMigrationListener listener) {
		this.listener.add(listener);
	}

	@Override
	public void removeMigrationListener(IMigrationListener listener) {
		if (this.listener.contains(listener)) {
			this.listener.remove(listener);
		}
	}

	@Override
	public void fireMigrationFinishedEvent(IMigrationEventSource sender) {
		for (IMigrationListener listener : this.listener) {
			listener.migrationFinished(this);
		}
	}

	@Override
	public void fireMigrationFailedEvent(IMigrationEventSource sender,
			Throwable ex) {
		for (IMigrationListener listener : this.listener) {
			listener.migrationFailed(sender, ex);
		}
	}

	@Override
	public IPhysicalQuery getPhysicalQuery() {
		return this.physicalQuery;
	}

	@Override
	public boolean hasPhysicalQuery() {
		return this.physicalQuery != null;
	}
}
