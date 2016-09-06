package de.uniol.inf.is.odysseus.processmining.common;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

public abstract class AbstractLCTuple<T extends IMetaAttribute> extends Tuple<T>{

	private static final long serialVersionUID = 7033989005670834137L;
	protected final int ACTIVITY = 0;
	protected final int FREQUENCE = 1;
	protected final int MAXERROR = 2;
	
	private LCTupleType type = null;
	
	
	public AbstractLCTuple(LCTupleType type){
		super(1,true);
		this.type = type;
	}
	
	protected abstract void create();
	public abstract String getIdentifier();
	
	public LCTupleType getType() {
		return this.type;
	}

	public void setType(LCTupleType type) {
		this.type = type;
	}

	public String getActivity(){
		if(this.getAttribute(ACTIVITY)!= null && this.getAttribute(ACTIVITY) instanceof String){
			return (String) this.getAttribute(ACTIVITY);
		}
		return null;
	}
	
	public Integer getFrequency(){
		if(this.getAttribute(FREQUENCE)!= null && this.getAttribute(FREQUENCE) instanceof Integer){
			return (Integer) this.getAttribute(FREQUENCE);
		}
		return null;
	}
	
	public Integer getMaxError(){
		if(this.getAttribute(MAXERROR)!= null && this.getAttribute(MAXERROR) instanceof Integer ){
			return (Integer) this.getAttribute(MAXERROR);
		}
		return null;
	}
	public void incrementFrequency(){
		int tempFrequency = this.getFrequency();
		this.setAttribute(FREQUENCE, Integer.valueOf(tempFrequency+1));
	}
	public void setMaxError(Integer maxError){
		this.setAttribute(MAXERROR, maxError);
	}
	public void setActivity(String activity){
		this.setAttribute(ACTIVITY, activity);
	}
}
