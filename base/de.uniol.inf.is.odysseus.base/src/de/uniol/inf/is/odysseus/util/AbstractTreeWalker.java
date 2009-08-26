package de.uniol.inf.is.odysseus.util;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;

public class AbstractTreeWalker {
	public static <R> R prefixWalk(ILogicalOperator node, INodeVisitor<ILogicalOperator, R> visitor) {
		visitor.node(node);
		for (int i= 0; i < node.getNumberOfInputs(); ++ i) {
			ILogicalOperator inputAO = node.getInputAO(i);
			visitor.descend(inputAO);
			prefixWalk(inputAO, visitor);
			visitor.ascend(node);
		}
		return visitor.getResult();
	}

	public static <T> T postfixWalk(ILogicalOperator op, INodeVisitor<ILogicalOperator, T> visitor) {
		for (int i= 0; i < op.getNumberOfInputs(); ++ i) {
			ILogicalOperator inputAO = op.getInputAO(i);
			visitor.descend(inputAO);
			prefixWalk(inputAO, visitor);
			visitor.ascend(op);
		}
		visitor.node(op);
		return visitor.getResult();
	}
}
