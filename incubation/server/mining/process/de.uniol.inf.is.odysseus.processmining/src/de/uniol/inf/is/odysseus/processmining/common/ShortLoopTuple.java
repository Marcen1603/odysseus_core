package de.uniol.inf.is.odysseus.processmining.common;

public class ShortLoopTuple extends AbstractLCTuple{

	private final int FOLLOW_ACTIVITY = 3;	
	
	ShortLoopTuple(){
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
