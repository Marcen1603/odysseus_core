package de.uniol.inf.is.odysseus.util;


import de.uniol.inf.is.odysseus.ISubscriber;
import de.uniol.inf.is.odysseus.ISubscription;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.PhysicalSubscription;

/**
 * @deprecated Should not be used any more. We have
 * a general graph walker, that can also walk through
 * trees. However, not all corresponding visitors have
 * been changed. 
 */
@SuppressWarnings("unchecked")
public class AbstractTreeWalker {
	
	
	
	public static <R,T extends ISubscriber, H extends ISubscription<T>> R prefixWalk(ISubscriber<T, H> node, INodeVisitor<ISubscriber<T, H>, R> visitor) {
		visitor.nodeAction(node);
		if (!(node instanceof ISubscriber)){
			return null;
		}
		for (H s:node.getSubscribedToSource()){
			visitor.descendAction(s.getTarget());
			prefixWalk(s.getTarget(), visitor);
			visitor.ascendAction(node);
		}
		return visitor.getResult();
	}
	
	public static <R> R prefixWalk2(IPhysicalOperator node, INodeVisitor<IPhysicalOperator,R> visitor) {
		visitor.nodeAction(node);
		if (!node.isSink()) {
			return null;
		}
		for (PhysicalSubscription<?> s : ((ISink<?>)node).getSubscribedToSource()){
			IPhysicalOperator t = (IPhysicalOperator) s.getTarget();
			visitor.descendAction(t);
			prefixWalk2(t, visitor);
			visitor.ascendAction(node);
		}
		return visitor.getResult();
	}

//	public static <T> T postfixWalk(ILogicalOperator op, INodeVisitor<ILogicalOperator, T> visitor) {
//		for (int i= 0; i < op.getNumberOfInputs(); ++ i) {
//			ILogicalOperator inputAO = op.getInputAO(i);
//			visitor.descend(inputAO);
//			prefixWalk(inputAO, visitor);
//			visitor.ascend(op);
//		}
//		visitor.node(op);
//		return visitor.getResult();
//	}
}
