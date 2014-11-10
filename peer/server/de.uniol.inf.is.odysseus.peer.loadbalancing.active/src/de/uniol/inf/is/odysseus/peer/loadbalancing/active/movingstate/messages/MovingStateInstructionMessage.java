package de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.messages;

import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;

/**
 * Message to which advises Peers to duplicate a particular connection.
 * 
 * @author Carsten Cordes
 * 
 */
public class MovingStateInstructionMessage implements IMessage {

	public static final int INITIATE_LOADBALANCING = 0;
	public static final int ADD_QUERY = 1;
	public static final int REPLACE_RECEIVER = 2;
	public static final int INSTALL_BUFFER_AND_REPLACE_SENDER = 3;
	public static final int INITIATE_STATE_COPY = 4;
	public static final int INJECT_STATE = 5;
	public static final int FINISHED_COPYING_STATES = 6;
	public static final int STOP_BUFFERING = 7;

	public static final int MESSAGE_RECEIVED = 8;
	public static final int PIPE_SUCCCESS_RECEIVED = 9;

	private int loadBalancingProcessId;
	private int msgType;

	private String PQLQuery;

	private String pipeId;
	private String peerId;
	private String operatorType;
	private int operatorIndex;

	/**
	 * Default constructor.
	 */
	public MovingStateInstructionMessage() {
	}

	public static MovingStateInstructionMessage createInitiateStateCopyMsg(
			int lbProcessId, String pipeID, String operatorType,
			int operatorIndex) {
		MovingStateInstructionMessage message = new MovingStateInstructionMessage();
		message.loadBalancingProcessId = lbProcessId;
		message.operatorType = operatorType;
		message.setOperatorIndex(operatorIndex);
		message.setPipeId(pipeID);
		message.msgType = INITIATE_STATE_COPY;

		return message;
	}
	
	public static MovingStateInstructionMessage createFinishedCopyingStatesMsg(int lbProcessId) {
		MovingStateInstructionMessage message = new MovingStateInstructionMessage();
		message.loadBalancingProcessId = lbProcessId;
		message.msgType = FINISHED_COPYING_STATES;
		return message;
	}
	
	public static MovingStateInstructionMessage createInitiateMsg(
			int lbProcessId) {
		MovingStateInstructionMessage message = new MovingStateInstructionMessage();
		message.loadBalancingProcessId = lbProcessId;
		message.msgType = INITIATE_LOADBALANCING;
		return message;
	}

	public static MovingStateInstructionMessage createMessageReceivedMsg(
			int lbProcessId) {
		MovingStateInstructionMessage message = new MovingStateInstructionMessage();
		message.loadBalancingProcessId = lbProcessId;
		message.msgType = MESSAGE_RECEIVED;
		return message;
	}

	public static MovingStateInstructionMessage createAddQueryMsg(
			int lbProcessId, String PQLQuery) {
		MovingStateInstructionMessage message = new MovingStateInstructionMessage();
		message.loadBalancingProcessId = lbProcessId;
		message.PQLQuery = PQLQuery;
		message.msgType = ADD_QUERY;
		return message;
	}

	public static MovingStateInstructionMessage createPipeReceivedMsg(
			int lbProcessId, String pipeID) {
		MovingStateInstructionMessage message = new MovingStateInstructionMessage();
		message.loadBalancingProcessId = lbProcessId;
		message.setPipeId(pipeID);
		message.msgType = PIPE_SUCCCESS_RECEIVED;
		return message;
	}

	public static MovingStateInstructionMessage createInstallBufferAndReplaceSenderMsg(
			int lbProcessId, String newPeerId,
			String pipeId) {
		MovingStateInstructionMessage message = new MovingStateInstructionMessage();
		message.loadBalancingProcessId = lbProcessId;
		message.pipeId = pipeId;
		message.peerId = newPeerId;
		message.msgType = INSTALL_BUFFER_AND_REPLACE_SENDER;
		return message;
	}

	public static MovingStateInstructionMessage createReplaceReceiverMsg(
			int lbProcessId, String peerId, String pipeId) {
		MovingStateInstructionMessage message = new MovingStateInstructionMessage();
		message.loadBalancingProcessId = lbProcessId;
		message.pipeId = pipeId;
		message.peerId = peerId;
		message.msgType = REPLACE_RECEIVER;
		return message;
	}

