package de.uniol.inf.is.odysseus.peer.recovery.messages;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;

/**
 * 
 * @author Tobias Brandt
 *
 */
public class RecoveryInstructionMessage implements IMessage {

	/**
	 * The peer has to stop sending tuples for query x to the next peer.
	 * Instead, he stores them until he get a message with the new receiver-peer
	 */
	public static final int HOLD_ON = 0;

	public static final int ADD_QUERY = 1;

	public static final int NEW_SENDER = 2;

	public static final int NEW_RECEIVER = 3;

	private String pqlQuery;
	private int messageType;
	private PeerID newSender;
	private PeerID newReceiver;
	private int sharedQueryId;

	/**
	 * This message tells, that the receiver of this message should hold on.
	 * This means that the peer stores the results of that query and sends it
	 * further when he receives a message with the new receiver.
	 * 
	 * @param queryId
	 *            ID of the query which has to hold on.
	 * @return A message which tells this.
	 */
	public static RecoveryInstructionMessage createHoldOnMessage(int sharedQueryId) {
		RecoveryInstructionMessage holdOnMessage = new RecoveryInstructionMessage();
		holdOnMessage.setMessageType(HOLD_ON);
		holdOnMessage.setSharedQueryId(sharedQueryId);
		return holdOnMessage;
	}

	public static RecoveryInstructionMessage createAddQueryMessage(String pqlQuery, int sharedQueryId) {
		RecoveryInstructionMessage addQueryMessage = new RecoveryInstructionMessage();
		addQueryMessage.setMessageType(ADD_QUERY);
		addQueryMessage.setPqlQuery(pqlQuery);
		addQueryMessage.setSharedQueryId(sharedQueryId);
		return addQueryMessage;
	}

	public static RecoveryInstructionMessage createNewSenderMessage(PeerID newSender, int sharedQueryId) {
		RecoveryInstructionMessage newSenderMessage = new RecoveryInstructionMessage();
		newSenderMessage.setMessageType(NEW_SENDER);
		newSenderMessage.setNewSender(newSender);
		newSenderMessage.setSharedQueryId(sharedQueryId);
		return newSenderMessage;
	}

	public static RecoveryInstructionMessage createNewReceiverMessage(
			PeerID newReceiver, int sharedQueryId) {
		RecoveryInstructionMessage newReceiverMessage = new RecoveryInstructionMessage();
		newReceiverMessage.setMessageType(NEW_RECEIVER);
		newReceiverMessage.setNewReceiver(newReceiver);
		newReceiverMessage.setSharedQueryId(sharedQueryId);
		return newReceiverMessage;
	}

	@Override
	public byte[] toBytes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void fromBytes(byte[] data) {
		// TODO Auto-generated method stub

	}

	public String getPqlQuery() {
		return pqlQuery;
	}

	public void setPqlQuery(String pqlQuery) {
		this.pqlQuery = pqlQuery;
	}

	public int getMessageType() {
		return messageType;
	}

	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

	public PeerID getNewSender() {
		return newSender;
	}

	public void setNewSender(PeerID newSender) {
		this.newSender = newSender;
	}

	public PeerID getNewReceiver() {
		return newReceiver;
	}

	public void setNewReceiver(PeerID newReceiver) {
		this.newReceiver = newReceiver;
	}

	public int getSharedQueryId() {
		return sharedQueryId;
	}

	public void setSharedQueryId(int sharedQueryId) {
		this.sharedQueryId = sharedQueryId;
	}

}
