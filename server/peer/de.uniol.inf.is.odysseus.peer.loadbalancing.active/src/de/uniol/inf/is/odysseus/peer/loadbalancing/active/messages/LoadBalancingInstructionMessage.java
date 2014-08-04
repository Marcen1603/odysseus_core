package de.uniol.inf.is.odysseus.peer.loadbalancing.active.messages;

import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;

/**
 * Message to which advises Peers to duplicate a particular connection.
 * @author Carsten Cordes
 *
 */
public class LoadBalancingInstructionMessage implements IMessage {

	public static final int INITIATE_LOADBALANCING = 0;
	public static final int ADD_QUERY = 1;
	public static final int COPY_RECEIVER = 2;
	public static final int COPY_SENDER = 3;
	public static final int DELETE_SENDER = 4;
	public static final int DELETE_RECEIVER = 5;
	
	private int loadBalancingProcessId;
	private int msgType;
	
	private String PQLQuery;
	
	private String newPeerId;
	private String oldPipeId;
	private String newPipeId;
	
	
	/**
	 * Default constructor.
	 */
	public LoadBalancingInstructionMessage() {
	}
	
	public static LoadBalancingInstructionMessage createInitiateMsg(int lbProcessId) {
		LoadBalancingInstructionMessage message = new LoadBalancingInstructionMessage();
		message.loadBalancingProcessId = lbProcessId;
		message.msgType = INITIATE_LOADBALANCING;
		return message;
	}
	
	
	public static LoadBalancingInstructionMessage createAddQueryMsg(int lbProcessId,String PQLQuery) {
		LoadBalancingInstructionMessage message = new LoadBalancingInstructionMessage();
		message.loadBalancingProcessId = lbProcessId;
		message.PQLQuery = PQLQuery;
		message.msgType = ADD_QUERY;
		return message;
	}
	
	public static LoadBalancingInstructionMessage createCopyOperatorMsg(boolean isSender, String newPeerId, String oldPipeId, String newPipeId) {
		LoadBalancingInstructionMessage message = new LoadBalancingInstructionMessage();
		message.oldPipeId = oldPipeId;
		message.newPipeId = newPipeId;
		message.newPeerId = newPeerId;
		if(isSender) {
			message.msgType = COPY_SENDER;
		}else {
			message.msgType = COPY_RECEIVER;
		}
		return message;
	}

	public static LoadBalancingInstructionMessage createDeleteOperatorMsg(boolean isSender, int lbProcessId, String pipeId) {
		LoadBalancingInstructionMessage message = new LoadBalancingInstructionMessage();
		message.oldPipeId = pipeId;
		if(isSender) {
			message.msgType = DELETE_SENDER;
		}else {
			message.msgType = DELETE_RECEIVER;
		}
		return message;
	}
	
