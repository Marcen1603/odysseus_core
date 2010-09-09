package de.uniol.inf.is.odysseus.base;

import java.util.Map;

import de.uniol.inf.is.odysseus.util.INodeVisitor;

public class UpdateLogicalPlanVisitor
		implements
		INodeVisitor<ISubscriber<ILogicalOperator, LogicalSubscription>, ILogicalOperator> {
	
	private Map<ILogicalOperator, ILogicalOperator> replaced;

	public UpdateLogicalPlanVisitor(
			Map<ILogicalOperator, ILogicalOperator> replaced) {
		this.replaced = replaced;
	}

	@Override
	public void ascendAction(ISubscriber<ILogicalOperator, LogicalSubscription> to) {
		if (to != null ) {
			((ILogicalOperator) to).updateAfterClone(replaced);
		}
	}

	@Override
	public void descendAction(ISubscriber<ILogicalOperator, LogicalSubscription> to) {
		if (to != null ) {
			((ILogicalOperator) to).updateAfterClone(replaced);
		}
	}

	@Override
	public ILogicalOperator getResult() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void nodeAction(ISubscriber<ILogicalOperator, LogicalSubscription> op) {
		// TODO Auto-generated method stub

	}

}
