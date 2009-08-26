package de.uniol.inf.is.odysseus.logicaloperator.base;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.util.INodeVisitor;

/**
 * Builds an string representation of a logical algebra tree 
 * @author Jonas Jacobi
 */
public class AlgebraPlanToStringVisitor implements INodeVisitor<ILogicalOperator, String> {

	private StringBuilder builder;
	private boolean wasup;

	public AlgebraPlanToStringVisitor() {
		reset();
	}
	
	@Override
	public void descend(ILogicalOperator op) {
		if (this.wasup) {
			this.builder.append(',');
		} else {
			this.builder.append('(');
		}
		this.wasup = false;
	}

	@Override
	public void node(ILogicalOperator op) {
		this.builder.append(' ');
		this.builder.append(op.getPOName());
	}

	@Override
	public void ascend(ILogicalOperator op) {
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
