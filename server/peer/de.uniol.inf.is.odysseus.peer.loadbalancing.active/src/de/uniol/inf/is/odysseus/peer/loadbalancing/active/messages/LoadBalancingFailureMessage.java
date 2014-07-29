package de.uniol.inf.is.odysseus.peer.loadbalancing.active.messages;

import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;

/**
 * Message to show a failure happened during LaodBalancing (Sent TO initiating Peer)
 * @author Carsten Cordes
 *
 */
public class LoadBalancingFailureMessage implements IMessage {

	/*
	 * ID to identify current load Balancing process (is reused in different stages of negotiation between clients)
	 */
	private int loadBalancingProcessId;
	
	/**
	 * Default Constructor
	 */
	public LoadBalancingFailureMessage() {
	}
	
	/**
	 * Constructor
	 * @param loadBalancingProcessId loadBalancingProcessId
	 */
	public LoadBalancingFailureMessage(int loadBalancingProcessId) {
		this.loadBalancingProcessId = loadBalancingProcessId;
	}


	@Override
	/**
	 * Returns message as bytes.
	 */
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
	/**
	 * Parses message from bytes.
	 */
	public void fromBytes(byte[] data) {
		ByteBuffer bb = ByteBuffer.wrap(data);
		loadBalancingProcessId = bb.getInt();

	}


	/**
	 * Returns LoadBalancing Process Id.
	 * @return
	 */
	public int getLoadBalancingProcessId() {
		return loadBalancingProcessId;
	}

	/**
	 * Sets load Balancing Process Id.
	 * @param loadBalancingProcessId
	 */
	public void setLoadBalancingProcessId(int loadBalancingProcessId) {
		this.loadBalancingProcessId = loadBalancingProcessId;
	}
	
	

}
