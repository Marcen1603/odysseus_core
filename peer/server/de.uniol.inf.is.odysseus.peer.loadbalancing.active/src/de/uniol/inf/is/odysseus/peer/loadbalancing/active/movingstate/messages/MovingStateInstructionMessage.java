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
	public static final int FINISHED_COPYING_STATES = 5;
	public static final int STOP_BUFFERING = 6;

	public static final int MESSAGE_RECEIVED = 7;
	public static final int PIPE_SUCCCESS_RECEIVED = 8;

	private int loadBalancingProcessId;
	private int msgType;

	private String PQLQuery;

	private String newPeerId;
	private String oldPipeId;
	private String newPipeId;
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
		message.newPipeId = pipeID;
		message.setOperatorIndex(operatorIndex);
		message.msgType = INITIATE_STATE_COPY;

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
		message.setOldPipeId(pipeID);
		message.msgType = PIPE_SUCCCESS_RECEIVED;
		return message;
	}

	public static MovingStateInstructionMessage createInstallBufferAndReplaceSenderMsg(
			int lbProcessId, String newPeerId,
			String oldPipeId, String newPipeId) {
		MovingStateInstructionMessage message = new MovingStateInstructionMessage();
		message.loadBalancingProcessId = lbProcessId;
		message.oldPipeId = oldPipeId;
		message.newPipeId = newPipeId;
		message.newPeerId = newPeerId;
		message.msgType = INSTALL_BUFFER_AND_REPLACE_SENDER;
		return message;
	}

	public static MovingStateInstructionMessage createReplaceReceiverMsg(
			int lbProcessId, String newPeerId, String oldPipeId,
			String newPipeId) {
		MovingStateInstructionMessage message = new MovingStateInstructionMessage();
		message.loadBalancingProcessId = lbProcessId;
		message.oldPipeId = oldPipeId;
		message.newPipeId = newPipeId;
		message.newPeerId = newPeerId;
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
			 * oldPipeId 4 Bytes for integer Size of newPipeId 4 Bytes for
			 * integer Size of newPeerId oldPipeId newPipeId newPeerId
			 */

			byte[] oldPipeIdBytes = oldPipeId.getBytes();
			byte[] newPipeIdBytes = newPipeId.getBytes();
			byte[] newPeerIdBytes = newPeerId.getBytes();

			bbsize = 4 + 4 + 4 + 4 + 4 + oldPipeIdBytes.length
					+ newPipeIdBytes.length + newPeerIdBytes.length;

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

		case PIPE_SUCCCESS_RECEIVED:

			/*
			 * Allocate byte Buffer: 4 Bytes for integer msgType 4 Bytes for
			 * integer loadBalancingProcessId 4 Bytes for integer Size of
			 * oldPipeId oldPipeId
			 */
			byte[] oldPipeIdAsBytes = oldPipeId.getBytes();

			bbsize = 4 + 4 + 4 + 4 + 4 + oldPipeIdAsBytes.length;

			bb = ByteBuffer.allocate(bbsize);
			bb.putInt(msgType);
			bb.putInt(loadBalancingProcessId);
			bb.putInt(oldPipeIdAsBytes.length);

			bb.put(oldPipeIdAsBytes);
			break;

		case INITIATE_STATE_COPY:

			byte[] pipeIdAsBytes = newPipeId.getBytes();
			byte[] operatorTypeAsBytes = operatorType.getBytes();

			/*
			 * Allocate byte Buffer: 4 Bytes for integer msgType 4 Bytes for
			 * integer loadBalancingProcessId 4 Bytes for integer Size of pipeId
			 * pipeId 4 Bytes for integer Size of operatorType operatorType 4
			 * Bytes for integer operator Index.
			 */
			bbsize = 4 + 4 + 4 + 4 + operatorTypeAsBytes.length
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
		case PIPE_SUCCCESS_RECEIVED:
			int sizeOfPipeId = bb.getInt();
			byte[] pipeIdAsBytes = new byte[sizeOfPipeId];
			bb.get(pipeIdAsBytes);
			this.oldPipeId = new String(pipeIdAsBytes);
			break;
		case INITIATE_STATE_COPY:

			int newPipeIdLength = bb.getInt();
			byte[] newPipeIdBytes = new byte[newPipeIdLength];
			bb.get(newPipeIdBytes);
			this.newPipeId = new String(newPipeIdBytes);

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

}