	@Override
	/**
	 * Returns message as byte array.
	 */
	public byte[] toBytes() {

		ByteBuffer bb = null;
		int bbsize;

		switch (msgType) {

		case INITIATE_LOADBALANCING:
		case MESSAGE_RECEIVED:
		case FINISHED_COPYING_STATES:
		case STOP_BUFFERING:
			/*
			 * Allocate byte Buffer: 4 Bytes for integer msgType 4 Bytes for
			 * integer loadBalancingProcessId
			 */
			bbsize = 4 + 4;
			bb = ByteBuffer.allocate(bbsize);
			bb.putInt(msgType);
			bb.putInt(loadBalancingProcessId);
			break;

		case ADD_QUERY:
			/*
			 * Allocate byte Buffer: 4 Bytes for integer msgType 4 Bytes for
			 * integer loadBalancingProcessId 4 Bytes for Size of PQL String PQL
			 * String
			 */

			byte[] pqlAsBytes = PQLQuery.getBytes();

			bbsize = 4 + 4 + 4 + pqlAsBytes.length;
			bb = ByteBuffer.allocate(bbsize);
			bb.putInt(msgType);
			bb.putInt(loadBalancingProcessId);
			bb.putInt(pqlAsBytes.length);
			bb.put(pqlAsBytes);
			break;

		case REPLACE_RECEIVER:
		case INSTALL_BUFFER_AND_REPLACE_SENDER:
			/*
			 * Allocate byte Buffer: 4 Bytes for integer msgType 4 Bytes for
			 * integer loadBalancingProcessId 4 Bytes for integer Size of
			 * oldPipeId 4 Bytes for
			 * integer Size of newPeerId oldPipeId newPeerId
			 */

			byte[] oldPipeIdBytes = pipeId.getBytes();
			byte[] newPeerIdBytes = peerId.getBytes();

			bbsize = 4 + 4 + 4 + 4 + oldPipeIdBytes.length
					 + newPeerIdBytes.length;

			bb = ByteBuffer.allocate(bbsize);
			bb.putInt(msgType);
			bb.putInt(loadBalancingProcessId);
			bb.putInt(oldPipeIdBytes.length);
			bb.putInt(newPeerIdBytes.length);

			bb.put(oldPipeIdBytes);
			bb.put(newPeerIdBytes);
			break;

		case PIPE_SUCCCESS_RECEIVED:

			/*
			 * Allocate byte Buffer: 4 Bytes for integer msgType 4 Bytes for
			 * integer loadBalancingProcessId 4 Bytes for integer Size of
			 * oldPipeId oldPipeId
			 */
			byte[] oldPipeIdAsBytes = pipeId.getBytes();

			bbsize = 4 + 4 + 4 + 4 + 4 + oldPipeIdAsBytes.length;

			bb = ByteBuffer.allocate(bbsize);
			bb.putInt(msgType);
			bb.putInt(loadBalancingProcessId);
			bb.putInt(oldPipeIdAsBytes.length);

			bb.put(oldPipeIdAsBytes);
			break;

		case INITIATE_STATE_COPY:

			byte[] pipeIdAsBytes = pipeId.getBytes();
			byte[] operatorTypeAsBytes = operatorType.getBytes();

			/*
			 * Allocate byte Buffer: 4 Bytes for integer msgType 4 Bytes for
			 * integer loadBalancingProcessId 4 Bytes for integer Size of pipeId
			 * pipeId 4 Bytes for integer Size of operatorType operatorType 4
			 * Bytes for integer operator Index.
			 */
			bbsize = 4 + 4 + 4 + 4 + 4 + operatorTypeAsBytes.length
					+ pipeIdAsBytes.length;
			bb = ByteBuffer.allocate(bbsize);
			bb.putInt(msgType);
			bb.putInt(loadBalancingProcessId);
			bb.putInt(pipeIdAsBytes.length);
			bb.put(pipeIdAsBytes);
			bb.putInt(operatorTypeAsBytes.length);
			bb.put(operatorTypeAsBytes);
			bb.putInt(getOperatorIndex());
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

		switch (msgType) {
		case ADD_QUERY:
			int sizeOfPql = bb.getInt();
			byte[] pqlAsBytes = new byte[sizeOfPql];
			bb.get(pqlAsBytes);
			this.PQLQuery = new String(pqlAsBytes);
			break;

		case REPLACE_RECEIVER:
		case INSTALL_BUFFER_AND_REPLACE_SENDER:
			int sizeOfPipeId = bb.getInt();
			int sizeOfPeerId = bb.getInt();

			byte[] pipeIdAsBytes = new byte[sizeOfPipeId];
			byte[] peerIdAsBytes = new byte[sizeOfPeerId];

			bb.get(pipeIdAsBytes);
			bb.get(peerIdAsBytes);

			this.pipeId = new String(pipeIdAsBytes);
			this.peerId = new String(peerIdAsBytes);
			
			
			break;
			
		case PIPE_SUCCCESS_RECEIVED:
			int sizeOfOldPipeId = bb.getInt();
			byte[] oldPipeIdAsBytes = new byte[sizeOfOldPipeId];
			bb.get(oldPipeIdAsBytes);
			this.pipeId = new String(oldPipeIdAsBytes);
			break;
			
		case INITIATE_STATE_COPY:

			int pipeIdLength = bb.getInt();
			byte[] pipeIdBytes = new byte[pipeIdLength];
			bb.get(pipeIdBytes);
			this.pipeId = new String(pipeIdBytes);

			int operatorTypeLength = bb.getInt();
			byte[] operatorTypeBytes = new byte[operatorTypeLength];
			bb.get(operatorTypeBytes);
			this.operatorType = new String(operatorTypeBytes);

			this.setOperatorIndex(bb.getInt());

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

	public String getPeerId() {
		return peerId;
	}

	public void setPeerId(String peerId) {
		this.peerId = peerId;
	}

	public String getPipeId() {
		return pipeId;
	}

	public void setPipeId(String pipeId) {
		this.pipeId = pipeId;
	}


	public String getOperatorType() {
		return operatorType;
	}

	public void setOperatorType(String operatorType) {
		this.operatorType = operatorType;
	}

	public int getOperatorIndex() {
		return operatorIndex;
	}

	public void setOperatorIndex(int operatorIndex) {
		this.operatorIndex = operatorIndex;
	}

	public static MovingStateInstructionMessage createStopBufferingMsg(
			int lbProcessId) {
		MovingStateInstructionMessage message = new MovingStateInstructionMessage();
		message.setLoadBalancingProcessId(lbProcessId);
		message.setMsgType(STOP_BUFFERING);
		return message;
	}

}
