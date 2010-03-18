package de.uniol.inf.is.odysseus.planmanagement.optimization.migration.simpleplanmigrationstrategy;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery;
import de.uniol.inf.is.odysseus.base.predicate.FalsePredicate;
import de.uniol.inf.is.odysseus.intervalapproach.TITransferFunction;
import de.uniol.inf.is.odysseus.physicaloperator.base.BlockingBuffer;
import de.uniol.inf.is.odysseus.physicaloperator.base.IPipe;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalPlanToStringVisitor;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalSubscription;
import de.uniol.inf.is.odysseus.physicaloperator.base.SelectPO;
import de.uniol.inf.is.odysseus.physicaloperator.base.UnionPO;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IEditableExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizer;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IPlanMigratable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.migration.MigrationHelper;
import de.uniol.inf.is.odysseus.planmanagement.optimization.planmigration.IPlanMigrationStrategy;
import de.uniol.inf.is.odysseus.util.AbstractTreeWalker;

/**
 * 
 * @author Tobias Witt
 *
 */
public class SimplePlanMigrationStrategy implements IPlanMigrationStrategy {
	
	private Logger logger;
	
	public SimplePlanMigrationStrategy() {
		this.logger = LoggerFactory.getLogger(SimplePlanMigrationStrategy.class);
	}

	@Override
	public IEditableExecutionPlan migratePlan(IPlanMigratable sender,
			IEditableExecutionPlan newExecutionPlan) {
		// no global plan migration
		return newExecutionPlan;
	}

	@Override
	public void migrateQuery(IOptimizer sender, IEditableQuery runningQuery,
			IPhysicalOperator newPlanRoot) throws QueryOptimizationException {
		this.logger.debug("Start planmigration.");
		
		// install both plans for parallel execution
		IPhysicalOperator oldPlanRoot = runningQuery.getRoot();
		List<ISource<?>> oldPlanSources = MigrationHelper.getPseudoSources(oldPlanRoot);
		List<IPhysicalOperator> oldPlanOperatorsBeforeSources = MigrationHelper.getOperatorsBeforeSources(oldPlanRoot);
		List<IPhysicalOperator> newPlanOperatorsBeforeSources = MigrationHelper.getOperatorsBeforeSources(newPlanRoot);
		
		
		// get last operators before output sink
		IPhysicalOperator lastOperatorOldPlan;
		if (oldPlanRoot.isSource()) {
			throw new QueryOptimizationException("Migration strategy needs a sink only as operator root.");
		} else {
			// expects last sink to have exactly one subscription
			lastOperatorOldPlan = ((ISink<?>)oldPlanRoot).getSubscribedToSource(0).getTarget();
		}
		IPhysicalOperator lastOperatorNewPlan;
		if (newPlanRoot.isSource()) {
			throw new QueryOptimizationException("Migration strategy needs a sink only as operator root.");
		} else {
			lastOperatorNewPlan = ((ISink<?>)newPlanRoot).getSubscribedToSource(0).getTarget();
		}
		
		StrategyContext context = new StrategyContext(sender, runningQuery, newPlanRoot);
		context.setOldPlanOperatorsBeforeSources(oldPlanOperatorsBeforeSources);
		
		this.logger.debug("Preparing plan for parallel execution.");
		// insert buffers before sources
		for (ISource<?> source : oldPlanSources) {
			IPipe buffer = new BlockingBuffer();
			buffer.setOutputSchema(source.getOutputSchema());
			context.getBlockingBuffers().add((BlockingBuffer<?>)buffer);
			
			List<PhysicalSubscription<?>> unSubList = new ArrayList<PhysicalSubscription<?>>();
			for (PhysicalSubscription<?> sub : source.getSubscriptions()) {
				IPhysicalOperator o = (IPhysicalOperator)sub.getTarget();
				if (oldPlanOperatorsBeforeSources.contains(o) || newPlanOperatorsBeforeSources.contains(o)) {
					// operator has to belong to this query
					unSubList.add(sub);
				}
			}
			for (PhysicalSubscription<?> sub : unSubList) {
				ISink o = (ISink) sub.getTarget();
				// remove subscription to source and subscribe to buffer
				((ISource)source).unsubscribeSink(sub);
				((ISink)o).subscribeToSource(buffer, sub.getSinkInPort(), sub.getSourceOutPort(), buffer.getOutputSchema());
			}
			
			// subscribe buffer to source
			buffer.subscribeToSource(source, 0, 0, source.getOutputSchema());
		}
		
		// 'merge' operator at top, discarding tuples of new plan
		// realized with base operators union and select with falsepredicate
		PhysicalSubscription<?> oldPlanRootSub = ((ISink<?>)oldPlanRoot).getSubscribedToSource(0);
		((ISink)oldPlanRoot).unsubscribeFromSource(oldPlanRootSub);
		PhysicalSubscription<?> newPlanRootSub = ((ISink<?>)newPlanRoot).getSubscribedToSource(0);
		((ISink)newPlanRoot).unsubscribeFromSource(newPlanRootSub);
		IPipe union = new UnionPO(new TITransferFunction());	// FIXME: migration strategy only for Interval approach?
		union.setOutputSchema(lastOperatorOldPlan.getOutputSchema());
		IPipe select = new SelectPO(new FalsePredicate());
		select.setOutputSchema(lastOperatorNewPlan.getOutputSchema());
		
		((ISink)newPlanRoot).subscribeToSource(union, newPlanRootSub.getSinkInPort(), 
				newPlanRootSub.getSourceOutPort(), union.getOutputSchema());
		union.subscribeToSource(lastOperatorOldPlan, 0, 
				oldPlanRootSub.getSourceOutPort(), lastOperatorOldPlan.getOutputSchema());
		union.subscribeToSource(select, 1, 0, select.getOutputSchema());
		select.subscribeToSource(lastOperatorNewPlan, 0, 
				newPlanRootSub.getSourceOutPort(), lastOperatorNewPlan.getOutputSchema());
		
		this.logger.debug("Result:\n"+AbstractTreeWalker.prefixWalk2(newPlanRoot, new PhysicalPlanToStringVisitor()));
		
		// execute plans for at least 'w_max' (longest window duration)
		long wMax = MigrationHelper.getLongestWindowSize(lastOperatorNewPlan);
		context.setMs(wMax);
		this.logger.debug("Initializing parallel execution plan.");
		try {
			runningQuery.initializePhysicalPlan(newPlanRoot);
		} catch (OpenFailedException e) {
			throw new QueryOptimizationException("Failed to initialize parallel execution plan.", e);
		}
		runningQuery.start();
		this.logger.debug("Parallel execution started.");
		new Thread(new ParallelExecutionWaiter(this, context)).start();
		this.logger.debug("ParallelExecutionWaiter started with "+wMax+"ms waiting period.");
		
	}

