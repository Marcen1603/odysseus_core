package de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.messages;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;

/**
 * Messages sent from Master to slave Peer to instruct slave Peer.
 * 
 * @author Carsten Cordes
 * 
 */
public class MovingStateInstructionMessage implements IMessage {

	/***
	 * Constants used to indicate MessageType.
	 */
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

	/**
	 * PQLQuery for ADD_QUERY Message
	 */
	private String PQLQuery;

	/**
	 * Pipe ID used in different Messages
	 */
	private String pipeId;

	/**
	 * PeerID used i different Messages
	 */
	private String peerId;

	/**
	 * Operator Type used in INITIATE_STATE_COPY Message
	 */
	private String operatorType;

	/**
	 * Operator Index used in INITIATE_STATE_COPY Message
	 */
	private int operatorIndex;
	
	/**
	 * True if current Query is Master for shared Query.
	 */
	@SuppressWarnings("unused")
	private boolean isMasterForQuery;
	
	/**
	 * Needed if query Part was Master for shared Query.
	 */
	@SuppressWarnings("unused")
	private ArrayList<PeerID> otherPeers;
	

	/**
	 * Default constructor.
	 */
	public MovingStateInstructionMessage() {
	}

	/***
	 * Creates initiate state copy Message
	 * 
	 * @param lbProcessId
	 *            LoadBalancing Process ID
	 * @param pipeID
	 *            PipeID Used for transmission
	 * @param operatorType
	 *            Type of operator (for error handling)
	 * @param operatorIndex
	 *            index of Operator in List
	 * @return Message with MSGType INITIATE_STATE_COPY
	 */
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

	/***
	 * Creates Finished Copying States Message
	 * 
	 * @param lbProcessId
	 *            LoadBalancing Process ID
	 * @return Message with msgType FINISHED_COPYING_STATES
	 */
	public static MovingStateInstructionMessage createFinishedCopyingStatesMsg(
			int lbProcessId) {
		MovingStateInstructionMessage message = new MovingStateInstructionMessage();
		message.loadBalancingProcessId = lbProcessId;
		message.msgType = FINISHED_COPYING_STATES;
		return message;
	}

	/***
	 * creates Initiate LoadBalancing Message
	 * 
	 * @param lbProcessId
	 *            Loadbalancing Process Id
	 * @return Message with msgType INITIATE_LOADBALANCING
	 */
	public static MovingStateInstructionMessage createInitiateMsg(
			int lbProcessId) {
		MovingStateInstructionMessage message = new MovingStateInstructionMessage();
		message.loadBalancingProcessId = lbProcessId;
		message.msgType = INITIATE_LOADBALANCING;
		return message;
	}

	/***
	 * Creates Message Received message
	 * 
	 * @param lbProcessId
	 *            LoadBalancing Process Id
	 * @return Message with msgType MESSAGE_RECEIVED
	 */
	public static MovingStateInstructionMessage createMessageReceivedMsg(
			int lbProcessId) {
		MovingStateInstructionMessage message = new MovingStateInstructionMessage();
		message.loadBalancingProcessId = lbProcessId;
		message.msgType = MESSAGE_RECEIVED;
		return message;
	}

	/***
	 * Creates AddQuery Message
	 * 
	 * @param lbProcessId
	 *            LoadBalancingProcess Id
	 * @param PQLQuery
	 *            PQL Query to install
	 * @return Message with ADD_QUERY msgType
	 */
	public static MovingStateInstructionMessage createAddQueryMsg(
			int lbProcessId, String PQLQuery) {
		MovingStateInstructionMessage message = new MovingStateInstructionMessage();
		message.loadBalancingProcessId = lbProcessId;
		message.PQLQuery = PQLQuery;
		message.msgType = ADD_QUERY;
		return message;
	}

	/***
	 * Create Pipe Received Message
	 * 
	 * @param lbProcessId
	 *            LoadBalancingProcess Id
	 * @param pipeID
	 *            PipeID
	 * @return Message with msgType PIPE_SUCCESS_RECEIVED
	 */
	public static MovingStateInstructionMessage createPipeReceivedMsg(
			int lbProcessId, String pipeID) {
		MovingStateInstructionMessage message = new MovingStateInstructionMessage();
		message.loadBalancingProcessId = lbProcessId;
		message.setPipeId(pipeID);
		message.msgType = PIPE_SUCCCESS_RECEIVED;
		return message;
	}

