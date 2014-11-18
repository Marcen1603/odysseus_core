package de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.messages;

import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;

/**
 * Response Messages a slave can send to a master peer.
 * 
 * @author Carsten Cordes
 *
 */
public class MovingStateResponseMessage implements IMessage {

	/**
	 * Constants defining different Message Types.
	 */
	public static final int ACK_LOADBALANCING = 0;
	public static final int SUCCESS_INSTALL_QUERY = 1;
	public static final int FAILURE_INSTALL_QUERY = 2;
	public static final int SUCCESS_DUPLICATE = 3;
	public static final int FAILURE_DUPLICATE_RECEIVER = 4;
	public static final int ACK_INIT_STATE_COPY = 5;
	public static final int STATE_COPY_FINISHED = 6;
	public static final int ACK_ALL_STATE_COPIES_FINISHED = 7;
	public static final int STOP_BUFFERING_FINISHED = 8;

	/**
	 * ID to identify current load Balancing process (is reused in different
	 * stages of negotiation between clients)
	 */
	private int loadBalancingProcessId;

	/***
	 * Message Type definining Type of Message.
	 */
	private int msgType;

	/**
	 * Pipe id
	 */
	private String pipeID;

	/***
	 * Array of installed Query IDs
	 */
	private Integer[] installedQueries;

	/**
	 * Default Constructor
	 */
	public MovingStateResponseMessage() {
	}

	/***
	 * Creates Ack Loadbalancin Message
	 * 
	 * @param loadBalancingProcessId
	 *            LoadBalancing Process Id
	 * @return Message with msgType ACK_LOADBALANCING
	 */
	public static MovingStateResponseMessage createAckLoadbalancingMessage(
			int loadBalancingProcessId) {
		MovingStateResponseMessage message = new MovingStateResponseMessage();
		message.setLoadBalancingProcessId(loadBalancingProcessId);
		message.setMsgType(ACK_LOADBALANCING);
		return message;
	}

	/***
	 * Creates StateCopy Finished Message
	 * 
	 * @param lbProcessId
	 *            LoadBalancing Process Id
	 * @param pipe
	 *            Pipe Id
	 * @return Message with STATE_COPY_FINISHED msgType
	 */
	public static MovingStateResponseMessage createStateCopyFinishedMessage(
			int lbProcessId, String pipe) {
		MovingStateResponseMessage message = new MovingStateResponseMessage();
		message.setLoadBalancingProcessId(lbProcessId);
		message.setPipeID(pipe);
		message.setMsgType(STATE_COPY_FINISHED);
		return message;
	}

	/***
	 * Create AckInitState Copy Message
	 * 
	 * @param lbProcessId
	 *            LoadBalancingProcess Id
	 * @param pipe
	 *            Pipe Id
	 * @return Message with msgType ACK_INIT_STATE_COPY
	 */
	public static MovingStateResponseMessage createAckInitStateCopyMessage(
			int lbProcessId, String pipe) {
		MovingStateResponseMessage message = new MovingStateResponseMessage();
		message.setLoadBalancingProcessId(lbProcessId);
		message.setPipeID(pipe);
		message.setMsgType(ACK_INIT_STATE_COPY);
		return message;
	}

	/***
	 * Create install SUCCES Message
	 * 
	 * @param loadBalancingProcessId
	 *            load balancing process id
	 * @return Message with SUCCESS_INSTALL_QUERY as msgType
	 */
	public static MovingStateResponseMessage createInstallSuccessMessage(
			int loadBalancingProcessId) {
		MovingStateResponseMessage message = new MovingStateResponseMessage();
		message.setLoadBalancingProcessId(loadBalancingProcessId);
		message.setMsgType(SUCCESS_INSTALL_QUERY);
		return message;
	}

	/***
	 * Create Duplicate Success Message
	 * 
	 * @param loadBalancingProcessId
	 *            LoadBalancing Process ID
	 * @param pipeID
	 *            Pipe ID
	 * @return Message with msgType SUCCESS_DUPLICATE
	 */
	public static MovingStateResponseMessage createDuplicateSuccessMessage(
			int loadBalancingProcessId, String pipeID) {
		MovingStateResponseMessage message = new MovingStateResponseMessage();
		message.setLoadBalancingProcessId(loadBalancingProcessId);
		message.setPipeID(pipeID);
		message.setMsgType(SUCCESS_DUPLICATE);
		return message;
	}

