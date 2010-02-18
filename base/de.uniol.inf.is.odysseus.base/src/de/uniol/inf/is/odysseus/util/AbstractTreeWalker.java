package de.uniol.inf.is.odysseus.util;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.ISubscriber;
import de.uniol.inf.is.odysseus.base.ISubscription;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalSubscription;

@SuppressWarnings("unchecked")
public class AbstractTreeWalker {
	public static <R,T extends ISubscriber, H extends ISubscription<T>> R prefixWalk(ISubscriber<T, H> node, INodeVisitor<ISubscriber<T, H>, R> visitor) {
		visitor.node(node);
		for (H s:node.getSubscribedToSource()){
			visitor.descend(s.getTarget());
			prefixWalk(s.getTarget(), visitor);
			visitor.ascend(node);
		}
		return visitor.getResult();
	}
	
	public static <R> R prefixWalk2(IPhysicalOperator node, INodeVisitor<IPhysicalOperator,R> visitor) {
		visitor.node(node);
		if (!node.isSink()) {
			return null;
		}
		for (PhysicalSubscription<?> s : ((ISink<?>)node).getSubscribedToSource()){
			IPhysicalOperator t = (IPhysicalOperator) s.getTarget();
			visitor.descend(t);
			prefixWalk2(t, visitor);
			visitor.ascend(node);
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
