package de.uniol.inf.is.odysseus.util;

import de.uniol.inf.is.odysseus.ISubscriber;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.planmanagement.IOwnedOperator;

public class SetOwnerVisitor implements INodeVisitor<ISubscriber<ILogicalOperator, LogicalSubscription>, Void> {

	private IOperatorOwner owner;

	public SetOwnerVisitor(IOperatorOwner owner) {
		this.owner = owner;
	}
	
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
		((IOwnedOperator)op).addOwner(owner);
	}

	@Override
	public Void getResult() {
		return null;
	}

}
