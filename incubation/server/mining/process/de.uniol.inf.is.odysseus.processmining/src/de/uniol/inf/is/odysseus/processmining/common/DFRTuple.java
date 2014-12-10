package de.uniol.inf.is.odysseus.processmining.common;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;


public class DFRTuple<T extends IMetaAttribute>  extends AbstractLCTuple<T>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6700319117413751639L;
	private final int FOLLOW_ACTIVITY = 3;
	
	DFRTuple(){
		super(LCTupleType.DirectlyFollowRelation);
		create();
	}

	@Override
	protected void create() {
		this.attributes = new Object[4];
		this.setAttribute(FREQUENCE, 1);
		this.setRequiresDeepClone(true);
	}

	public String getFollowActivity(){
		if(this.getAttribute(FOLLOW_ACTIVITY)!= null && this.getAttribute(FOLLOW_ACTIVITY) instanceof String){
			return (String) this.getAttribute(FOLLOW_ACTIVITY);
		}
		return null;
	}
	public void setFollowActivity(String activity){
		this.setAttribute(FOLLOW_ACTIVITY, activity);
	}

	@Override
	public String getIdentifier() {
		return this.getActivity()+this.getFollowActivity();
	}
}
