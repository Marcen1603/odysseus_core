package de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.messages;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.peer.communication.IMessage;

/**
 * Message to which advises Peers to duplicate a particular connection.
 * 
 * @author Carsten Cordes
 *
 */
public class ParallelTrackInstructionMessage implements IMessage {

	public static final int INITIATE_LOADBALANCING = 0;
	public static final int ADD_QUERY = 1;
	public static final int COPY_RECEIVER = 2;
	public static final int COPY_SENDER = 3;
	public static final int DELETE_SENDER = 4;
	public static final int DELETE_RECEIVER = 5;

	public static final int MESSAGE_RECEIVED = 6;
	public static final int PIPE_SUCCCESS_RECEIVED = 7;

	private int loadBalancingProcessId;
	private int msgType;

	private String PQLQuery;

	/**
	 * Needed if query part was Master for shared Query.
	 */
	private String sharedQueryID;
	private String masterPeerID;

	/**
	 * True if current Query is Master for shared Query.
	 */
	private boolean isMasterForQuery;

	public boolean isMasterForQuery() {
		return isMasterForQuery;
	}

	/**
	 * Needed if query Part was Master for shared Query.
	 */
	private List<String> otherPeers;

	private String newPeerId;
	private String oldPipeId;
	private String newPipeId;

	public String getSharedQueryID() {
		return this.sharedQueryID;
	}

	/**
	 * Default constructor.
	 */
	public ParallelTrackInstructionMessage() {
	}

	public String getMasterPeerID() {
		return masterPeerID;
	}

	public List<String> getOtherPeerIDs() {
		return this.otherPeers;
	}

	public void setMasterPeerID(String masterPeerID) {
		this.masterPeerID = masterPeerID;
	}

	public static ParallelTrackInstructionMessage createAddQueryMsgForMasterQuery(int lbProcessId, String PQLQuery, List<String> otherPeers, String sharedQueryID) {
		ParallelTrackInstructionMessage message = new ParallelTrackInstructionMessage();
		message.loadBalancingProcessId = lbProcessId;
		message.PQLQuery = PQLQuery;
		message.isMasterForQuery = true;
		message.otherPeers = otherPeers;
		message.sharedQueryID = sharedQueryID;
		message.msgType = ADD_QUERY;
		return message;
	}

	public static ParallelTrackInstructionMessage createInitiateMsg(int lbProcessId) {
		ParallelTrackInstructionMessage message = new ParallelTrackInstructionMessage();
		message.loadBalancingProcessId = lbProcessId;
		message.msgType = INITIATE_LOADBALANCING;
		return message;
	}

	public static ParallelTrackInstructionMessage createMessageReceivedMsg(int lbProcessId) {
		ParallelTrackInstructionMessage message = new ParallelTrackInstructionMessage();
		message.loadBalancingProcessId = lbProcessId;
		message.msgType = MESSAGE_RECEIVED;
		return message;
	}

	public static ParallelTrackInstructionMessage createAddQueryMsg(int lbProcessId, String PQLQuery, String sharedQueryID, String masterPeerID) {
		ParallelTrackInstructionMessage message = new ParallelTrackInstructionMessage();
		message.loadBalancingProcessId = lbProcessId;
		message.PQLQuery = PQLQuery;
		message.isMasterForQuery = false;
		message.sharedQueryID = sharedQueryID;
		message.setMasterPeerID(masterPeerID);
		message.msgType = ADD_QUERY;
		return message;
	}

	public static ParallelTrackInstructionMessage createPipeReceivedMsg(int lbProcessId, String pipeID) {
		ParallelTrackInstructionMessage message = new ParallelTrackInstructionMessage();
		message.loadBalancingProcessId = lbProcessId;
		message.setOldPipeId(pipeID);
		message.msgType = PIPE_SUCCCESS_RECEIVED;
		return message;
	}

	public static ParallelTrackInstructionMessage createCopyOperatorMsg(int lbProcessId, boolean isSender, String newPeerId, String oldPipeId, String newPipeId) {
		ParallelTrackInstructionMessage message = new ParallelTrackInstructionMessage();
		message.loadBalancingProcessId = lbProcessId;
		message.oldPipeId = oldPipeId;
		message.newPipeId = newPipeId;
		message.newPeerId = newPeerId;
		if (isSender) {
			message.msgType = COPY_SENDER;
		} else {
			message.msgType = COPY_RECEIVER;
		}
		return message;
	}

