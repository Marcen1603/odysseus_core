package de.uniol.inf.is.odysseus.planmanagement.optimization.migration.simpleplanmigrationstrategy;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.IOperatorOwner;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery;
import de.uniol.inf.is.odysseus.base.predicate.FalsePredicate;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.base.predicate.TruePredicate;
import de.uniol.inf.is.odysseus.physicaloperator.base.BlockingBuffer;
import de.uniol.inf.is.odysseus.physicaloperator.base.IPipe;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalSubscription;
import de.uniol.inf.is.odysseus.physicaloperator.base.SelectPO;
import de.uniol.inf.is.odysseus.physicaloperator.base.SplitPO;
import de.uniol.inf.is.odysseus.physicaloperator.base.SweepArea;
import de.uniol.inf.is.odysseus.physicaloperator.base.UnionPO;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IEditableExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizer;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IPlanMigratable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.migration.MigrationHelper;
import de.uniol.inf.is.odysseus.planmanagement.optimization.planmigration.IPlanMigrationStrategie;

/**
 * 
 * @author Tobias Witt
 *
 */
public class SimplePlanMigrationStrategy implements IPlanMigrationStrategie {
	
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
		
		// install both plans for parallel execution
		IPhysicalOperator oldPlanRoot = runningQuery.getRoot();
		List<ISource<?>> oldPlanSources = MigrationHelper.getSources(oldPlanRoot);
		
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
		
		// insert buffers before sources
		for (ISource<?> source : oldPlanSources) {
			IPipe buffer = new BlockingBuffer();
			buffer.setOutputSchema(source.getOutputSchema());
			context.getBlockingBuffers().add((BlockingBuffer<?>)buffer);
			List<IPredicate<?>> predicates = new ArrayList<IPredicate<?>>();
			predicates.add(new TruePredicate());
			IPipe split = new SplitPO(predicates);
			split.setOutputSchema(buffer.getOutputSchema());
			context.getSplits().add(split);
			
			int freeSourcePort = -1;
			for (PhysicalSubscription<?> sub : source.getSubscriptions()) {
				IPhysicalOperator o = (IPhysicalOperator)sub.getTarget();
				boolean belongsToThisQuery = false;
				for (IOperatorOwner owner : o.getOwner()) {
					if (owner == runningQuery) {
						belongsToThisQuery = true;
						break;
					}
				}
				if (!belongsToThisQuery) {
					continue;
				}
				// remove subscription to source and subscribe to split
				((ISink)o).unsubscribeFromSource(sub);
				((ISink)o).subscribeToSource(split, sub.getSinkInPort(), sub.getSourceOutPort(), split.getOutputSchema());
				if (freeSourcePort == -1) {
					freeSourcePort = sub.getSourceOutPort();
				}
			}
			
			// subscribe split to buffer and buffer to source
			split.subscribeToSource(buffer, 0, 0, buffer.getOutputSchema());
			buffer.subscribeToSource(source, 0, freeSourcePort, source.getOutputSchema());
		}
		
		// 'merge' operator at top, discarding tuples of new plan
		// realized with base operators union and select with falsepredicate
		PhysicalSubscription<?> oldPlanRootSub = ((ISink<?>)oldPlanRoot).getSubscribedToSource(0);
		((ISink)oldPlanRoot).unsubscribeFromSource(oldPlanRootSub);
		PhysicalSubscription<?> newPlanRootSub = ((ISink<?>)newPlanRoot).getSubscribedToSource(0);
		((ISink)newPlanRoot).unsubscribeFromSource(newPlanRootSub);
		IPipe union = new UnionPO(new SweepArea(), null);	// FIXME: unionhelper for interval approach?
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
		
		
		// execute plans for at least 'w_max' (longest window duration)
		long wMax = MigrationHelper.getLongestWindowSize(lastOperatorNewPlan);
		context.setMs(wMax);
		try {
			runningQuery.initializePhysicalPlan(newPlanRoot);
		} catch (OpenFailedException e) {
			throw new QueryOptimizationException("Failed to initialize parallel execution plan.", e);
		}
		runningQuery.start();
		new Thread(new ParallelExecutionWaiter(this, context)).start();
		
	}

	void finishedParallelExecution(StrategyContext context) {
		// activate blocking buffers
		for (BlockingBuffer<?> buffer : context.getBlockingBuffers()) {
			buffer.block();
		}
		
		// drain all tuples out of plans
		// TODO: monitor if there are no more operators that can be scheduled and produce output
		// TODO: duerfen operatoren explizit gescheduled werden?
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {}
		context.getRunningQuery().stop();
		
		// remove old plan and migration operators, keep buffers
		// TODO: muessen owner entfernt/hinzugefuegt werden?
		// top operators
		ISink<?> newPlanRoot = (ISink<?>)context.getNewPlanRoot();
		PhysicalSubscription newRootSubscription = newPlanRoot.getSubscribedToSource(0);
		newPlanRoot.unsubscribeFromSource(newRootSubscription);
		
		ISink<?> union = (ISink<?>)newRootSubscription.getTarget();
		union.unsubscribeFromSource((PhysicalSubscription)union.getSubscribedToSource(0));
		PhysicalSubscription unionSubscription = union.getSubscribedToSource(1);
		union.unsubscribeFromSource(unionSubscription);
		
		ISink<?> select = (ISink<?>)unionSubscription.getTarget();
		PhysicalSubscription selectSubscription = select.getSubscribedToSource(0);
		select.unsubscribeFromSource(selectSubscription);
		
		IPipe lastOperator = (IPipe)selectSubscription.getTarget();
		newPlanRoot.subscribeToSource(lastOperator, newRootSubscription.getSinkInPort(), 
				selectSubscription.getSourceOutPort(), lastOperator.getOutputSchema());
		
		// connect new plan directly to buffers, unsubscribe from split
		// TODO: kann buffer mehrere ausgabestroeme haben?
		List<ISink<?>> opBeforeSplits = MigrationHelper.getOperatorsBeforeSplit(newPlanRoot);
		for (ISink<?> op : opBeforeSplits) {
			for (PhysicalSubscription sub : op.getSubscribedToSource()) {
				op.unsubscribeFromSource(sub);
				IPipe buffer = (IPipe)((IPipe)sub.getTarget()).getSubscribedToSource(0).getTarget();
				op.subscribeToSource(buffer, sub.getSinkInPort(), sub.getSourceOutPort(), buffer.getOutputSchema());
			}
		}
		
		// push data from buffers into plan
		// TODO direct push (s.o.)?
		
		// remove buffers
		for (ISink<?> op : opBeforeSplits) {
			for (PhysicalSubscription sub : op.getSubscribedToSource()) {
				op.unsubscribeFromSource(sub);
				IPipe buffer = (IPipe)sub.getTarget();
				ISource source = (ISource)buffer.getSubscribedToSource(0);
				// TODO: freier Port bei source?
				op.subscribeToSource(source, sub.getSinkInPort(), sub.getSourceOutPort(), source.getOutputSchema());
			}
		}
		
		// new plan is ready to be initialized
		
	}

}
