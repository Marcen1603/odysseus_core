package de.uniol.inf.is.odysseus.peer.loadbalancing.active.messages;

import java.nio.ByteBuffer;
import java.util.Collection;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;

/**
 * Message to show installing the new QueryPart was Successful (Sent TO initiating Peer)
 * @author Carsten Cordes
 *
 */
public class LoadBalancingResponseMessage implements IMessage {
	
	public static final int ACK_LOADBALANCING = 0;
	
	public static final int SUCCESS_INSTALL_QUERY = 1;
	public static final int FAILURE_INSTALL_QUERY = 2;
	
	public static final int SUCCESS_DUPLICATE = 3;
	public static final int FAILURE_DUPLICATE_RECEIVER = 4;
	
	
	
	
	/*
	 * ID to identify current load Balancing process (is reused in different stages of negotiation between clients)
	 */
	private int loadBalancingProcessId;
	
	private int msgType;
	
	private String pipeID;
	private Integer[] installedQueries;
	

	/**
	 * Default Constructor
	 */
	public LoadBalancingResponseMessage() {
	}
	
	
	public LoadBalancingResponseMessage(int loadBalancingProcessId, String pipeID) {
		this.loadBalancingProcessId = loadBalancingProcessId;
		this.pipeID = pipeID;
		msgType = SUCCESS_DUPLICATE;
	}
	
	public LoadBalancingResponseMessage(int loadBalancingProcessId, Collection<Integer> installedQueries) {
		this.loadBalancingProcessId = loadBalancingProcessId;
		this.installedQueries = installedQueries.toArray(new Integer[installedQueries.size()]);
		msgType = SUCCESS_DUPLICATE;
	}
	
	public LoadBalancingResponseMessage(int loadBalancingProcessId, int msgType) {
		this.msgType = msgType;
		this.loadBalancingProcessId = loadBalancingProcessId;
	}


	@Override
	/**
	 * Returns message as bytes.
	 */
	public byte[] toBytes() {
		
		ByteBuffer bb = null;
		int bbsize;
		
		switch(msgType) {
			case ACK_LOADBALANCING:
			case FAILURE_INSTALL_QUERY:
			case FAILURE_DUPLICATE_RECEIVER:
				
				/*
				 * Allocate byte Buffer:
				 *  4 Bytes for integer msgType
				 * 	4 Bytes for integer loadBalancingProcessId
				 */
				bbsize = 8;
				bb = ByteBuffer.allocate(bbsize);
				bb.putInt(msgType);
				bb.putInt(loadBalancingProcessId);
				
				break;
			case SUCCESS_INSTALL_QUERY:
				
				/*
				 * Allocate byte Buffer:
				 *  4 Bytes for integer msgType
				 * 	4 Bytes for integer loadBalancingProcessId
				 *  4 Bytes for size of Integer Collection
				 *  sizeOfIntegerCollection bytes for IntegerCollection
				 */
				
				int sizeOfIntegerCollection = (installedQueries.length * 4);
				
				bbsize = 4 + 4 + 4 + sizeOfIntegerCollection;
				
				bb = ByteBuffer.allocate(bbsize);
				
				bb.putInt(msgType);
				bb.putInt(loadBalancingProcessId);
				bb.putInt(installedQueries.length);
				for(int query : installedQueries) {
					bb.putInt(query);
				}
				
				break;
			case SUCCESS_DUPLICATE:
				
				/*
				 * Allocate byte Buffer:
				 *  4 Bytes for msgType
				 * 	4 Bytes for integer loadBalancingProcessId
				 *  4 Bytes for integer pipeIdSize
				 *  payloadSize bytes for PipeId
				 */
				
				byte[] pipeIdAsBytes = pipeID.getBytes();
				
				bbsize = 4 + 4 + 4 + pipeIdAsBytes.length;
				bb = ByteBuffer.allocate(bbsize);
				
				bb.putInt(msgType);
				bb.putInt(loadBalancingProcessId);
				bb.putInt(pipeIdAsBytes.length);
				bb.put(pipeIdAsBytes);
				
				break;
		
		}
		
		
		bb.flip();
		return bb.array();
	}

	@Override
	/**
	 * Parses message from bytes.
	 */
	public void fromBytes(byte[] data) {
		ByteBuffer bb = ByteBuffer.wrap(data);
		msgType = bb.getInt();
		loadBalancingProcessId = bb.getInt();
		
		switch(msgType) {
		
		case SUCCESS_INSTALL_QUERY:
			
			/*
			 * Allocate byte Buffer:
			 *  4 Bytes for integer msgType
			 * 	4 Bytes for integer loadBalancingProcessId
			 *  4 Bytes for size of Integer Collection
			 *  sizeOfIntegerCollection bytes for IntegerCollection
			 */
			
			int lengthOfIntArray = bb.getInt();
			this.installedQueries = new Integer[lengthOfIntArray];
			for(int i=0;i<lengthOfIntArray;i++) {
				installedQueries[i] = bb.getInt();
			}
			break;
			
		case SUCCESS_DUPLICATE:
			
			int pipeIdSize = bb.getInt();
			byte[] pipeIdBytes = new byte[pipeIdSize];
			bb.get(pipeIdBytes);
			this.pipeID = new String (pipeIdBytes);
		
		}
		
		
	}


	public int getMsgType() {
		return msgType;
	}


	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}


	public String getPipeID() {
		return pipeID;
	}


	public void setPipeID(String pipeID) {
		this.pipeID = pipeID;
	}


	public Integer[] getInstalledQueries() {
		return installedQueries;
	}


	public void setInstalledQueries(Integer[] installedQueries) {
		this.installedQueries = installedQueries;
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
