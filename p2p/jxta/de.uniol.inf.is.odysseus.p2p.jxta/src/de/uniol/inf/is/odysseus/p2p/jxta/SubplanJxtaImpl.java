package de.uniol.inf.is.odysseus.p2p.jxta;


import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Subplan;


public class SubplanJxtaImpl extends Subplan{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1577798037401864479L;

	public SubplanJxtaImpl(String id, ILogicalOperator iLogicalOperator){
		super(id, iLogicalOperator);
	}
	
		
	String responseSocket;

	public String getResponseSocket() {
		return responseSocket;
	}

	public void setResponseSocket(String responseSocket) {
		this.responseSocket = responseSocket;
	}
	
	
	
	
}