	/**
	 * Create DuplicteFailure Message
	 * 
	 * @param loadBalancingProcessId
	 *            loadBalancing Process Id
	 * @return Message with msgType FAILURE_DUPLICATE_RECEIVER
	 */
	public static MovingStateResponseMessage createDuplicateFailureMessage(
			int loadBalancingProcessId) {
		MovingStateResponseMessage message = new MovingStateResponseMessage();
		message.setLoadBalancingProcessId(loadBalancingProcessId);
		message.setMsgType(FAILURE_DUPLICATE_RECEIVER);
		return message;
	}

	/**
	 * Create Install Failure Message
	 * 
	 * @param loadBalancingProcessId
	 *            LoadBalancing Process iD
	 * @return Message with msgType FAILURE_INSTALL_QUERY
	 */
	public static MovingStateResponseMessage createInstallFailureMessage(
			int loadBalancingProcessId) {
		MovingStateResponseMessage message = new MovingStateResponseMessage();
		message.setLoadBalancingProcessId(loadBalancingProcessId);
		message.setMsgType(FAILURE_INSTALL_QUERY);
		return message;
	}

	@Override
	/**
	 * Returns message as bytes.
	 */
	public byte[] toBytes() {

		ByteBuffer bb = null;
		int bbsize;

		switch (msgType) {
		case ACK_LOADBALANCING:
		case FAILURE_INSTALL_QUERY:
		case FAILURE_DUPLICATE_RECEIVER:
		case SUCCESS_INSTALL_QUERY:
		case ACK_ALL_STATE_COPIES_FINISHED:
		case STOP_BUFFERING_FINISHED:

			/*
			 * Allocate byte Buffer: 4 Bytes for integer msgType 4 Bytes for
			 * integer loadBalancingProcessId
			 */
			bbsize = 8;
			bb = ByteBuffer.allocate(bbsize);
			bb.putInt(msgType);
			bb.putInt(loadBalancingProcessId);

			break;

		case SUCCESS_DUPLICATE:
		case ACK_INIT_STATE_COPY:
		case STATE_COPY_FINISHED:

			/*
			 * Allocate byte Buffer: 4 Bytes for msgType 4 Bytes for integer
			 * loadBalancingProcessId 4 Bytes for integer pipeIdSize pipeIdSize
			 * bytes for PipeId
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

		switch (msgType) {

		case SUCCESS_DUPLICATE:
		case ACK_INIT_STATE_COPY:
		case STATE_COPY_FINISHED:

			int pipeIdSize = bb.getInt();
			byte[] pipeIdBytes = new byte[pipeIdSize];
			bb.get(pipeIdBytes);
			this.pipeID = new String(pipeIdBytes);

		}

	}

	/***
	 * Gets Message Type
	 * 
	 * @return Message Type
	 */
	public int getMsgType() {
		return msgType;
	}

	/**
	 * Sets Message Type
	 * 
	 * @param msgType
	 *            Message Type
	 */
	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	/**
	 * Gets PipeID
	 * 
	 * @return Pipe ID
	 */
	public String getPipeID() {
		return pipeID;
	}

	/**
	 * Sets Pipe ID
	 * 
	 * @param pipeID
	 *            Pipe ID to set
	 */
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
	 * 
	 * @return
	 */
	public int getLoadBalancingProcessId() {
		return loadBalancingProcessId;
	}

	/**
	 * Sets load Balancing Process Id.
	 * 
	 * @param loadBalancingProcessId
	 */
	public void setLoadBalancingProcessId(int loadBalancingProcessId) {
		this.loadBalancingProcessId = loadBalancingProcessId;
	}

	/***
	 * create Ack Copy Finished Message
	 * 
	 * @param lbProcessId
	 *            LoadBalancingProcess ID
	 * @return Message with msgType ACK_ALL_STATE_COPIES_FINISHED
	 */
	public static MovingStateResponseMessage createAckCopyingFinishedMsg(
			int lbProcessId) {
		MovingStateResponseMessage message = new MovingStateResponseMessage();
		message.setLoadBalancingProcessId(lbProcessId);
		message.setMsgType(ACK_ALL_STATE_COPIES_FINISHED);
		return message;
	}

	/***
	 * Create stop Buffering Finished Message
	 * 
	 * @param lbProcessId
	 *            loadBalancing Process ID
	 * @return Message with msgType STOP_BUFFERING_FINISHED
	 */
	public static MovingStateResponseMessage createStopBufferingFinishedMsg(
			int lbProcessId) {
		MovingStateResponseMessage message = new MovingStateResponseMessage();
		message.setLoadBalancingProcessId(lbProcessId);
		message.setMsgType(STOP_BUFFERING_FINISHED);
		return message;
	}

}
