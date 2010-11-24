package de.uniol.inf.is.odysseus.util;

import de.uniol.inf.is.odysseus.ISubscriber;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.LogicalSubscription;

public class PrintTreeVisitor implements INodeVisitor<ISubscriber<ILogicalOperator, LogicalSubscription>, Void> {
	
	@Override
	public void ascendAction(
			ISubscriber<ILogicalOperator, LogicalSubscription> to) {
		
	}

	@Override
	public void descendAction(
			ISubscriber<ILogicalOperator, LogicalSubscription> to) {
		
	}

	@Override
	public void nodeAction(ISubscriber<ILogicalOperator, LogicalSubscription> op) {
		System.out.println(op);
	}

	@Override
	public Void getResult() {
		return null;
	}

}
