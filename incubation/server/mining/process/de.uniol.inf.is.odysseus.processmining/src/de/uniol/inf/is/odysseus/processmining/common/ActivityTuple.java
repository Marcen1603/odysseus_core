package de.uniol.inf.is.odysseus.processmining.common;


public class ActivityTuple extends AbstractLCTuple{
	
	ActivityTuple(){
		super(LCTupleType.Activity);
		create();
	}
	@Override
	protected void create() {
		this.attributes = new Object[3];
		this.setAttribute(FREQUENCE, 1);
		this.setRequiresDeepClone(true);
	}

	
	@Override
	public String getIdentifier() {
		return this.getActivity();
	}

}
