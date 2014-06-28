package de.uniol.inf.is.odysseus.peer.loadbalancing.active;

import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;

public class LoadBalancingAddQueryMessage implements IMessage {

	/*
	 * ID to identify current load Balancing process (is reused in different stages of negotiation between clients)
	 */
	private int loadBalancingProcessId;
	private String pqlQuery;
	
	public LoadBalancingAddQueryMessage() {
	}
	
	public LoadBalancingAddQueryMessage(int loadBalancingProcessId,String pqlQuery) {
		this.loadBalancingProcessId = loadBalancingProcessId;
		this.pqlQuery = pqlQuery;
	}


	@Override
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
	public void fromBytes(byte[] data) {
		ByteBuffer bb = ByteBuffer.wrap(data);
		loadBalancingProcessId = bb.getInt();
		int sizeOfPqlString = bb.getInt();
		byte[] pqlStringAsBytes = new byte[sizeOfPqlString];
		bb.get(pqlStringAsBytes);
		this.pqlQuery = new String (pqlStringAsBytes);
	}


	public int getLoadBalancingProcessId() {
		return loadBalancingProcessId;
	}


	public void setLoadBalancingProcessId(int loadBalancingProcessId) {
		this.loadBalancingProcessId = loadBalancingProcessId;
	}
	
	public String getPqlQuery() {
		return this.pqlQuery;
	}
	
	public void setPqlQuery(String pqlQuery) {
		this.pqlQuery = pqlQuery;
	}
	

}
