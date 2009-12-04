package de.uniol.inf.is.odysseus.p2p.jxta;


import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Subplan;


public class SubplanJxtaImpl extends Subplan{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1577798037401864479L;

	public SubplanJxtaImpl(String id, AbstractLogicalOperator op){
		super(id, op);
	}
	
		
	String responseSocket;

	public String getResponseSocket() {
		return responseSocket;
	}

	public void setResponseSocket(String responseSocket) {
		this.responseSocket = responseSocket;
	}
	
	
	
	
}
