package de.uniol.inf.is.odysseus.peer.loadbalancing.active.messages;

import java.nio.ByteBuffer;


import de.uniol.inf.is.odysseus.p2p_new.IMessage;

/**
 * Message containing a QueryPart which should be installed as a new Query.
 * @author Carsten Cordes
 *
 */
public class LoadBalancingAddQueryMessage implements IMessage {

	/**
	 * ID to identify current load Balancing process (is reused in different stages of negotiation between clients)
	 */
	private int loadBalancingProcessId;
	
	/**
	 * PQL Query to install.
	 */
	private String pqlQuery;
	
	/**
	 * Constructor
	 */
	public LoadBalancingAddQueryMessage() {
	}
	
	
	/**
	 * Constructor
	 * @param loadBalancingProcessId Load Balancing process iD.
	 * @param pqlQuery Query to install.
	 */
	public LoadBalancingAddQueryMessage(int loadBalancingProcessId,String pqlQuery) {
		this.loadBalancingProcessId = loadBalancingProcessId;
		this.pqlQuery = pqlQuery;
	}


	@Override
	/**
	 * Returns message as byte Array.
	 */
	public byte[] toBytes() {
		/*
		 * Allocate byte Buffer:
		 * 	4 Bytes for integer loadBalancingProcessId
		 *  4 Bytes for integer Size of PQL String
		 *  (Size Of PQL Bytes) for PQL String
		 */
		
		byte[] pqlStatementBytes = pqlQuery.getBytes();
		
		int bbsize = 4 + 4 + pqlStatementBytes.length;
		
		ByteBuffer bb = ByteBuffer.allocate(bbsize);
		bb.putInt(loadBalancingProcessId);
		bb.putInt(pqlStatementBytes.length);
		bb.put(pqlStatementBytes);
		bb.flip();
		return bb.array();
	}

	@Override
	/**
	 * Parses message from Byte Array.
	 */
	public void fromBytes(byte[] data) {
		ByteBuffer bb = ByteBuffer.wrap(data);
		loadBalancingProcessId = bb.getInt();
		int sizeOfPqlString = bb.getInt();
		byte[] pqlStringAsBytes = new byte[sizeOfPqlString];
		bb.get(pqlStringAsBytes);
		this.pqlQuery = new String (pqlStringAsBytes);
	}


	/**
	 * Get LoadBalancing Process id.
	 * @return LoadBalancing Process id
	 */
	public int getLoadBalancingProcessId() {
		return loadBalancingProcessId;
	}


	/**
	 * Sets load balancing process id.
	 * @param loadBalancingProcessId LoadBalancingProcess Id to set.
	 */
	public void setLoadBalancingProcessId(int loadBalancingProcessId) {
		this.loadBalancingProcessId = loadBalancingProcessId;
	}
	
	/**
	 * Returns PQL Query.
	 * @return PQL as String
	 */
	public String getPqlQuery() {
		return this.pqlQuery;
	}
	
	/**
	 * Sets PQL Query 
	 * @param pqlQuery PQL Query to set.
	 */
	public void setPqlQuery(String pqlQuery) {
		this.pqlQuery = pqlQuery;
	}
	

}
