package de.uniol.inf.is.odysseus.util;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.ISubscribable;
import de.uniol.inf.is.odysseus.base.ISubscriber;
import de.uniol.inf.is.odysseus.base.ISubscription;

public class AbstractTreeWalker {
	public static <R,T extends ISubscriber, H extends ISubscription<T>> R prefixWalk(ISubscriber<T, H> node, INodeVisitor<ISubscriber<T, H>, R> visitor) {
		visitor.node(node);
		for (H s:node.getSubscribedTo()){
			visitor.descend(s.getTarget());
			prefixWalk(s.getTarget(), visitor);
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
