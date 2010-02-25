package de.uniol.inf.is.odysseus.physicaloperator.base;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.util.INodeVisitor;

/**
 * Builds an string representation of a physical plan.
 * 
 * @author Tobias Witt
 *
 */
public class PhysicalPlanToStringVisitor implements INodeVisitor<IPhysicalOperator, String> {
	
	private StringBuilder builder;
	private int depth;
	private boolean showSchema;
	
	public PhysicalPlanToStringVisitor() {
		reset();
	}
	
	public PhysicalPlanToStringVisitor(boolean showSchema) {
		reset();
		this.showSchema = showSchema;
	}

	@Override
	public void ascend(IPhysicalOperator to) {
		this.depth--;
	}

	@Override
	public void descend(IPhysicalOperator to) {
		this.depth++;
	}

	@Override
	public String getResult() {
		return this.builder.toString();
	}

	@Override
	public void node(IPhysicalOperator op) {
		for (int i=0; i<this.depth; i++) {
			this.builder.append("  ");
		}
		this.builder.append(op.getName());
		if(showSchema) {
			this.builder.append("["+op.getOutputSchema()+"]");
		}
		this.builder.append('\n');
	}
	
	public void reset() {
		this.builder = new StringBuilder();
		this.depth = 0;
	}

}
