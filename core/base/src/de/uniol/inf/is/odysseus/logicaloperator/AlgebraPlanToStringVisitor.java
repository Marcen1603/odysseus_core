package de.uniol.inf.is.odysseus.logicaloperator;

import de.uniol.inf.is.odysseus.ISubscriber;
import de.uniol.inf.is.odysseus.util.INodeVisitor;

/**
 * Builds an string representation of a logical algebra tree 
 * @author Jonas Jacobi, Marco Grawunder
 */
public class AlgebraPlanToStringVisitor implements INodeVisitor<ISubscriber<ILogicalOperator, LogicalSubscription>, String> {

	private StringBuilder builder;
	private boolean wasup;
	private boolean showSchema;

	public AlgebraPlanToStringVisitor() {
		reset();
	}
	
	public AlgebraPlanToStringVisitor(boolean showSchema) {
		reset();
		this.showSchema = showSchema;
		
	}
	
	@Override
	public void descendAction(ISubscriber<ILogicalOperator, LogicalSubscription> sub) {
		if (this.wasup) {
			this.builder.append(',');
		} else {
			this.builder.append('(');
		}
		this.wasup = false;
	}

	@Override
	public void nodeAction(ISubscriber<ILogicalOperator, LogicalSubscription> sub) {
		this.builder.append(' ');
		//this.builder.append(((ILogicalOperator)sub).getName()).append(sub.hashCode());
		this.builder.append(((ILogicalOperator)sub));
		if(showSchema) {
			this.builder.append("["+((ILogicalOperator)sub).getOutputSchema()+"]");
		}
	}

	@Override
	public void ascendAction(ISubscriber<ILogicalOperator, LogicalSubscription> sub) {
		if (this.wasup) {
			this.builder.append(" )");	
		}
		this.wasup = true;
	}
	
	@Override
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
