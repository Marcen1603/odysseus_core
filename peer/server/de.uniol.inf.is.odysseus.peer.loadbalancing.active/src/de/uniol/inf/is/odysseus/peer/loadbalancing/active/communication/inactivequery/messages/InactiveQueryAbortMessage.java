package de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.inactivequery.messages;

import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;

/**
 * Message to abort LaodBalancing (Sent from inititating Peer)
 * @author Carsten Cordes
 *
 */
public class InactiveQueryAbortMessage implements IMessage {
	
	/**
	 * Constants used for communication
	 */
	public static final int ABORT_INSTRUCTION = 0;
	public static final int ABORT_RESPONSE = 1;

	/**
	 * ID to identify current load Balancing process (is reused in different stages of negotiation between clients)
	 */
	private int loadBalancingProcessId;
	
	/***
	 * Stores MessageType (constants above)
	 */
	private int msgType;
	
	/***
	 * Gets message type
	 * @return Type of Message (one of constants above)
	 */
	public int getMsgType() {
		return msgType;
	}

	/**
	 * Sets message type 
	 * @param msgType Message-Type (one of constants above)
	 */
	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	/**
	 * Returns an AbortMessage
	 * @param lbProcessId LoadBalancingProcess Id
	 * @return AbortMessage with msgType ABORT_INSTRUCTION
	 */
	public static InactiveQueryAbortMessage createAbortInstructionMsg(int lbProcessId){
		
		InactiveQueryAbortMessage message = new InactiveQueryAbortMessage();
		
		message.setMsgType(ABORT_INSTRUCTION);
		message.setLoadBalancingProcessId(lbProcessId);
		return message;
	}
	
	/***
	 * Returns response to an AbortMessage
	 * @param lbProcessId LoadBalancingProcessId
	 * @return AbortMessage with MsgType ABORT_RESPONSE
	 */
    public static InactiveQueryAbortMessage createAbortResponseMsg(int lbProcessId){
		
		InactiveQueryAbortMessage message = new InactiveQueryAbortMessage();
		
		message.setMsgType(ABORT_RESPONSE);
		message.setLoadBalancingProcessId(lbProcessId);
		return message;
	}
	
	/**
	 * Default Constructor
	 */
	public InactiveQueryAbortMessage() {
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