	/***
	 * creates Install Buffer and Replace Sender Message
	 * 
	 * @param lbProcessId
	 *            LoadBalancingProcess Id
	 * @param newPeerId
	 *            New Peer ID
	 * @param pipeId
	 *            Pipe ID to identify Sender
	 * @return Message with msgType INSTALL_BUFFER_AND_REPLACE_SENDER
	 */
	public static MovingStateInstructionMessage createInstallBufferAndReplaceSenderMsg(
			int lbProcessId, String newPeerId, String pipeId) {
		MovingStateInstructionMessage message = new MovingStateInstructionMessage();
		message.loadBalancingProcessId = lbProcessId;
		message.pipeId = pipeId;
		message.peerId = newPeerId;
		message.msgType = INSTALL_BUFFER_AND_REPLACE_SENDER;
		return message;
	}

	/***
	 * creates Replace Receiver Message
	 * 
	 * @param lbProcessId
	 *            LoadBalancingProcess Id
	 * @param peerId
	 *            new PeerID
	 * @param pipeId
	 *            pipe ID to identify receiver
	 * @return Message with msgType REPLACE_RECEIVER
	 */
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
			 * oldPipeId 4 Bytes for integer Size of newPeerId oldPipeId
			 * newPeerId
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

	/***
	 * Gets Loadbalancing Process Id
	 * 
	 * @return Load Balancing Process Id
	 */
	public int getLoadBalancingProcessId() {
		return loadBalancingProcessId;
	}

	/**
	 * Sets loadBalancing Process Id
	 * 
	 * @param loadBalancingProcessId
	 *            Process Id.
	 */
	public void setLoadBalancingProcessId(int loadBalancingProcessId) {
		this.loadBalancingProcessId = loadBalancingProcessId;
	}

	/**
	 * Returngs Message Type
	 * 
	 * @return Message Type
	 */
	public int getMsgType() {
		return msgType;
	}

	/***
	 * Sets Message Type
	 * 
	 * @param msgType
	 *            Message Type
	 */
	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	/**
	 * Returns PQL Query
	 * 
	 * @return PQL Query
	 */
	public String getPQLQuery() {
		return PQLQuery;
	}

	/**
	 * Sets PQL Query
	 * 
	 * @param pQLQuery
	 *            PQL Query
	 */
	public void setPQLQuery(String pQLQuery) {
		PQLQuery = pQLQuery;
	}

	/**
	 * Gets Peer ID
	 * 
	 * @return peerID
	 */
	public String getPeerId() {
		return peerId;
	}

	/***
	 * Sets Peer ID
	 * 
	 * @param peerId
	 *            Peer ID
	 */
	public void setPeerId(String peerId) {
		this.peerId = peerId;
	}

	/***
	 * Gets Pipe Id
	 * 
	 * @return Pipe ID
	 */
	public String getPipeId() {
		return pipeId;
	}

	/**
	 * Sets Pipe ID
	 * 
	 * @param pipeId
	 *            Pipe Id
	 */
	public void setPipeId(String pipeId) {
		this.pipeId = pipeId;
	}

	/***
	 * Gets operator Type
	 * 
	 * @return operator Type
	 */
	public String getOperatorType() {
		return operatorType;
	}

	/***
	 * Sets operator Type
	 * 
	 * @param operatorType
	 *            Operator Type
	 */
	public void setOperatorType(String operatorType) {
		this.operatorType = operatorType;
	}

	/***
	 * Gets operator index
	 * 
	 * @return operator index
	 */
	public int getOperatorIndex() {
		return operatorIndex;
	}

	/***
	 * Sets operator index
	 * 
	 * @param operatorIndex
	 *            operator index
	 */
	public void setOperatorIndex(int operatorIndex) {
		this.operatorIndex = operatorIndex;
	}

	/***
	 * Create STOP_BUFFERING Message
	 * 
	 * @param lbProcessId
	 *            LoadBalancing Process ID
	 * @return Message with msgType STOP_BUFFERING
	 */
	public static MovingStateInstructionMessage createStopBufferingMsg(
			int lbProcessId) {
		MovingStateInstructionMessage message = new MovingStateInstructionMessage();
		message.setLoadBalancingProcessId(lbProcessId);
		message.setMsgType(STOP_BUFFERING);
		return message;
	}

}
