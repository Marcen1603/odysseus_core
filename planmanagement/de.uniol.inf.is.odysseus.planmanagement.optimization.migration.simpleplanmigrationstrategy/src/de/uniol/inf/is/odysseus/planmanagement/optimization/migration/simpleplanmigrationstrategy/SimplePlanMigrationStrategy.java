package de.uniol.inf.is.odysseus.planmanagement.optimization.migration.simpleplanmigrationstrategy;

import java.util.List;
import java.util.Vector;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
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
import de.uniol.inf.is.odysseus.planmanagement.optimization.planmigration.IPlanMigrationStrategie;

/**
 * 
 * @author Tobias Witt
 *
 */
public class SimplePlanMigrationStrategy implements IPlanMigrationStrategie {

	@Override
	public IEditableExecutionPlan migratePlan(IPlanMigratable sender,
			IEditableExecutionPlan newExecutionPlan) {
		// TODO: queries stoppen?
		// benötige mehr Kontrolle als IEditableExecutionPlan

		List<IPhysicalOperator> newRoots = new Vector<IPhysicalOperator>();
		for (IPhysicalOperator pOp : newExecutionPlan.getRoots()) {
			newRoots.add(prepareParallelExecution(pOp));
		}
		newExecutionPlan.setRoots(newRoots);
		
		// TODO: partial plan anpassen?!
		
		return newExecutionPlan;
	}
	
	/**
	 * 
	 * @param root
	 * @return new root of operator tree
	 */
	private IPhysicalOperator prepareParallelExecution(IPhysicalOperator root) {
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
			IPhysicalOperator parentNewPlan, PhysicalSubscription parentSubscription) {
		System.out.println(op.getName());
		
		if (!op.isSink()) {
			// unsubscribe from source
			((ISink<?>)parentOldPlan).unsubscribeFromSource(parentSubscription);
			
			// create splitPO
			List<IPredicate<?>> predicates = new Vector<IPredicate<?>>();
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
		ISink<?> copy = ((ISink<?>)op).createCopy();
		
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
