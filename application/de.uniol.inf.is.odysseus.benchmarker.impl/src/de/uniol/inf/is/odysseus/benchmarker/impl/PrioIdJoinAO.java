package de.uniol.inf.is.odysseus.benchmarker.impl;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.LogicalSubscription;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class PrioIdJoinAO extends AbstractLogicalOperator implements
		ILogicalOperator {
	private static final long serialVersionUID = 7562422981222202288L;

	private int leftPos = -1;
	private int rightPos = -1;

	public PrioIdJoinAO() {
	}

	public PrioIdJoinAO(PrioIdJoinAO prioIdJoinAO) {
		this.leftPos = prioIdJoinAO.leftPos;
		this.rightPos = prioIdJoinAO.rightPos;
	}

	public void setLeftPos(int leftPos) {
		this.leftPos = leftPos;
	}

	public void setRightPos(int rightPos) {
		this.rightPos = rightPos;
	}

	public int getLeftPos() {
		return leftPos;
	}

	public int getRightPos() {
		return rightPos;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new PrioIdJoinAO(this);
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		SDFAttributeList outputSchema = new SDFAttributeList();
		for (LogicalSubscription l : getSubscribedToSource()) {
			outputSchema.addAttributes(l.getSchema());
		}
		return outputSchema;
	}

}
