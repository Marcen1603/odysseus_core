package de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.inactivequery.status;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;

public class JxtaOperatorInformation {
	private String pipeID;
	private String oldPeer;
	private IPhysicalOperator operator;
	private boolean isSender;
	public String getPipeID() {
		return pipeID;
	}
	public void setPipeID(String pipeID) {
		this.pipeID = pipeID;
	}
	public String getOldPeer() {
		return oldPeer;
	}
	public void setOldPeer(String oldPeer) {
		this.oldPeer = oldPeer;
	}
	public IPhysicalOperator getOperator() {
		return operator;
	}
	public void setOperator(IPhysicalOperator operator) {
		this.operator = operator;
	}
	public boolean isSender() {
		return isSender;
	}
	public void setSender(boolean isSender) {
		this.isSender = isSender;
	}
}