	public static ParallelTrackInstructionMessage createDeleteOperatorMsg(boolean isSender, int lbProcessId, String pipeId) {
		ParallelTrackInstructionMessage message = new ParallelTrackInstructionMessage();
		message.setLoadBalancingProcessId(lbProcessId);
		message.oldPipeId = pipeId;
		if (isSender) {
			message.msgType = DELETE_SENDER;
		} else {
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

			if (!isMasterForQuery) {

				byte[] pqlAsBytes = PQLQuery.getBytes();

				byte[] sharedQueryIDAsBytes;
				if(sharedQueryID==null) {
					sharedQueryIDAsBytes="null".getBytes();
				}
				else {
					sharedQueryIDAsBytes = sharedQueryID.getBytes();
				}
				
				byte[] masterPeerIDAsBytes;
				if(masterPeerID==null) {
					masterPeerIDAsBytes = "null".getBytes();
				}
				else {
					masterPeerIDAsBytes = masterPeerID.getBytes();
				}

				bbsize = 4 + 4 + 4 + pqlAsBytes.length + 4 + sharedQueryIDAsBytes.length + 1 + 4 + masterPeerIDAsBytes.length;
				bb = ByteBuffer.allocate(bbsize);
				bb.putInt(msgType);
				bb.putInt(loadBalancingProcessId);
				bb.putInt(pqlAsBytes.length);
				bb.put(pqlAsBytes);
				bb.putInt(sharedQueryIDAsBytes.length);
				bb.put(sharedQueryIDAsBytes);
				bb.put((byte) 0);
				bb.putInt(masterPeerIDAsBytes.length);
				bb.put(masterPeerIDAsBytes);
				break;
			}

			byte[] pqlAsBytes = PQLQuery.getBytes();
			
			if(this.getOtherPeerIDs()==null) {
				this.otherPeers = new ArrayList<String>();
			}
			
			byte[] otherPeersAsBytes = stringListToBytes(this.getOtherPeerIDs());
			
			byte[] sharedQueryIDAsBytes;
			if(sharedQueryID==null) {
				sharedQueryIDAsBytes="null".getBytes();
			}
			else {
				sharedQueryIDAsBytes = sharedQueryID.getBytes();
			}

			bbsize = 4 + 4 + 4 + pqlAsBytes.length + 1 + otherPeersAsBytes.length + 4 + sharedQueryIDAsBytes.length;
			bb = ByteBuffer.allocate(bbsize);
			bb.putInt(msgType);
			bb.putInt(loadBalancingProcessId);
			bb.putInt(pqlAsBytes.length);
			bb.put(pqlAsBytes);
			bb.putInt(sharedQueryIDAsBytes.length);
			bb.put(sharedQueryIDAsBytes);
			bb.put((byte) 1);
			bb.put(otherPeersAsBytes);
			break;

		case COPY_RECEIVER:
		case COPY_SENDER:
			/*
			 * Allocate byte Buffer: 4 Bytes for integer msgType 4 Bytes for
			 * integer loadBalancingProcessId 4 Bytes for integer Size of
			 * oldPipeId 4 Bytes for integer Size of newPipeId 4 Bytes for
			 * integer Size of newPeerId oldPipeId newPipeId newPeerId
			 */

			byte[] oldPipeIdBytes = oldPipeId.getBytes();
			byte[] newPipeIdBytes = newPipeId.getBytes();
			byte[] newPeerIdBytes = newPeerId.getBytes();

			bbsize = 4 + 4 + 4 + 4 + 4 + oldPipeIdBytes.length + newPipeIdBytes.length + newPeerIdBytes.length;

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

			int sizeOfSharedQueryID = bb.getInt();
			byte[] sharedQueryIDAsBytes = new byte[sizeOfSharedQueryID];
			bb.get(sharedQueryIDAsBytes);
			String receivedSharedQueryID = new String(sharedQueryIDAsBytes);
			if(!receivedSharedQueryID.equals("null")) {
				this.sharedQueryID = new String(sharedQueryIDAsBytes);
			}
			else {
				this.sharedQueryID = null;
			}

			byte masterFlag = bb.get();
			if (masterFlag == 0) {
				isMasterForQuery = false;
				int sizeOfMasterPeerID = bb.getInt();
				byte[] masterPeerIDBytes = new byte[sizeOfMasterPeerID];
				bb.get(masterPeerIDBytes);
				this.masterPeerID = new String(masterPeerIDBytes);

				break;
			}
			isMasterForQuery = true;
			this.otherPeers = new ArrayList<String>();
			int numberOfOtherPeers = bb.getInt();
			for (int i = 0; i < numberOfOtherPeers; i++) {
				int sizeOfPeerID = bb.getInt();
				byte[] peerIDStringAsBytes = new byte[sizeOfPeerID];
				bb.get(peerIDStringAsBytes);
				otherPeers.add(new String(peerIDStringAsBytes));
			}
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
		case PIPE_SUCCCESS_RECEIVED:
			int sizeOfPipeId = bb.getInt();
			byte[] pipeIdAsBytes = new byte[sizeOfPipeId];
			bb.get(pipeIdAsBytes);
			this.oldPipeId = new String(pipeIdAsBytes);
			break;
		default:
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

	private byte[] stringListToBytes(List<String> strings) {
		int numberOfBytesNeeded = 4;

		// Calculate Buffer Size.
		for (String element : strings) {
			int numberOfBytesForElement = element.getBytes().length;
			numberOfBytesNeeded += 4;
			numberOfBytesNeeded += numberOfBytesForElement;
		}

		ByteBuffer bb = ByteBuffer.allocate(numberOfBytesNeeded);
		bb.putInt(strings.size());
		for (String element : strings) {
			bb.putInt(element.getBytes().length);
			bb.put(element.getBytes());
		}

		return bb.array();
	}

}
