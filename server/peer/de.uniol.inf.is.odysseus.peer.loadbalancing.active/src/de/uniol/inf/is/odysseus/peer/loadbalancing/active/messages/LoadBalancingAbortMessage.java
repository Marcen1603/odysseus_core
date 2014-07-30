package de.uniol.inf.is.odysseus.peer.loadbalancing.active.messages;

import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;

/**
 * Message to abort LaodBalancing (Sent from inititating Peer)
 * @author Carsten Cordes
 *
 */
public class LoadBalancingAbortMessage implements IMessage {
	
	public static final int ABORT_DO_NOTHING = 0;
	public static final int ABORT_REMOVE_QUERY = 1;
	public static final int ABORT_REMOVE_DUPLICATE_CONNECTION = 2;

	/*
	 * ID to identify current load Balancing process (is reused in different stages of negotiation between clients)
	 */
	private int loadBalancingProcessId;
	private int msgType;
	
	
	private Integer[] queriesToRemove;
	private String pipeToRemove;
	
	/**
	 * Default Constructor
	 */
	public LoadBalancingAbortMessage() {
	}
	
	/**
	 * Constructor
	 * @param loadBalancingProcessId loadBalancingProcessId
	 */
	public LoadBalancingAbortMessage(int loadBalancingProcessId) {
		this.loadBalancingProcessId = loadBalancingProcessId;
		this.msgType = ABORT_DO_NOTHING;
	}
	
	
	public LoadBalancingAbortMessage(int loadBalancingProcessId, Integer[] queriesToRemove) {
		this.loadBalancingProcessId = loadBalancingProcessId;
		this.msgType = ABORT_REMOVE_QUERY;
		this.queriesToRemove = queriesToRemove;
	}
	
	public LoadBalancingAbortMessage(int loadBalancingProcessId, String pipeToRemove) {
		this.loadBalancingProcessId = loadBalancingProcessId;
		this.msgType = ABORT_REMOVE_DUPLICATE_CONNECTION;
		this.pipeToRemove = pipeToRemove;
	}


	@Override
	/**
	 * Returns message as bytes.
	 */
	public byte[] toBytes() {
		
		ByteBuffer bb=null;
		int bbsize;
		
		switch(msgType) {
			case ABORT_DO_NOTHING: 
				/*
				 * Allocate byte Buffer:
				 * 	4 Bytes for msgType
				 *  4 Bytes for process id.
				 */

				bbsize = 8;
				
				bb = ByteBuffer.allocate(bbsize);
				bb.putInt(msgType);
				bb.putInt(loadBalancingProcessId);
				break;
				
			case ABORT_REMOVE_QUERY:
				/*
				 * Allocate Byte Buffer:
				 *  4 Bytes msgType
				 *  4 bytes processID
				 *  4 bytes number of integers in queryId Array
				 *  Query ID Array
				 */
				
				bbsize = 4+4+4+(queriesToRemove.length*4);
				bb = ByteBuffer.allocate(bbsize);
				bb.putInt(msgType);
				bb.putInt(loadBalancingProcessId);
				bb.putInt(queriesToRemove.length);
				for(int query : queriesToRemove) {
					bb.putInt(query);
				}
				break;
				
			case ABORT_REMOVE_DUPLICATE_CONNECTION:
				/*
				 * Allocate Byte Buffer:
				 *  4 Bytes msgType
				 *  4 bytes processID
				 *  4 bytes number of bytes for String pipeIdToRemove
				 *  pipeIDToRemove
				 */
				
				byte[] pipeIdAsBytes = pipeToRemove.getBytes();
				
				bbsize = 4+4+4+(pipeIdAsBytes.length);
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
		case ABORT_REMOVE_DUPLICATE_CONNECTION:
			int sizeOfPipeStringAsBytes = bb.getInt();
			byte[] pipeStringBuffer = new byte[sizeOfPipeStringAsBytes];
			bb.get(pipeStringBuffer);
			this.pipeToRemove = new String(pipeStringBuffer);
			break;
		case ABORT_REMOVE_QUERY:
			
			int numberOfQueries = bb.getInt();
			this.queriesToRemove = new Integer[numberOfQueries];
			for(int i=0;i<numberOfQueries;i++) {
				queriesToRemove[i] = bb.getInt();
			}
			break;
		}
		

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
