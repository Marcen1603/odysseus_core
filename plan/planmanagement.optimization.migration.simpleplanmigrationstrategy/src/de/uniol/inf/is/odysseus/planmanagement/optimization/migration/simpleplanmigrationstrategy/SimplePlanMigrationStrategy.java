package de.uniol.inf.is.odysseus.planmanagement.optimization.migration.simpleplanmigrationstrategy;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.IWindow;
import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.base.IWindow.WindowType;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.base.predicate.FalsePredicate;
import de.uniol.inf.is.odysseus.intervalapproach.DefaultHeartbeatGeneration;
import de.uniol.inf.is.odysseus.intervalapproach.TITransferArea;
import de.uniol.inf.is.odysseus.physicaloperator.base.BlockingBuffer;
import de.uniol.inf.is.odysseus.physicaloperator.base.IPipe;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalPlanToStringVisitor;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalRestructHelper;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalSubscription;
import de.uniol.inf.is.odysseus.physicaloperator.base.SelectPO;
import de.uniol.inf.is.odysseus.physicaloperator.base.UnionPO;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizer;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IPlanMigratable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.migration.MigrationHelper;
import de.uniol.inf.is.odysseus.planmanagement.optimization.planmigration.IPlanMigrationStrategy;
import de.uniol.inf.is.odysseus.util.AbstractTreeWalker;


/**
 * SimplePlanMigrationStrategy transfers a currently running physical plan into
 * a new given plan that has empty operator states. Therefore, both plans are
 * running parallel, while only the old plan does the output. When all states in
 * the new plan are filled, the old one is removed and the new plan continues
 * query processing. This strategy is based on the "Generalized Parallel Track"
 * strategy by ZHU, Yali ; RUNDENSTEINER, Elke A. ; HEINEMAN, George T..
 * 
 * @author Tobias Witt
 * 
 */
public class SimplePlanMigrationStrategy implements IPlanMigrationStrategy {

	private Logger logger;

