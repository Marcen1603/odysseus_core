package de.uniol.inf.is.odysseus.planmanagement.optimization.migration;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.intervalapproach.window.AbstractWindowTIPO;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalSubscription;
import de.uniol.inf.is.odysseus.physicaloperator.base.SplitPO;

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
	
	// FIXME: root ist jetzt die letzte senke
	private static IPhysicalOperator copyTree(IPhysicalOperator op, IPhysicalOperator parentNewPlan,
			PhysicalSubscription parentSubscription) throws CloneNotSupportedException {
		System.out.println(op.getName());
		
		if (!op.isSink()) {
			// copy source
			ISource<?> copy = (ISource<?>) op.clone();
			
			// subscribe to source
			((ISink)parentNewPlan).subscribeToSource(copy, parentSubscription.getSinkInPort(),
					parentSubscription.getSourceOutPort(), parentSubscription.getSchema());
			
			return copy;
		}
		
		// create copy
		ISink<?> copy = (ISink<?>) op.clone();
		// TODO: empty sweeparea
		
		if (parentNewPlan != null) {
			// subscribe it's parent to copied operator
			((ISink)parentNewPlan).subscribeToSource(copy, parentSubscription.getSinkInPort(),
					parentSubscription.getSourceOutPort(), parentSubscription.getSchema());
		}
		
		// navigate depth-first through plan
		for (int i=0; i<((ISink<?>)op).getSubscribedToSource().size(); i++) {
			PhysicalSubscription<?> pSub = ((ISink<?>)op).getSubscribedToSource(i);
			copyTree((IPhysicalOperator)pSub.getTarget(), (IPhysicalOperator)copy, pSub);
		}
		return copy;
	}
	
	public static List<ISource<?>> getSources(IPhysicalOperator op) {
		List<ISource<?>> sources = new ArrayList<ISource<?>>();
		getSources(sources,op);
		return sources;
	}
	
	private static void getSources(List<ISource<?>> sources, IPhysicalOperator op) {
		if (!op.isSink()) {
			sources.add((ISource<?>)op);
			return;
		}
		for (PhysicalSubscription<?> sub : ((ISink<?>)op).getSubscribedToSource()) {
			getSources(sources, (IPhysicalOperator)sub.getTarget());
		}
	}
	
	public static List<ISink<?>> getOperatorsBeforeSplit(IPhysicalOperator op) {
		List<ISink<?>> operators = new ArrayList<ISink<?>>();
		getOperatorsBeforeSplit(operators, op);
		return operators;
	}
	
	private static void getOperatorsBeforeSplit(List<ISink<?>> operators, IPhysicalOperator op) {
		for (PhysicalSubscription<?> sub : ((ISink<?>)op).getSubscribedToSource()) {
			IPhysicalOperator target = (IPhysicalOperator)sub.getTarget();
			if (target instanceof SplitPO) {
				operators.add((ISink<?>) op);
				continue;
			}
			getOperatorsBeforeSplit(operators, target);
		}
	}
	
	public static long getLongestWindowSize(IPhysicalOperator root) {
		if (!root.isSink()) {
			return 0L;
		}
		long wMax = 0L;
		if (root instanceof AbstractWindowTIPO) {
			wMax = ((AbstractWindowTIPO)root).getWindowSize();
		}
		for (PhysicalSubscription<?> sub : ((ISink<?>)root).getSubscribedToSource()) {
			long w = getLongestWindowSize((IPhysicalOperator)sub.getTarget());
			if (w > wMax) {
				wMax = w;
			}
		}
		return wMax;
	}

}
