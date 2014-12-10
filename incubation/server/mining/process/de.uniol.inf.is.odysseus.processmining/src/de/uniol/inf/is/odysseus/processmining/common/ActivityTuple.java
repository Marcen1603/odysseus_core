package de.uniol.inf.is.odysseus.processmining.common;


public class ActivityTuple extends AbstractLCTuple{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7351974444983332480L;


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