	public SimplePlanMigrationStrategy() {
		this.logger = LoggerFactory
				.getLogger(SimplePlanMigrationStrategy.class);
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

	@SuppressWarnings("unchecked")
	@Override
	public void migrateQuery(IOptimizer sender, IQuery runningQuery,
			List<IPhysicalOperator> newPlanRoots) throws QueryOptimizationException {
		if(0 == 0){
			throw new RuntimeException("Planmigration assumes acyclic trees, " +
					"however we can have cyclic graphs. Migrations will not work.\n" +
					"You can remove this exception, however check that the query only contains a tree");
		}
		
		// @Marco: Bitte so umbauen, dass beachtet wird,
		// dass ein Anfrageplan mehrere Roots haben kann.
		this.logger.debug("Start planmigration.");

		// install both plans for parallel execution
		IPhysicalOperator oldPlanRoot = runningQuery.getRoots().get(0);
		List<ISource<?>> oldPlanSources = MigrationHelper
				.getPseudoSources(oldPlanRoot);
		List<IPhysicalOperator> oldPlanOperatorsBeforeSources = MigrationHelper
				.getOperatorsBeforeSources(oldPlanRoot, oldPlanSources);
		List<IPhysicalOperator> newPlanOperatorsBeforeSources = MigrationHelper
				.getOperatorsBeforeSources(newPlanRoots.get(0), oldPlanSources);

		// get last operators before output sink
		IPhysicalOperator lastOperatorOldPlan;
		if (oldPlanRoot.isSource()) {
			throw new QueryOptimizationException(
					"Migration strategy needs a sink only as operator root.");
		} else {
			// expects last sink to have exactly one subscription
			lastOperatorOldPlan = ((ISink<?>) oldPlanRoot)
					.getSubscribedToSource(0).getTarget();
		}
		IPhysicalOperator lastOperatorNewPlan;
		if (newPlanRoots.get(0).isSource()) {
			throw new QueryOptimizationException(
					"Migration strategy needs a sink only as operator root.");
		} else {
			lastOperatorNewPlan = ((ISink<?>) newPlanRoots.get(0))
					.getSubscribedToSource(0).getTarget();
		}

		// get longest window
		IWindow wMax = MigrationHelper.getLongestWindow(lastOperatorNewPlan);
		if (wMax != null && wMax.getWindowType() != WindowType.TIME_BASED) {
			throw new QueryOptimizationException(
					"Only time based windows are supported.");
		}

		StrategyContext context = new StrategyContext(sender, runningQuery,
				newPlanRoots.get(0));
		context.setOldPlanOperatorsBeforeSources(oldPlanOperatorsBeforeSources);
		context.setLastOperatorNewPlan(lastOperatorNewPlan);
		context.setLastOperatorOldPlan(lastOperatorOldPlan);
		context.setwMax(wMax);

		this.logger.debug("Preparing plan for parallel execution.");
		// insert buffers before sources
		for (ISource<?> source : oldPlanSources) {
			this.logger.debug("Insert Blocking-Buffer after source " +source);
			BlockingBuffer buffer = new BlockingBuffer(true);
			try {
				buffer.open();
			} catch (OpenFailedException e) {
				// Buffer not connected, so no errors can occur
				e.printStackTrace();
			}
			buffer.setOutputSchema(source.getOutputSchema());
			// pause execution by blocking output of buffer
			context.getBlockingBuffers().add((BlockingBuffer<?>) buffer);

			List<PhysicalSubscription<?>> unSubList = new ArrayList<PhysicalSubscription<?>>();
			for (PhysicalSubscription<?> sub : source.getSubscriptions()) {
				IPhysicalOperator o = (IPhysicalOperator) sub.getTarget();
				if (oldPlanOperatorsBeforeSources.contains(o)
						|| newPlanOperatorsBeforeSources.contains(o)) {
					// operator has to belong to this query
					unSubList.add(sub);
					// subscribe first operators to buffer
					PhysicalRestructHelper.appendOperator(o, buffer);
				}
			}

			// replace direct connection to source with connection to buffer in
			// one atomic step
			PhysicalRestructHelper.atomicReplaceSink(source, unSubList, buffer);
			this.logger.debug("Insert Blocking-Buffer after source ... done");
		}

		this.logger.debug("Adding False Select to new Plan " );
		// 'merge' operator at top, discarding tuples of new plan
		// realized with base operators union and select with falsepredicate
		IPipe<?, ?> select = null;
		select = new SelectPO(new FalsePredicate());
		((SelectPO)select).setHeartbeatGenerationStrategy(new DefaultHeartbeatGeneration());
		select.setOutputSchema(lastOperatorNewPlan.getOutputSchema());
		IPipe<?, ?> union = null;
		// only interval approach like in TUnionTIPO.drl supported
		if (runningQuery
				.getBuildParameter()
				.getTransformationConfiguration()
				.getMetaTypes()
				.contains(
						"de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval")) {
			union = new UnionPO(new TITransferArea());
		} else {
			throw new QueryOptimizationException(
					"Approach for UnionPO not supported.");
		}
		union.setOutputSchema(lastOperatorOldPlan.getOutputSchema());
		context.setSelect(select);
		context.setUnion(union);
		context.setOldPlanRoot(oldPlanRoot);
		
		this.logger.debug("Merging Plans " );

		PhysicalRestructHelper.removeSubscription(oldPlanRoot,
				lastOperatorOldPlan);
		PhysicalRestructHelper.replaceChild(newPlanRoots.get(0), lastOperatorNewPlan,
				union);
		PhysicalRestructHelper.appendOperator(select, lastOperatorNewPlan);
		PhysicalRestructHelper.appendBinaryOperator(union, lastOperatorOldPlan,
				select);

		// TODO Workaround, weil UnionPO nicht funktioniert. Es wird fuer die
		// Zeit
		// der parallelen Ausfuehrung noch der alte Ausgabeoperator benutzt.
		// Sobald
		// Union geht, muss nur diese Zeile und in finishedParallelExecution
		// markierte
		// Zeilen entfernt werden.
		// Anm (MG). Es werder hier die Teilpläne zusammen an die Senke gehängt.Dann
		// stimmt aber u.U. die Ausgabereihenfolge der Tupel nicht mehr!!
		//PhysicalRestructHelper.appendOperator(oldPlanRoot, lastOperatorOldPlan);
		// Workaround Ende
		
		// open auf dem neuen Plan aufrufen
		this.logger.debug("Calling open on new Plan " );
		synchronized (union) {
			try {
				union.open();
			} catch (OpenFailedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}			
		}
		this.logger.debug("Calling open on new Plan ... done" );

		this.logger.debug("Result:\n"
				+ AbstractTreeWalker.prefixWalk2(newPlanRoots.get(0),
						new PhysicalPlanToStringVisitor()));

		// execute plans for at least 'w_max' (longest window duration)
		this.logger.debug("Initializing parallel execution plan.");
		try {
			runningQuery.initializePhysicalRoots(newPlanRoots);
		} catch (OpenFailedException e) {
			throw new QueryOptimizationException(
					"Failed to initialize parallel execution plan.", e);
		}
		// resume by unblocking buffers
		for (BlockingBuffer<?> buffer : context.getBlockingBuffers()) {
			buffer.unblock();
		}

		this.logger.debug("Parallel execution started.");
		if (wMax == null) {
			this.logger
					.debug("No windows, can finish parallel execution instantly.");
			finishedParallelExecution(context);
		} else if (wMax.getWindowType() == WindowType.TIME_BASED) {
			new Thread(new ParallelExecutionWaiter(this, context)).start();
			this.logger.debug("ParallelExecutionWaiter started with "
					+ wMax.getWindowSize() + "ms waiting period.");
		}
	}

	void finishedParallelExecution(StrategyContext context) {
		if(0 == 0){
			throw new RuntimeException("Planmigration assumes acyclic trees, " +
					"however we can have cyclic graphs. Migrations will not work.\n" +
					"You can remove this exception, however check that the query only contains a tree");
		}
		
		if (logger.isDebugEnabled()) {
			this.logger.debug("ParallelExecutionWaiter terminated.");
			// activate blocking buffers
			this.logger.debug("Blocking " + context.getBlockingBuffers().size()
					+ " input buffers.");
		}
		for (BlockingBuffer<?> buffer : context.getBlockingBuffers()) {
			logger.debug("Blocking buffer "+buffer);
			buffer.block();
			logger.debug("Blocking buffer "+buffer+" done.");
		}

		// drain all tuples out of plans
		this.logger.debug("Draining tuples out of plans.");
		MigrationHelper.drainTuples(context.getRunningQuery().getRoots().get(0));

		// remove old plan, keep buffers
		// top operators
		this.logger.debug("Deinitializing parallel execution plan.");
		PhysicalRestructHelper.replaceChild(context.getNewPlanRoot(), context
				.getUnion(), context.getLastOperatorNewPlan());
		PhysicalRestructHelper.removeSubscription(context.getUnion(), context
				.getLastOperatorOldPlan());
		PhysicalRestructHelper.removeSubscription(context.getUnion(), context
				.getSelect());
		PhysicalRestructHelper.removeSubscription(context.getSelect(), context
				.getLastOperatorNewPlan());

		// TODO Workaround s.o.
//		PhysicalRestructHelper.removeSubscription(context.getOldPlanRoot(),
//				context.getLastOperatorOldPlan());
//		PhysicalRestructHelper.removeSubscription(context.getNewPlanRoot(),
//				context.getLastOperatorNewPlan());
//		PhysicalRestructHelper.appendOperator(context.getOldPlanRoot(), context
//				.getLastOperatorNewPlan());
//		try {
//			context.getRunningQuery().setRoot(context.getOldPlanRoot());
//		} catch (OpenFailedException e1) {
//			e1.printStackTrace();
//		}
		// Workaround Ende

		// remove connection from buffers to old plan
		for (BlockingBuffer<?> buffer : context.getBlockingBuffers()) {
			for (PhysicalSubscription<?> sub : buffer.getSubscriptions()
					.toArray(
							new PhysicalSubscription<?>[buffer
									.getSubscriptions().size()])) {
				ISink<?> sink = (ISink<?>) sub.getTarget();
				// have to test '==', because equals is overwritten
				int ind = context.getOldPlanOperatorsBeforeSources().indexOf(
						sink);
				if (ind != -1
						&& context.getOldPlanOperatorsBeforeSources().get(ind) == sink) {
					PhysicalRestructHelper.removeSubscription(sink, buffer);
				}
			}
		}

		// clean up, removing ownership of every operator
		// remove any metadata on old plan operators
		AbstractTreeWalker.prefixWalk2(context.getLastOperatorOldPlan(),
				new CleanOperatorsVisitor(context.getRunningQuery()));

		// push data from buffers into plan
		this.logger.debug("Pushing data from BlockingBuffers.");
		for (BlockingBuffer<?> buffer : context.getBlockingBuffers()) {
			for (int i = 0; i < buffer.size(); i++) {
				buffer.transferNext();
			}
		}

		// TODO: Warum dies? Die Anfrage ist doch schon drin, oder?
		
		// set and initialize new physical plan
		// adds query as operator owner, too
		try {
			context.getRunningQuery().initializePhysicalRoots(
					context.getRunningQuery().getRoots());
		} catch (OpenFailedException e) {
			this.logger.error("Failed to initialize new execution plan.", e);
		}

		// remove buffers, thereby resuming query processing
		this.logger.debug("Removing buffers.");
		for (BlockingBuffer<?> buffer : context.getBlockingBuffers()) {
			this.logger.debug("Remove buffer "+buffer);
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
			this.logger.debug("Remove buffer "+buffer+" done.");
		}

		// new plan is ready and running
		this.logger.debug("Planmigration finished. Result:"
				+ AbstractTreeWalker.prefixWalk2(context.getRunningQuery()
						.getRoots().get(0), new PhysicalPlanToStringVisitor()));
		context.getOptimizer().handleFinishedMigration(
				context.getRunningQuery());
	}

}
