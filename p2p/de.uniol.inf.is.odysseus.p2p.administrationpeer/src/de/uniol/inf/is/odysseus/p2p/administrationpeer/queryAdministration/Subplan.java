package de.uniol.inf.is.odysseus.p2p.administrationpeer.queryAdministration;

import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;

public class Subplan {
	
	//OPEN = Subplan wurde noch keinem Peer zugeteilt.
	//CLOSED = Subplan wurde breits einem Peer zugeteilt.
	public enum SubplanStatus{
		OPEN, CLOSED
	}
	
	String id;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	String peerId="";
	
	private SubplanStatus status;
	
	public String getPeerId() {
		return peerId;
	}

	public void setPeerId(String peerId) {
		this.peerId = peerId;
	}
	
	
	public SubplanStatus getStatus() {
		return status;
	}

	public void setStatus(SubplanStatus status) {
		this.status = status;
	}
	
	private AbstractLogicalOperator ao;

	public AbstractLogicalOperator getAo() {
		return ao;
	}

	public void setAo(AbstractLogicalOperator ao) {
		this.ao = ao;
	}

	public Subplan(String id, AbstractLogicalOperator ao) {
		super();
		this.ao = ao;
		this.status = SubplanStatus.OPEN;
		this.id=id;
	}

}
