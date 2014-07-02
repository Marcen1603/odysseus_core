package de.uniol.inf.is.odysseus.peer.loadbalancing.active;

import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;

/**
 * Message to initiate LaodBalancing
 * @author Carsten Cordes
 *
 */
public class LoadBalancingInitiateMessage implements IMessage {

	/*
	 * ID to identify current load Balancing process (is reused in different stages of negotiation between clients)
	 */
	private int loadBalancingProcessId;
	
	public LoadBalancingInitiateMessage() {
	}
	
	public LoadBalancingInitiateMessage(int loadBalancingProcessId) {
		this.loadBalancingProcessId = loadBalancingProcessId;
	}


	@Override
	public byte[] toBytes() {
		/*
		 * Allocate byte Buffer:
		 * 	4 Bytes for integer loadBalancingProcessId
		 */
		int bbsize = 4;
		
		ByteBuffer bb = ByteBuffer.allocate(bbsize);
		bb.putInt(loadBalancingProcessId);
		bb.flip();
		return bb.array();
	}

	@Override
	public void fromBytes(byte[] data) {
		ByteBuffer bb = ByteBuffer.wrap(data);
		loadBalancingProcessId = bb.getInt();

	}


	public int getLoadBalancingProcessId() {
		return loadBalancingProcessId;
	}


	public void setLoadBalancingProcessId(int loadBalancingProcessId) {
		this.loadBalancingProcessId = loadBalancingProcessId;
	}
	
	

}
