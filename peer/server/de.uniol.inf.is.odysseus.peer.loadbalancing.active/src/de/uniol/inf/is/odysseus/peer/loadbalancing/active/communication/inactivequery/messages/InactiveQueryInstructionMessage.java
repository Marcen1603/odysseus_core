package de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.inactivequery.messages;

import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;

/**
 * Messages sent from Master to slave Peer to instruct slave Peer.
 * 
 * @author Carsten Cordes
 * 
 */
public class InactiveQueryInstructionMessage implements IMessage {

	/***
	 * Constants used to indicate MessageType.
	 */
	public static final int INITIATE_LOADBALANCING = 0;
	public static final int ADD_QUERY = 1;
	public static final int REPLACE_RECEIVER = 2;
	public static final int REPLACE_SENDER = 3;
	public static final int MESSAGE_RECEIVED = 4;
	public static final int PIPE_SUCCCESS_RECEIVED = 5;

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
	 * Default constructor.
	 */
	public InactiveQueryInstructionMessage() {
	}

	/***
	 * creates Initiate LoadBalancing Message
	 * 
	 * @param lbProcessId
	 *            Loadbalancing Process Id
	 * @return Message with msgType INITIATE_LOADBALANCING
	 */
	public static InactiveQueryInstructionMessage createInitiateMsg(
			int lbProcessId) {
		InactiveQueryInstructionMessage message = new InactiveQueryInstructionMessage();
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
	public static InactiveQueryInstructionMessage createMessageReceivedMsg(
			int lbProcessId) {
		InactiveQueryInstructionMessage message = new InactiveQueryInstructionMessage();
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
	public static InactiveQueryInstructionMessage createAddQueryMsg(
			int lbProcessId, String PQLQuery) {
		InactiveQueryInstructionMessage message = new InactiveQueryInstructionMessage();
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
	public static InactiveQueryInstructionMessage createPipeReceivedMsg(
			int lbProcessId, String pipeID) {
		InactiveQueryInstructionMessage message = new InactiveQueryInstructionMessage();
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
	public static InactiveQueryInstructionMessage createInstallBufferAndReplaceSenderMsg(
			int lbProcessId, String newPeerId, String pipeId) {
		InactiveQueryInstructionMessage message = new InactiveQueryInstructionMessage();
		message.loadBalancingProcessId = lbProcessId;
		message.pipeId = pipeId;
		message.peerId = newPeerId;
		message.msgType = REPLACE_SENDER;
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
	public static InactiveQueryInstructionMessage createReplaceReceiverMsg(
			int lbProcessId, String peerId, String pipeId) {
		InactiveQueryInstructionMessage message = new InactiveQueryInstructionMessage();
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
		case REPLACE_SENDER:
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
		case REPLACE_SENDER:
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

}
