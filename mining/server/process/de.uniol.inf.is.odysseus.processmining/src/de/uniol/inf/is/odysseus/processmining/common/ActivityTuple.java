package de.uniol.inf.is.odysseus.processmining.common;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;


public class ActivityTuple<T extends IMetaAttribute>  extends AbstractLCTuple<T>{

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