	void finishedParallelExecution(StrategyContext context) {
		this.logger.debug("ParallelExecutionWaiter terminated.");
		// activate blocking buffers
		this.logger.debug("Blocking input buffers.");
		for (BlockingBuffer<?> buffer : context.getBlockingBuffers()) {
			buffer.block();
		}
		
		// drain all tuples out of plans
		this.logger.debug("Draining tuples out of plans.");
		context.getRunningQuery().stop();
		MigrationHelper.drainTuples(context.getRunningQuery().getRoot());
		
		// remove old plan, keep buffers
		// TODO: muessen owner entfernt/hinzugefuegt werden?
		// top operators
		this.logger.debug("Deinitializing parallel execution plan.");
		ISink<?> newPlanRoot = (ISink<?>)context.getNewPlanRoot();
		PhysicalSubscription newRootSubscription = newPlanRoot.getSubscribedToSource(0);
		newPlanRoot.unsubscribeFromSource(newRootSubscription);
		
		ISink<?> union = (ISink<?>)newRootSubscription.getTarget();
		union.unsubscribeFromSource((PhysicalSubscription)union.getSubscribedToSource(0));
		PhysicalSubscription unionSubscription = union.getSubscribedToSource(0);
		union.unsubscribeFromSource(unionSubscription);
		
		ISink<?> select = (ISink<?>)unionSubscription.getTarget();
		PhysicalSubscription selectSubscription = select.getSubscribedToSource(0);
		select.unsubscribeFromSource(selectSubscription);
		
		IPipe lastOperator = (IPipe)selectSubscription.getTarget();
		newPlanRoot.subscribeToSource(lastOperator, newRootSubscription.getSinkInPort(), 
				selectSubscription.getSourceOutPort(), lastOperator.getOutputSchema());
		
		// remove connection from buffers to old plan
		for (BlockingBuffer<?> buffer : context.getBlockingBuffers()) {
			ISource<?> source = buffer.getSubscribedToSource(0).getTarget();
			for (PhysicalSubscription<?> sub : buffer.getSubscriptions().toArray(new PhysicalSubscription<?>[buffer.getSubscriptions().size()])) {
				ISink sink = (ISink<?>) sub.getTarget();
				if (context.getOldPlanOperatorsBeforeSources().contains(sink)) {
					((ISource)buffer).unsubscribeSink(sub);
				}
			}
		}
		
		// push data from buffers into plan
		this.logger.debug("Pushing data from BlockingBuffers.");
		for (BlockingBuffer<?> buffer : context.getBlockingBuffers()) {
			for (int i=0; i<buffer.size(); i++) {
				buffer.transferNext();
			}
		}
		
		// remove buffers
		this.logger.debug("Removing buffers.");
		for (BlockingBuffer<?> buffer : context.getBlockingBuffers()) {
			ISource<?> source = buffer.getSubscribedToSource(0).getTarget();
			for (PhysicalSubscription<?> sub : buffer.getSubscriptions().toArray(new PhysicalSubscription<?>[buffer.getSubscriptions().size()])) {
				ISink sink = (ISink<?>) sub.getTarget();
				((ISource)buffer).unsubscribeSink(sub);
				sink.subscribeToSource(source, sub.getSinkInPort(), sub.getSourceOutPort(), source.getOutputSchema());
			}
		}
		
		// new plan is ready to be initialized
		this.logger.debug("Planmigration finished. Result:"
				+ AbstractTreeWalker.prefixWalk2(newPlanRoot, new PhysicalPlanToStringVisitor()));
		context.getOptimizer().handleFinishedMigration(context.getRunningQuery());
	}

}
