package de.uniol.inf.is.odysseus.physicaloperator.base;

import java.util.List;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;

/**
 * 
 * @author Tobias Witt
 *
 */
@SuppressWarnings("unchecked")
public class PhysicalRestructHelper {
	
	public static void appendBinaryOperator(IPhysicalOperator binaryOp, IPhysicalOperator child1, IPhysicalOperator child2) {
		((ISink<?>)binaryOp).subscribeToSource((ISource)child1, 0, 0, child1.getOutputSchema());
		((ISink<?>)binaryOp).subscribeToSource((ISource)child2, 1, 0, child2.getOutputSchema());
	}
	
	public static void appendOperator(IPhysicalOperator parent, IPhysicalOperator child) {
		((ISink<?>)parent).subscribeToSource((ISource)child, 0, 0, child.getOutputSchema());
	}
	
	public static PhysicalSubscription<?> removeSubscription(IPhysicalOperator parent, IPhysicalOperator child) {
		for (PhysicalSubscription<?> sub : ((ISink<?>)parent).getSubscribedToSource()) {
			if (sub.getTarget() == child) {
				((ISink<?>)parent).unsubscribeFromSource((PhysicalSubscription)sub);
				return sub;
			}
		}
		return null;
	}
	
	public static void replaceChild(IPhysicalOperator parent, IPhysicalOperator child, IPhysicalOperator newChild) {
		PhysicalSubscription<?> sub = removeSubscription(parent, child);
		((ISink<?>)parent).subscribeToSource((ISource)newChild, sub.getSinkInPort(), sub.getSourceOutPort(),
				newChild.getOutputSchema());
	}
	
	public static void atomicReplaceSink(ISource<?> source, List<PhysicalSubscription<?>> remove, ISink<?> sink) {
		((ISource)source).atomicReplaceSink(remove, sink, 0, 0, source.getOutputSchema());
	}
	
	public static void atomicReplaceSink(ISource<?> source, PhysicalSubscription<?> remove, List<ISink<?>> sinks) {
		((ISource)source).atomicReplaceSink(remove, sinks, 0, 0, source.getOutputSchema());
	}
	
}
