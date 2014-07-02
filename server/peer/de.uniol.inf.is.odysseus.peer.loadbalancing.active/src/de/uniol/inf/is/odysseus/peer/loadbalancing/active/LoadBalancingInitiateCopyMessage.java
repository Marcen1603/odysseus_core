package de.uniol.inf.is.odysseus.peer.loadbalancing.active;

import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;

/**
 * Message to initiate copying of a particular Query part. 
 * @author Carsten Cordes.
 *
 */
public class LoadBalancingInitiateCopyMessage implements IMessage {

	/*
	 * ID to identify current load Balancing process (is reused in different stages of negotiation between clients)
	 */
	private int loadBalancingProcessId;
	
	/**
	 * Default constructor
	 */
	public LoadBalancingInitiateCopyMessage() {
	}
	
	/**
	 * Constructor
	 * @param loadBalancingProcessId LoadBalancingProcessId.
	 */
	public LoadBalancingInitiateCopyMessage(int loadBalancingProcessId) {
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
	 * Parses messages from bytes.
	 */
	public void fromBytes(byte[] data) {
		ByteBuffer bb = ByteBuffer.wrap(data);
		loadBalancingProcessId = bb.getInt();

	}


	/**
	 * Gets loadBalancing process id.
	 * @return
	 */
	public int getLoadBalancingProcessId() {
		return loadBalancingProcessId;
	}


	/**
	 * Sets load Balancing process id.
	 * @param loadBalancingProcessId
	 */
	public void setLoadBalancingProcessId(int loadBalancingProcessId) {
		this.loadBalancingProcessId = loadBalancingProcessId;
	}
	
	

}
