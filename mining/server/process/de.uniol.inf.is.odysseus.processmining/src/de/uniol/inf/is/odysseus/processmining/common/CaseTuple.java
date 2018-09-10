package de.uniol.inf.is.odysseus.processmining.common;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

public class CaseTuple<T extends IMetaAttribute>  extends AbstractLCTuple<T>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3794834480004730196L;
	private final int CASE_ID = 3;
	
	CaseTuple(){
		super(LCTupleType.Case);
		create();
	}
	@Override
	protected void create() {
		this.attributes = new Object[4];
		this.setAttribute(FREQUENCE, 1);
		this.setRequiresDeepClone(true);
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
		return this.getCaseID();
	}

}
