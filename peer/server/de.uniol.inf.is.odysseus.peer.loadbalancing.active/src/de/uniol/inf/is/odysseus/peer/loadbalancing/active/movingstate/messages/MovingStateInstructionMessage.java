package de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.messages;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

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
	
	public List<String> getOtherPeerIDs() {
		return this.otherPeers;
	}
	
	public String getSharedQueryID() {
		return this.sharedQueryID;
	}
	
	public boolean isMasterForQuery() {
		return this.isMasterForQuery;
	}

	/**
	 * PQLQuery for ADD_QUERY Message
	 */
	private String PQLQuery;

	/**
	 * Pipe ID used in different Messages
	 */
	private String pipeId;

	private String newPipe;
	
	public String getNewPipe() {
		return newPipe;
	}

	public void setNewPipe(String newPipe) {
		this.newPipe = newPipe;
	}

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
	private boolean isMasterForQuery;
	
	/**
	 * Needed if query Part was Master for shared Query.
	 */
	private List<String> otherPeers;
	
	/**
	 * Needed if query part was Master for shared Query.
	 */
	private String sharedQueryID;
	private String masterPeerID;

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
			int lbProcessId, String PQLQuery, String sharedQueryID, String masterPeerID) {
		MovingStateInstructionMessage message = new MovingStateInstructionMessage();
		message.loadBalancingProcessId = lbProcessId;
		message.PQLQuery = PQLQuery;
		message.isMasterForQuery = false;
		message.sharedQueryID = sharedQueryID;
		message.setMasterPeerID(masterPeerID);
		message.msgType = ADD_QUERY;
		return message;
	}
	
	public static MovingStateInstructionMessage createAddQueryMsgForMasterQuery(int lbProcessId, String PQLQuery, List<String> otherPeers, String sharedQueryID) {
		MovingStateInstructionMessage message = new MovingStateInstructionMessage();
		message.loadBalancingProcessId = lbProcessId;
		message.PQLQuery = PQLQuery;
		message.isMasterForQuery = true;
		message.otherPeers = otherPeers;
		message.sharedQueryID = sharedQueryID;
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
			int lbProcessId, String newPeerId, String pipeId,String newPipe) {
		MovingStateInstructionMessage message = new MovingStateInstructionMessage();
		message.loadBalancingProcessId = lbProcessId;
		message.pipeId = pipeId;
		message.newPipe = newPipe;
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
			int lbProcessId, String peerId, String pipeId, String newPipe) {
		MovingStateInstructionMessage message = new MovingStateInstructionMessage();
		message.loadBalancingProcessId = lbProcessId;
		message.pipeId = pipeId;
		message.peerId = peerId;
		message.newPipe = newPipe;
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
			 * 1 Bytes for byte-coded boolean.
			 * String
			 */
			
			if(!isMasterForQuery) {

				byte[] pqlAsBytes = PQLQuery.getBytes();

				byte[] sharedQueryIDAsBytes = sharedQueryID.getBytes();
				byte[] masterPeerIDAsBytes = masterPeerID.getBytes();

	
				bbsize = 4 + 4 + 4 + pqlAsBytes.length + 4 + sharedQueryIDAsBytes.length + 1 + 4 + masterPeerIDAsBytes.length;
				bb = ByteBuffer.allocate(bbsize);
				bb.putInt(msgType);
				bb.putInt(loadBalancingProcessId);
				bb.putInt(pqlAsBytes.length);
				bb.put(pqlAsBytes);
				bb.putInt(sharedQueryIDAsBytes.length);
				bb.put(sharedQueryIDAsBytes);
				bb.put((byte)0);
				bb.putInt(masterPeerIDAsBytes.length);
				bb.put(masterPeerIDAsBytes);
				break;
			}
			
			else {
				byte[] pqlAsBytes = PQLQuery.getBytes();
				byte[] otherPeersAsBytes = stringListToBytes(this.getOtherPeerIDs());
				byte[] sharedQueryIDAsBytes = sharedQueryID.getBytes();
				
				bbsize = 4 + 4 + 4 + pqlAsBytes.length + 1 + otherPeersAsBytes.length + 4 + sharedQueryIDAsBytes.length;
				bb = ByteBuffer.allocate(bbsize);
				bb.putInt(msgType);
				bb.putInt(loadBalancingProcessId);
				bb.putInt(pqlAsBytes.length);
				bb.put(pqlAsBytes);
				bb.putInt(sharedQueryIDAsBytes.length);
				bb.put(sharedQueryIDAsBytes);
				bb.put((byte)1);
				bb.put(otherPeersAsBytes);
				break;
			}

		case REPLACE_RECEIVER:
		case INSTALL_BUFFER_AND_REPLACE_SENDER:
			/*
			 * Allocate byte Buffer: 4 Bytes for integer msgType 4 Bytes for
			 * integer loadBalancingProcessId 4 Bytes for integer Size of
			 * oldPipeId 4 Bytes for integer Size of newPeerId oldPipeId
			 * newPeerId
			 */

			byte[] oldPipeIdBytes = pipeId.getBytes();
			byte[] newPipeIdBytes = newPipe.getBytes();
			byte[] newPeerIdBytes = peerId.getBytes();

			bbsize = 4 + 4 + 4 + 4 +4 + oldPipeIdBytes.length + newPipeIdBytes.length
					+ newPeerIdBytes.length;

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
			
			int sizeOfSharedQueryID = bb.getInt();
			byte[] sharedQueryIDAsBytes = new byte[sizeOfSharedQueryID];
			bb.get(sharedQueryIDAsBytes);
			this.sharedQueryID = new String(sharedQueryIDAsBytes);
			
			
			byte masterFlag = bb.get();
			if(masterFlag==0) {
				isMasterForQuery = false;
				int sizeOfMasterPeerID = bb.getInt();
				byte[] masterPeerIDBytes = new byte[sizeOfMasterPeerID];
				bb.get(masterPeerIDBytes);
				this.masterPeerID = new String(masterPeerIDBytes);
				
				break;
			}
			else {
				isMasterForQuery = true;
				this.otherPeers = new ArrayList<String>();
				int numberOfOtherPeers = bb.getInt();
				for (int i=0;i<numberOfOtherPeers;i++) {
					int sizeOfPeerID = bb.getInt();
					byte[] peerIDStringAsBytes = new byte[sizeOfPeerID];
					bb.get(peerIDStringAsBytes);
					otherPeers.add(new String(peerIDStringAsBytes));
				}
				break;
			}
			
			

		case REPLACE_RECEIVER:
		case INSTALL_BUFFER_AND_REPLACE_SENDER:
			int sizeOfPipeId = bb.getInt();
			int sizeOfNewPipeId = bb.getInt();
			int sizeOfPeerId = bb.getInt();

			byte[] pipeIdAsBytes = new byte[sizeOfPipeId];
			byte[] newPipeIdAsBytes = new byte[sizeOfNewPipeId];
			byte[] peerIdAsBytes = new byte[sizeOfPeerId];

			bb.get(pipeIdAsBytes);
			bb.get(newPipeIdAsBytes);
			bb.get(peerIdAsBytes);

			this.pipeId = new String(pipeIdAsBytes);
			this.newPipe = new String(newPipeIdAsBytes);
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
	
	private byte[] stringListToBytes(List<String> strings) {
		int numberOfBytesNeeded = 4;
		
		//Calculate Buffer Size.
		for (String element : strings) {
			int numberOfBytesForElement = element.getBytes().length;
			numberOfBytesNeeded += 4;
			numberOfBytesNeeded +=numberOfBytesForElement;
		}
		
		ByteBuffer bb = ByteBuffer.allocate(numberOfBytesNeeded);
		bb.putInt(strings.size());
		for (String element : strings) {
			bb.putInt(element.getBytes().length);
			bb.put(element.getBytes());
		}
		
		return bb.array();
	}

	public String getMasterPeerID() {
		return masterPeerID;
	}

	public void setMasterPeerID(String masterPeerID) {
		this.masterPeerID = masterPeerID;
	}

}
