package de.uniol.inf.is.odysseus.processmining.common;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

public class DirectlyFollowLoopTuple<T extends IMetaAttribute>  extends AbstractLCTuple<T>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 498780440247191676L;
	private final int FOLLOW_ACTIVITY = 3;
	private final int CASE_ID = 4;
	
	DirectlyFollowLoopTuple(){
		super(LCTupleType.DirectlyFollowRelationLoop);
		create();
	}

	@Override
	protected void create() {
		this.attributes = new Object[5];
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

	public String getCaseID(){
		if(this.getAttribute(CASE_ID)!= null && this.getAttribute(CASE_ID) instanceof String){
			return (String) this.getAttribute(CASE_ID);
		}
		return null;
	}
	public void setCaseID(String caseId){
		this.setAttribute(CASE_ID, caseId);
	}
	
	@Override
	public String getIdentifier() {
		return this.getFollowActivity();
	}

}