	@Override
	/**
	 * Returns message as byte array.
	 */
	public byte[] toBytes() {
		
		ByteBuffer bb = null;
		int bbsize;
		
		switch(msgType) {
		
		case INITIATE_LOADBALANCING:
			/*
			 * Allocate byte Buffer:
			 * 	4 Bytes for integer msgType
			 *  4 Bytes for integer loadBalancingProcessId
			 */
			bbsize = 4+4;
			bb = ByteBuffer.allocate(bbsize);
			bb.putInt(msgType);
			bb.putInt(loadBalancingProcessId);
			break;
			
		case ADD_QUERY:
			/*
			 * Allocate byte Buffer:
			 * 	4 Bytes for integer msgType
			 *  4 Bytes for integer loadBalancingProcessId
			 *  4 Bytes for Size of PQL String
			 *  PQL String
			 */
			
			byte[] pqlAsBytes = PQLQuery.getBytes();
			
			bbsize = 4+4+4+pqlAsBytes.length;
			bb = ByteBuffer.allocate(bbsize);
			bb.putInt(msgType);
			bb.putInt(loadBalancingProcessId);
			bb.putInt(pqlAsBytes.length);
			bb.put(pqlAsBytes);
			break;
			
		case COPY_RECEIVER:
		case COPY_SENDER:
			/*
			 * Allocate byte Buffer:
			 *  4 Bytes for integer msgType
			 * 	4 Bytes for integer loadBalancingProcessId
			 *  4 Bytes for integer Size of oldPipeId
			 *  4 Bytes for integer Size of newPipeId
			 *  4 Bytes for integer Size of newPeerId
			 *  oldPipeId
			 *  newPipeId
			 *  newPeerId
			 */
			
			byte[] oldPipeIdBytes = oldPipeId.getBytes();
			byte[] newPipeIdBytes = newPipeId.getBytes();
			byte[] newPeerIdBytes = newPeerId.getBytes();
			
			
			bbsize = 4+ 4 + 4 + 4 + 4 + oldPipeIdBytes.length + newPipeIdBytes.length + newPeerIdBytes.length;
			
			bb = ByteBuffer.allocate(bbsize);
			bb.putInt(msgType);
			bb.putInt(loadBalancingProcessId);
			bb.putInt(oldPipeIdBytes.length);
			bb.putInt(newPipeIdBytes.length);
			bb.putInt(newPeerIdBytes.length);
			
			bb.put(oldPipeIdBytes);
			bb.put(newPipeIdBytes);
			bb.put(newPeerIdBytes);
			break;
			
		case DELETE_RECEIVER:
		case DELETE_SENDER:
			
			/*
			 * Allocate byte Buffer:
			 *  4 Bytes for integer msgType
			 * 	4 Bytes for integer loadBalancingProcessId
			 *  4 Bytes for integer Size of oldPipeId
			 *  oldPipeId
			 */
			byte[] oldPipeIdAsBytes = oldPipeId.getBytes();
			
			
			bbsize = 4+ 4 + 4 + 4 + 4 + oldPipeIdAsBytes.length;
			
			bb = ByteBuffer.allocate(bbsize);
			bb.putInt(msgType);
			bb.putInt(loadBalancingProcessId);
			bb.putInt(oldPipeIdAsBytes.length);
			
			bb.put(oldPipeIdAsBytes);
			break;
			
		
		}
		
		bb.flip();
		return bb.array();
	}

	@Override
	/**
	 * Parses message from byte array.
	 */
	public void fromBytes(byte[] data) {
		ByteBuffer bb = ByteBuffer.wrap(data);
		msgType = bb.getInt();
		loadBalancingProcessId = bb.getInt();
		
		switch(msgType) {
		case ADD_QUERY:
			int sizeOfPql = bb.getInt();
			byte[] pqlAsBytes = new byte[sizeOfPql];
			bb.get(pqlAsBytes);
			this.PQLQuery = new String(pqlAsBytes);
			break;
			
		case COPY_RECEIVER:
		case COPY_SENDER:
			int sizeOfOldPipeId = bb.getInt();
			int sizeOfNewPipeId = bb.getInt();
			int sizeOfNewPeerId = bb.getInt();
			
			byte[] oldPipeIdAsBytes = new byte[sizeOfOldPipeId];
			byte[] newPipeIdAsBytes = new byte[sizeOfNewPipeId];
			byte[] newPeerIdAsBytes = new byte[sizeOfNewPeerId];
			
			bb.get(oldPipeIdAsBytes);
			bb.get(newPipeIdAsBytes);
			bb.get(newPeerIdAsBytes);
			
			this.oldPipeId = new String(oldPipeIdAsBytes);
			this.newPipeId = new String(newPipeIdAsBytes);
			this.newPeerId = new String(newPeerIdAsBytes);
			break;
		case DELETE_RECEIVER:
		case DELETE_SENDER:
			int sizeOfPipeId = bb.getInt();
			byte[] pipeIdAsBytes = new byte[sizeOfPipeId];
			bb.get(pipeIdAsBytes);
			this.oldPipeId = new String(pipeIdAsBytes);
			break;
		}
		
		
		
	}

	public int getLoadBalancingProcessId() {
		return loadBalancingProcessId;
	}

	public void setLoadBalancingProcessId(int loadBalancingProcessId) {
		this.loadBalancingProcessId = loadBalancingProcessId;
	}

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	public String getPQLQuery() {
		return PQLQuery;
	}

	public void setPQLQuery(String pQLQuery) {
		PQLQuery = pQLQuery;
	}

	public String getNewPeerId() {
		return newPeerId;
	}

	public void setNewPeerId(String newPeerId) {
		this.newPeerId = newPeerId;
	}

	public String getOldPipeId() {
		return oldPipeId;
	}

	public void setOldPipeId(String oldPipeId) {
		this.oldPipeId = oldPipeId;
	}

	public String getNewPipeId() {
		return newPipeId;
	}

	public void setNewPipeId(String newPipeId) {
		this.newPipeId = newPipeId;
	}

}
