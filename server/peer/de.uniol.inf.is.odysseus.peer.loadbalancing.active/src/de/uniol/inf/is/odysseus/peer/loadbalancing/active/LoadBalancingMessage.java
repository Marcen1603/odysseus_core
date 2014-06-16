package de.uniol.inf.is.odysseus.peer.loadbalancing.active;

import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;

public class LoadBalancingMessage implements IMessage {

	public enum messageTypes {
		LB_Advertisement, LB_Apply, LB_Ack, LB_deny, LB_Query, LB_Empty
	}

	public messageTypes messageType;
	private String senderPeer;
	public String getSenderPeer() {
		return senderPeer;
	}
	
	public LoadBalancingMessage() {
		this.messageType = messageTypes.LB_Empty;
	}



	public void setSenderPeer(String senderPeer) {
		this.senderPeer = senderPeer;
	}



	public String getLoadBalancingPeer() {
		return loadBalancingPeer;
	}



	public void setLoadBalancingPeer(String loadBalancingPeer) {
		this.loadBalancingPeer = loadBalancingPeer;
	}



	public int getQueryID() {
		return queryID;
	}



	public void setQueryID(int queryID) {
		this.queryID = queryID;
	}

	private String loadBalancingPeer;
	private int queryID;
	

	public LoadBalancingMessage(messageTypes messageType) {
		this.messageType = messageType;
	}
	
	

	@Override
	public byte[] toBytes() {
		ByteBuffer bb = ByteBuffer.allocate(10);
		bb.putInt(0);
		bb.flip();
		return bb.array();
	}

	@Override
	public void fromBytes(byte[] data) {
		this.messageType = messageTypes.LB_Empty;

	}
	
	

}
