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
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.MigrationRouterPO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.buffer.BufferPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.exception.SchedulerException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.IPlanMigratable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planmigration.IMigrationEventSource;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planmigration.IMigrationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planmigration.IPlanMigrationStrategy;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planmigration.MigrationHelper;
import de.uniol.inf.is.odysseus.core.server.planmanagement.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.scheduler.exception.NoSchedulerLoadedException;
import de.uniol.inf.is.odysseus.core.server.util.AbstractTreeWalker;
import de.uniol.inf.is.odysseus.core.server.util.GenericGraphWalker;
import de.uniol.inf.is.odysseus.core.server.util.PhysicalPlanToStringVisitor;
import de.uniol.inf.is.odysseus.core.server.util.PhysicalRestructHelper;
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
	public void migrateQuery(IServerExecutor sender, IPhysicalQuery runningQuery,
			List<IPhysicalOperator> newPlanRoots)
			throws QueryOptimizationException {
		if (runningQuery.containsCycles()) {
			throw new RuntimeException("Planmigration assumes acyclic trees");
		}

		// @Marco: Bitte so umbauen, dass beachtet wird,
		// dass ein Anfrageplan mehrere Roots haben kann.
		LOG.debug("Start planmigration.");

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

		// TODO: das klappt nur bedingt. Nämlich wenn es ausgewiesene Senken
		// (z.b. SocketSink) gibt. Sonst ist das eigentlich Unsinn.

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

		StrategyContext context = new StrategyContext(sender.getOptimizer(), runningQuery,
				newPlanRoots.get(0));
		context.setOldPlanOperatorsBeforeSources(oldPlanOperatorsBeforeSources);
		context.setLastOperatorNewPlan(lastOperatorNewPlan);
		context.setLastOperatorOldPlan(lastOperatorOldPlan);
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
			buffer.block();
			buffer.setOutputSchema(metadataUpdatePO.getOutputSchema());

			// set the source for this buffer in case it is empty
			buffer.setSource((ISource<?>) metadataUpdatePO);

			// pause execution by blocking output of buffer
			context.addBufferPO((BufferPO<?>) buffer);

			// TODO: hier können tupel verloren gehen!!! nochmal mit MG etwas
			// überlegen.
			// add buffer to source's subscriptions

			Collection<PhysicalSubscription> afterSourceSubs = ((ISource) metadataUpdatePO)
					.getSubscriptions();
			List<ISink> sinks = new ArrayList<ISink>();
			List<Integer> sinkInPorts = new ArrayList<Integer>();
			for (PhysicalSubscription<?> sub : afterSourceSubs) {
				IPhysicalOperator sink = (IPhysicalOperator) sub.getTarget();
				// filter DataSourceObserver
				if (sink.isPipe()) {
					sinks.add((ISink) sink);
					sinkInPorts.add(sub.getSinkInPort());
				}
			}

			buffer.subscribeToSource(metadataUpdatePO, 0, 0,
					metadataUpdatePO.getOutputSchema());
			PhysicalSubscription<?> bufferSub = null;
			for (PhysicalSubscription<?> sub : ((ISource<?>) metadataUpdatePO)
					.getSubscriptions()) {
				if (sub.getTarget().equals(buffer)) {
					bufferSub = sub;
				}
			}
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
				// ((AbstractPipe) op).unsubscribeFromSource(sub);
				((AbstractPipe) op).unsubscribeFromSource(metadataUpdatePO, 0,
						0, metadataUpdatePO.getOutputSchema());

				LOG.debug("Operator: " + op.getClass().getSimpleName() + " ("
						+ op.hashCode() + ") was unsubscribed from source");

				// buffer.subscribeSink((ISink<?>) op, 0, 0,
				// buffer.getOutputSchema());
				((AbstractPipe) op).subscribeToSource(buffer, 0, 0,
						buffer.getOutputSchema());

				LOG.debug("Operator: " + op.getClass().getSimpleName() + " ("
						+ op.hashCode() + ") was subscribed to buffer");
			}

			LOG.debug("Insert Blocking-Buffer after source ... done");
		}

		IPipe<?, ?> router = new MigrationRouterPO(metaDataUpdates, 0, 1);

		router.setOutputSchema(lastOperatorOldPlan.getOutputSchema());

		// context.setSelect(select);
		context.setRouter(router);
		context.setOldPlanRoot(oldPlanRoot);

		this.routerStrategy.put((MigrationRouterPO<?>) router, context);

		LOG.debug("Merging Plans ");

		// PhysicalRestructHelper.removeSubscription(oldPlanRoot,
		// lastOperatorOldPlan);
		// PhysicalRestructHelper.replaceChild(newPlanRoots.get(0),
		// lastOperatorNewPlan, router);
		// PhysicalRestructHelper.appendOperator(select, lastOperatorNewPlan);
		if (!lastOperatorOldPlan.isSource()) {
			// TODO es handelt sich um einen sink-only operator.
			// TODO lastOperator*Plan muss geändert werden und die senke muss
			// nach dem router kommen.
		}

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

		// wir brauchen allerdings einen Owner!
		SetOwnerGraphVisitor<IOwnedOperator> addVisitor = new SetOwnerGraphVisitor<>(
				runningQuery);
		GenericGraphWalker walker = new GenericGraphWalker();
		walker.prefixWalkPhysical(router, addVisitor);

		// runningQuery.initializePhysicalRoots(newPlanRoots);
		List<IPhysicalOperator> routers = new ArrayList<IPhysicalOperator>();
		routers.add(router);
		runningQuery.initializePhysicalRoots(routers);

		LOG.debug("Router has been initialized with owner "
				+ router.getOwnerIDs());

		// open auf dem neuen Plan aufrufen
		LOG.debug("Calling open on new Plan ");
		// synchronized (router) {
		try {
			router.open();
		} catch (OpenFailedException e1) {
			e1.printStackTrace();
		}
		// }
		LOG.debug("Calling open on new Plan ... done");

		// LOG.debug("Result:\n"
		// + AbstractTreeWalker.prefixWalk2(newPlanRoots.get(0),
		// new PhysicalPlanToStringVisitor()));
		LOG.debug("Result:\n"
				+ AbstractTreeWalker.prefixWalk2(router,
						new PhysicalPlanToStringVisitor()));

		// execute plans for at least 'w_max' (longest window duration)
		LOG.debug("Initializing parallel execution plan.");

		LOG.debug("Running Query: " + runningQuery.getRoots());

		// das sollte eigentlich nicht nötig sein.
		// for (IPhysicalOperator op : newPlanRoots) {
		// if (op.isSink()) {
		// try {
		// ((ISink) op).open();
		// } catch (OpenFailedException e) {
		// e.printStackTrace();
		// }
		// }
		// }

		try {
			sender.executionPlanChanged();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSchedulerLoadedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// resume by unblocking buffers
		for (BufferPO<?> buffer : context.getBufferPOs()) {
			buffer.insertMigrationMarkerPunctuation();
			buffer.unblock();
		}

		LOG.debug("Parallel execution started.");
		// we are waiting for the event that is fired.
	}

	void finishedParallelExecution(StrategyContext context) {

		LOG.debug("ParallelExecutionWaiter terminated.");
		// activate blocking buffers
		LOG.debug("Blocking " + context.getBufferPOs().size()
				+ " input buffers.");

		for (BufferPO<?> buffer : context.getBufferPOs()) {
			LOG.debug("Blocking buffer " + buffer.getClass().getSimpleName()
					+ " (" + buffer.hashCode() + ")");
			buffer.block();
			LOG.debug("Blocking buffer " + buffer.getClass().getSimpleName()
					+ " (" + buffer.hashCode() + ") done.");
		}

		// drain all tuples out of plans
		LOG.debug("Draining tuples out of plans.");
		MigrationHelper
				.drainTuples(context.getRunningQuery().getRoots().get(0));

		// remove old plan, keep buffers
		// top operators
		LOG.debug("Deinitializing parallel execution plan.");
		// TODO: beachten was mit sink-only operatoren passiert, die vll noch an
		// dem router hängen!
		PhysicalRestructHelper.replaceChild(context.getNewPlanRoot(),
				context.getRouter(), context.getLastOperatorNewPlan());
		PhysicalRestructHelper.removeSubscription(context.getRouter(),
				context.getLastOperatorOldPlan());
		PhysicalRestructHelper.removeSubscription(context.getRouter(),
				context.getLastOperatorNewPlan());
		// Workaround Ende

		// remove connection from buffers to old plan
		for (BufferPO<?> buffer : context.getBufferPOs()) {
			// for (PhysicalSubscription<?> sub : buffer.getSubscriptions()
			// .toArray(
			// new PhysicalSubscription<?>[buffer
			// .getSubscriptions().size()])) {
			// ISink<?> sink = (ISink<?>) sub.getTarget();
			// // have to test '==', because equals is overwritten
			// int ind = context.getOldPlanOperatorsBeforeSources().indexOf(
			// sink);
			// if (ind != -1
			// && context.getOldPlanOperatorsBeforeSources().get(ind) == sink) {
			// PhysicalRestructHelper.removeSubscription(sink, buffer);
			// }
			// }
			List<ISink> sinks = new ArrayList<ISink>();
			List<Integer> sinkInPorts = new ArrayList<Integer>();
			for (PhysicalSubscription<?> sub : buffer.getSubscriptions()) {
				sinks.add((ISink) sub.getTarget());
				sinkInPorts.add(sub.getSinkInPort());
			}
			ISource source = buffer.getSubscribedToSource(0).getTarget();
			PhysicalRestructHelper.removeOperator(source, 0, sinks,
					sinkInPorts, buffer);
		}

		// clean up, removing ownership of every operator
		// remove any metadata on old plan operators
		AbstractTreeWalker.prefixWalk2(context.getLastOperatorOldPlan(),
				new CleanOperatorsVisitor(context.getRunningQuery()));

		// push data from buffers into plan
		LOG.debug("Pushing data from BufferPOs.");
		for (BufferPO<?> buffer : context.getBufferPOs()) {
			for (int i = 0; i < buffer.size(); i++) {
				buffer.transferNext();
			}
		}

		// TODO: Warum dies? Die Anfrage ist doch schon drin, oder?

		context.getRunningQuery().initializePhysicalRoots(
				context.getRunningQuery().getRoots());

		// remove buffers, thereby resuming query processing
		LOG.debug("Removing buffers.");
		for (BufferPO<?> buffer : context.getBufferPOs()) {
			LOG.debug("Remove buffer " + buffer.getClass().getSimpleName()
					+ " (" + buffer.hashCode() + ")");
			ISource<?> source = buffer.getSubscribedToSource(0).getTarget();
			List<ISink<?>> sinks = new ArrayList<ISink<?>>();
			for (PhysicalSubscription<?> sub : buffer.getSubscriptions()) {
				sinks.add((ISink<?>) sub.getTarget());
			}
			PhysicalRestructHelper.atomicReplaceSink(source, buffer, sinks);
			for (PhysicalSubscription<?> sub : buffer.getSubscriptions()
					.toArray(
							new PhysicalSubscription<?>[buffer
									.getSubscriptions().size()])) {
				ISink<?> sink = (ISink<?>) sub.getTarget();
				PhysicalRestructHelper.removeSubscription(sink, buffer);
			}
			buffer.removeOwner(context.getRunningQuery());
			LOG.debug("Remove buffer " + buffer.getClass().getSimpleName()
					+ " (" + buffer.hashCode() + ") done.");
		}

		// new plan is ready and running
		LOG.debug("Planmigration finished. Result:"
				+ AbstractTreeWalker.prefixWalk2(context.getRunningQuery()
						.getRoots().get(0), new PhysicalPlanToStringVisitor()));
		context.getOptimizer().handleFinishedMigration(
				context.getRunningQuery());
		fireMigrationFinishedEvent(this);
	}

	@Override
	public void migrationFinished(IMigrationEventSource sender) {
		MigrationRouterPO<?> router = (MigrationRouterPO<?>) sender;
		finishedParallelExecution(this.routerStrategy.get(router));
	}

	@Override
	public void migrationFailed(IMigrationEventSource sender) {
		// TODO Auto-generated method stub

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
	public void fireMigrationFailedEvent(IMigrationEventSource sender) {
		// TODO Auto-generated method stub

	}
}
