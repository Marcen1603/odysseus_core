package de.uniol.inf.is.odysseus.datamining.state.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.BinaryLogicalOp;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class StateAO extends BinaryLogicalOp{
	
	private static final long serialVersionUID = -6413869628754774723L;
	private boolean open = true;
	private int openAt;
		
	
	public StateAO(boolean open, int openAt){
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

	public void setOpen(boolean open) {
		this.open = open;
	}

	public void setOpenAt(int openAt){
		this.openAt  = openAt;
	}
	
	public int getOpenAt() {
		return this.openAt;
	}	

}
