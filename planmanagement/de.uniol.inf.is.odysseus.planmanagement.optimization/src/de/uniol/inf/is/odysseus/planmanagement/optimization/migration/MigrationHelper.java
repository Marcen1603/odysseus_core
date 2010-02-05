package de.uniol.inf.is.odysseus.planmanagement.optimization.migration;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalSubscription;

/**
 * 
 * @author Tobias Witt
 *
 */
public class MigrationHelper {
	
	/**
	 * Creates a copy of a physical plan. The new plan starts with emtpy states.
	 * 
	 * @param root 
	 * 		plan to clone
	 * @return 
	 * 		new copied plan
	 * @throws CloneNotSupportedException
	 */
	public static IPhysicalOperator clonePhysicalPlan(IPhysicalOperator root) throws CloneNotSupportedException {
		return copyTree(root, null, null);
	}
	
	private static IPhysicalOperator copyTree(IPhysicalOperator op, IPhysicalOperator parentNewPlan,
			PhysicalSubscription parentSubscription) throws CloneNotSupportedException {
		System.out.println(op.getName());
		
		if (!op.isSink()) {
			// copy source
			ISource<?> copy = (ISource<?>) op.clone();
			
			// subscribe to source
			((ISink)parentNewPlan).subscribeToSource(copy, parentSubscription.getSinkPort(),
					parentSubscription.getSourcePort(), parentSubscription.getSchema());
			
			return copy;
		}
		
		// create copy
		ISink<?> copy = (ISink<?>) op.clone();
		// TODO: empty sweeparea
		
		if (parentNewPlan != null) {
			// subscribe it's parent to copied operator
			((ISink)parentNewPlan).subscribeToSource(copy, parentSubscription.getSinkPort(),
					parentSubscription.getSourcePort(), parentSubscription.getSchema());
		}
		
		// navigate depth-first through plan
		for (int i=0; i<((ISink<?>)op).getSubscribedToSource().size(); i++) {
			PhysicalSubscription<?> pSub = ((ISink<?>)op).getSubscribedToSource(i);
			copyTree((IPhysicalOperator)pSub.getTarget(), (IPhysicalOperator)copy, pSub);
		}
		return copy;
	}

}
