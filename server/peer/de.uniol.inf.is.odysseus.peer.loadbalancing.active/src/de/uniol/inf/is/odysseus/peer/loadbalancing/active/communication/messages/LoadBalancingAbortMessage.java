package de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.messages;

import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;

/**
 * Message to abort LaodBalancing (Sent from inititating Peer)
 * @author Carsten Cordes
 *
 */
public class LoadBalancingAbortMessage implements IMessage {
	
	public static final int ABORT_INSTRUCTION = 0;
	public static final int ABORT_RESPONSE = 1;

	/*
	 * ID to identify current load Balancing process (is reused in different stages of negotiation between clients)
	 */
	private int loadBalancingProcessId;
	private int msgType;
	
	
	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	public static LoadBalancingAbortMessage createAbortInstructionMsg(int lbProcessId){
		
		LoadBalancingAbortMessage message = new LoadBalancingAbortMessage();
		
		message.setMsgType(ABORT_INSTRUCTION);
		message.setLoadBalancingProcessId(lbProcessId);
		return message;
	}
	
public static LoadBalancingAbortMessage createAbortResponseMsg(int lbProcessId){
		
		LoadBalancingAbortMessage message = new LoadBalancingAbortMessage();
		
		message.setMsgType(ABORT_RESPONSE);
		message.setLoadBalancingProcessId(lbProcessId);
		return message;
	}
	
	/**
	 * Default Constructor
	 */
	public LoadBalancingAbortMessage() {
	}
	

	@Override
	/**
	 * Returns message as bytes.
	 */
	public byte[] toBytes() {
		
		ByteBuffer bb=null;
		int bbsize;
			
				/*
				 * Allocate byte Buffer:
				 * 	4 Bytes for msgType
				 *  4 Bytes for process id.
				 */

				bbsize = 8;
				
				bb = ByteBuffer.allocate(bbsize);
				bb.putInt(msgType);
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
		msgType = bb.getInt();
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
