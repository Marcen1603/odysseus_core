package de.uniol.inf.is.odysseus.logicaloperator.base;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.ISubscriber;
import de.uniol.inf.is.odysseus.base.LogicalSubscription;
import de.uniol.inf.is.odysseus.util.INodeVisitor;

/**
 * Builds an string representation of a logical algebra tree 
 * @author Jonas Jacobi, Marco Grawunder
 */
public class AlgebraPlanToStringVisitor implements INodeVisitor<ISubscriber<ILogicalOperator, LogicalSubscription>, String> {

	private StringBuilder builder;
	private boolean wasup;

	public AlgebraPlanToStringVisitor() {
		reset();
	}
	
	@Override
	public void descend(ISubscriber<ILogicalOperator, LogicalSubscription> sub) {
		if (this.wasup) {
			this.builder.append(',');
		} else {
			this.builder.append('(');
		}
		this.wasup = false;
	}

	@Override
	public void node(ISubscriber<ILogicalOperator, LogicalSubscription> sub) {
		this.builder.append(' ');
		this.builder.append(((ILogicalOperator)sub).getName());
	}

	@Override
	public void ascend(ISubscriber<ILogicalOperator, LogicalSubscription> sub) {
		if (this.wasup) {
			this.builder.append(" )");	
		}
		this.wasup = true;
	}
	
	public String getResult() {
		if (this.wasup) {
			this.builder.append(" )");	
		}
		return this.builder.toString();
	}
	
	public void reset() {
		this.builder = new StringBuilder();
		this.wasup = false;
	}

}
