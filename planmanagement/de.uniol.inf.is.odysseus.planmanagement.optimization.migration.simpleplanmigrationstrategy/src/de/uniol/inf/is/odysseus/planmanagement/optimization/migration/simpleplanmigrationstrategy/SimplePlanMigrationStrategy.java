package de.uniol.inf.is.odysseus.planmanagement.optimization.migration.simpleplanmigrationstrategy;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.IOperatorOwner;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.base.predicate.TruePredicate;
import de.uniol.inf.is.odysseus.physicaloperator.base.BlockingBuffer;
import de.uniol.inf.is.odysseus.physicaloperator.base.IPipe;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalSubscription;
import de.uniol.inf.is.odysseus.physicaloperator.base.SplitPO;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IEditableExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizer;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IPlanMigratable;
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
		return newExecutionPlan;
	}
	
	/*
	private IPhysicalOperator prepareParallelExecution(IPhysicalOperator root) throws QueryOptimizationException {
		// create copy of operator-tree
		// TODO: mit treewalker probleme, da IPhysicalOperator kein ISubscriber<T, H> ist
		// evtl. mit Umweg mit ISink, ISource ?
		//AbstractTreeWalker.prefixWalk2(root, new CopyPhysicalPlanVisitor());
		
		// create union and set old plan as source
		UnionPO<?> union = new UnionPO(new SweepArea(), null); // TODO: union helper für intervall ansatz?
		union.setOutputSchema(root.getOutputSchema());
		((ISink)union).subscribeToSource(root, 0, 0, root.getOutputSchema());
		
		copyOperatorTree(root, union, null, null);
		
		// TODO: was ist mit verknüpfung zu ausgabe hinter root?
		// diese muss gelöst werden und an union gehängt werden
		
		// TODO: insert buffer
		// add monitor
		
		return union;
	}
	
	private void copyOperatorTree(IPhysicalOperator op, IPhysicalOperator parentOldPlan, 
			IPhysicalOperator parentNewPlan, PhysicalSubscription parentSubscription) throws QueryOptimizationException {
		System.out.println(op.getName());
		
		if (!op.isSink()) {
			// unsubscribe from source
			((ISink<?>)parentOldPlan).unsubscribeFromSource(parentSubscription);
			
			// create splitPO
			List<IPredicate<?>> predicates = new ArrayList<IPredicate<?>>();
			predicates.add(new TruePredicate());
			SplitPO<?> split = new SplitPO(predicates);
			split.setOutputSchema(op.getOutputSchema());
			
			// subscribe splitPO to source and parents to splitPO
			split.subscribeToSource((ISource)op, 0, 0, op.getOutputSchema());
			((ISink)parentOldPlan).subscribeToSource(split, parentSubscription.getSinkPort(), 0, split.getOutputSchema());
			((ISink)parentNewPlan).subscribeToSource(split, parentSubscription.getSinkPort(), 1, split.getOutputSchema());
			
			return;
		}
		
		// create copy
		ISink<?> copy = null;
		try {
			copy = (ISink<?>) op.clone();
		} catch (CloneNotSupportedException e) {
			throw new QueryOptimizationException("Operator cannot be cloned.",e);
		}
		
		if (parentNewPlan == null) {
			// subscribe union to root of new plan
			((ISink)parentOldPlan).subscribeToSource(copy, 1, 0, copy.getOutputSchema());
		} else {
			// subscribe it's parent to copied operator
			((ISink)parentNewPlan).subscribeToSource(copy, parentSubscription.getSinkPort(),
					parentSubscription.getSourcePort(), parentSubscription.getSchema());
		}
		
		// navigate depth-first through plan
		for (int i=0; i<((ISink<?>)op).getSubscribedToSource().size(); i++) {
			PhysicalSubscription<?> pSub = ((ISink<?>)op).getSubscribedToSource(i);
			copyOperatorTree((IPhysicalOperator)pSub.getTarget(), op, (IPhysicalOperator)copy, pSub);
		}
	}*/

	@Override
	public void migrateQuery(IOptimizer sender, IEditableQuery runningQuery,
			IPhysicalOperator newPlanRoot) {
		
		// install both plans for parallel execution
		IPhysicalOperator oldPlanRoot = runningQuery.getRoot();
		List<ISource> oldPlanSources = MigrationHelper.getSources(oldPlanRoot);
		
		// insert buffers before sources
		for (ISource source : oldPlanSources) {
			IPipe buffer = new BlockingBuffer();
			List<IPredicate<?>> predicates = new ArrayList<IPredicate<?>>();
			predicates.add(new TruePredicate());
			IPipe split = new SplitPO(predicates);
			
			for (Object sub : source.getSubscriptions()) {
				IPhysicalOperator o = (IPhysicalOperator)((PhysicalSubscription)sub).getTarget();
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
			}
			
			// subscribe split to buffer and buffer to source
		}
		
		// 'merge' operator at top, discarding tuples of new plan
		
		// execute plans for at least 'w_max' (longest window duration)
		
		// activate blocking buffers
		
		// drain all tuples out of plans
		
		// remove old plan, continue new one
		
	}

}
