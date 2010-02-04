package de.uniol.inf.is.odysseus.planmanagement.optimization.migration.simpleplanmigrationstrategy;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.base.predicate.TruePredicate;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalSubscription;
import de.uniol.inf.is.odysseus.physicaloperator.base.SplitPO;
import de.uniol.inf.is.odysseus.physicaloperator.base.SweepArea;
import de.uniol.inf.is.odysseus.physicaloperator.base.UnionPO;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IEditableExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IPlanMigratable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.planmigration.IPlanMigrationStrategie;
import de.uniol.inf.is.odysseus.scheduler.exception.NoSchedulerLoadedException;

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
		this.logger.info("Start Planmigration.");
		
		try {
			sender.getSchedulerManager().stopScheduling();
		} catch (NoSchedulerLoadedException e) {
			e.printStackTrace();
			return newExecutionPlan;
		}

		try {
			List<IPhysicalOperator> newRoots = new ArrayList<IPhysicalOperator>();
			for (IPhysicalOperator pOp : newExecutionPlan.getRoots()) {
				newRoots.add(prepareParallelExecution(pOp));
			}
			newExecutionPlan.setRoots(newRoots);
			
			// TODO: partial plan anpassen?!
			
		} catch (Exception e1) {
			this.logger.error("Plan migration failed: "+e1.getMessage(),e1);
		}
		
		try {
			sender.getSchedulerManager().startScheduling();
		} catch (NoSchedulerLoadedException e) {
			e.printStackTrace();
		} catch (OpenFailedException e) {
			e.printStackTrace();
		}
		
		this.logger.info("Finished Planmigration.");
		
		return newExecutionPlan;
	}
	
	/**
	 * 
	 * @param root
	 * @return new root of operator tree
	 */
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
	}

}
