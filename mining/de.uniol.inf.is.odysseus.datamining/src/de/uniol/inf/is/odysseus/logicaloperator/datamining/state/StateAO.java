package de.uniol.inf.is.odysseus.logicaloperator.datamining.state;

import de.uniol.inf.is.odysseus.logicaloperator.BinaryLogicalOp;
import de.uniol.inf.is.odysseus.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

@LogicalOperator(name = "STATE", minInputPorts = 2, maxInputPorts = 2)
public class StateAO extends BinaryLogicalOp {

	private static final long serialVersionUID = -6413869628754774723L;
	private boolean open = true;
	private int openAt = 10000;

	public StateAO(boolean open, int openAt) {
		super();
		this.open = open;
		this.openAt = openAt;
	}

	public StateAO(StateAO stateAO) {
		super(stateAO);
		this.open = stateAO.open;
		this.openAt = stateAO.openAt;
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return getInputSchema(0);
	}

	@Override
	public StateAO clone() {
		return new StateAO(this);
	}

	public boolean isOpen() {
		return open;
	}

	@Parameter(type = BooleanParameter.class, optional = true)
	public void setOpen(boolean open) {
		this.open = open;
	}

	@Parameter(type = IntegerParameter.class, optional = true)
	public void setOpenAt(int openAt) {
		this.openAt = openAt;
	}

	public int getOpenAt() {
		return this.openAt;
	}

}
