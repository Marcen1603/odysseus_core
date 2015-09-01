package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.communicator.paralleltrack.messages;

import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.peer.communication.IMessage;

/**
 * Message to show installing the new QueryPart was Successful (Sent TO
 * initiating Peer)
 * 
 * @author Carsten Cordes
 *
 */
public class ParallelTrackResponseMessage implements IMessage {

	public static final int ACK_LOADBALANCING = 0;

	public static final int SUCCESS_INSTALL_QUERY = 1;
	public static final int FAILURE_INSTALL_QUERY = 2;

	public static final int SUCCESS_DUPLICATE = 3;
	public static final int FAILURE_DUPLICATE_RECEIVER = 4;

	public static final int SYNC_FINISHED = 5;

	public static final int DELETE_FINISHED = 6;

	/*
	 * ID to identify current load Balancing process (is reused in different
	 * stages of negotiation between clients)
	 */
	private int loadBalancingProcessId;

	private int msgType;

	private String pipeID;
	private Integer[] installedQueries;

	/**
	 * Default Constructor
	 */
	public ParallelTrackResponseMessage() {
	}

	public static ParallelTrackResponseMessage createAckLoadbalancingMessage(int loadBalancingProcessId) {
		ParallelTrackResponseMessage message = new ParallelTrackResponseMessage();
		message.setLoadBalancingProcessId(loadBalancingProcessId);
		message.setMsgType(ACK_LOADBALANCING);
		return message;
	}

	public static ParallelTrackResponseMessage createInstallSuccessMessage(int loadBalancingProcessId) {
		ParallelTrackResponseMessage message = new ParallelTrackResponseMessage();
		message.setLoadBalancingProcessId(loadBalancingProcessId);
		message.setMsgType(SUCCESS_INSTALL_QUERY);
		return message;
	}

	public static ParallelTrackResponseMessage createDeleteFinishedMessage(int loadBalancingProcessId, String pipeID) {
		ParallelTrackResponseMessage message = new ParallelTrackResponseMessage();
		message.setLoadBalancingProcessId(loadBalancingProcessId);
		message.setPipeID(pipeID);
		message.setMsgType(DELETE_FINISHED);
		return message;
	}

	public static ParallelTrackResponseMessage createDuplicateSuccessMessage(int loadBalancingProcessId, String pipeID) {
		ParallelTrackResponseMessage message = new ParallelTrackResponseMessage();
		message.setLoadBalancingProcessId(loadBalancingProcessId);
		message.setPipeID(pipeID);
		message.setMsgType(SUCCESS_DUPLICATE);
		return message;
	}

	public static ParallelTrackResponseMessage createDuplicateFailureMessage(int loadBalancingProcessId) {
		ParallelTrackResponseMessage message = new ParallelTrackResponseMessage();
		message.setLoadBalancingProcessId(loadBalancingProcessId);
		message.setMsgType(FAILURE_DUPLICATE_RECEIVER);
		return message;
	}

	public static ParallelTrackResponseMessage createInstallFailureMessage(int loadBalancingProcessId) {
		ParallelTrackResponseMessage message = new ParallelTrackResponseMessage();
		message.setLoadBalancingProcessId(loadBalancingProcessId);
		message.setMsgType(FAILURE_INSTALL_QUERY);
		return message;
	}

	public static ParallelTrackResponseMessage createSyncFinishedMsg(int lbProcessId, String pipeID) {
		ParallelTrackResponseMessage message = new ParallelTrackResponseMessage();
		message.setMsgType(SYNC_FINISHED);
		message.setLoadBalancingProcessId(lbProcessId);
		message.setPipeID(pipeID);
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
		case SYNC_FINISHED:
		case DELETE_FINISHED:

			/*
			 * Allocate byte Buffer: 4 Bytes for msgType 4 Bytes for integer
			 * loadBalancingProcessId 4 Bytes for integer pipeIdSize payloadSize
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
		default:
			break;
		}

		if (bb != null) {
			bb.flip();
			return bb.array();
		}

		return new byte[0];
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
		case SYNC_FINISHED:
		case DELETE_FINISHED:

			int pipeIdSize = bb.getInt();
			byte[] pipeIdBytes = new byte[pipeIdSize];
			bb.get(pipeIdBytes);
			this.pipeID = new String(pipeIdBytes);
			break;
		default:
			break;
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

}