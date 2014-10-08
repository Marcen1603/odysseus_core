package de.uniol.inf.is.odysseus.peer.recovery.messages;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

import net.jxta.id.ID;
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

	/**
	 * The peer has to install the attached queries
	 */
	public static final int ADD_QUERY = 1;

	public static final int NEW_SENDER = 2;

	public static final int NEW_RECEIVER = 3;

	/**
	 * The peer which receives this message is the buddy of the sender, so the
	 * receiving peer is (also) responsible for recovery if the sender peer
	 * fails
	 */
	public static final int BE_BUDDY = 4;

	private String pqlQuery;
	private int messageType;
	private PeerID newSender;
	private PeerID newReceiver;
	private ID sharedQueryId;

	/**
	 * This message tells, that the receiver of this message should hold on.
	 * This means that the peer stores the results of that query and sends it
	 * further when he receives a message with the new receiver.
	 * 
	 * @param queryId
	 *            ID of the query which has to hold on.
	 * @return A message which tells this.
	 */
	public static RecoveryInstructionMessage createHoldOnMessage(
			ID sharedQueryId) {
		RecoveryInstructionMessage holdOnMessage = new RecoveryInstructionMessage();
		holdOnMessage.setMessageType(HOLD_ON);
		holdOnMessage.setSharedQueryId(sharedQueryId);
		return holdOnMessage;
	}

	/**
	 * This message tells that the receiver of this message should install the
	 * given query.
	 * 
	 * @param pqlQuery
	 *            The query the peer should install, given as PQL.
	 * @param sharedQueryId
	 *            ID of the shared query, so that you can identify which query
	 *            parts from different peers belong together
	 * @return A message which tells the receiving peer to install this query.
	 */
	public static RecoveryInstructionMessage createAddQueryMessage(
			String pqlQuery, ID sharedQueryId) {
		RecoveryInstructionMessage addQueryMessage = new RecoveryInstructionMessage();
		addQueryMessage.setMessageType(ADD_QUERY);
		addQueryMessage.setPqlQuery(pqlQuery);
		addQueryMessage.setSharedQueryId(sharedQueryId);
		return addQueryMessage;
	}

	public static RecoveryInstructionMessage createNewSenderMessage(
			PeerID newSender, ID sharedQueryId) {
		RecoveryInstructionMessage newSenderMessage = new RecoveryInstructionMessage();
		newSenderMessage.setMessageType(NEW_SENDER);
		newSenderMessage.setNewSender(newSender);
		newSenderMessage.setSharedQueryId(sharedQueryId);
		return newSenderMessage;
	}

	/**
	 * Tells the receiver of this message, that it has to send the tuples for
	 * query with the given shared query-id to a new receiver
	 * 
	 * @param newReceiver
	 *            PeerID of the new receiver, probably the peer which replaces
	 *            the failed peer
	 * @param sharedQueryId
	 *            The shared query-id which indicated, which parts on different
	 *            peers belong together
	 * @return A message which tells to send the tuples to the new peer
	 */
	public static RecoveryInstructionMessage createNewReceiverMessage(
			PeerID newReceiver, ID sharedQueryId) {
		RecoveryInstructionMessage newReceiverMessage = new RecoveryInstructionMessage();
		newReceiverMessage.setMessageType(NEW_RECEIVER);
		newReceiverMessage.setNewReceiver(newReceiver);
		newReceiverMessage.setSharedQueryId(sharedQueryId);
		return newReceiverMessage;
	}

	/**
	 * Tells the receiver of this message, that it is a buddy of the sender
	 * peer. That means, that this peer is (also) responsible for the recovery
	 * if the sender peer fails. The sender peer has to send his
	 * backup-information to this peer.
	 * 
	 * @return A message which tells the receiver that he is a buddy of the
	 *         sender
	 */
	public static RecoveryInstructionMessage createBeBuddyMessage(
			ID sharedQueryId) {
		RecoveryInstructionMessage beBuddyMessage = new RecoveryInstructionMessage();
		beBuddyMessage.setMessageType(BE_BUDDY);
		beBuddyMessage.setSharedQueryId(sharedQueryId);
		return beBuddyMessage;
	}

	/**
	 * Puts the message into a byte-array. The message-type is always at the
	 * beginning with an integer-value in the first 4 bytes.
	 */
	@Override
	public byte[] toBytes() {
		// TODO Not finished yet

		ByteBuffer bb = null;
		int bbsize;

		int sharedQueryIdLength = sharedQueryId.toString().getBytes().length;

		switch (messageType) {
		case HOLD_ON:
			bbsize = 4 + 4 + sharedQueryId.toString().getBytes().length;
			bb = ByteBuffer.allocate(bbsize);
			// 1. MessageType
			bb.putInt(messageType);
			// 2. Length of the sharedQueryId
			bb.putInt(sharedQueryId.toString().getBytes().length);
			// 3. SharedQueryId
			bb.put(sharedQueryId.toString().getBytes());
			break;
		case ADD_QUERY:
			byte[] pqlAsBytes = pqlQuery.getBytes();
			bbsize = 4 + 4 + sharedQueryIdLength + 4 + pqlAsBytes.length;
			bb = ByteBuffer.allocate(bbsize);
			bb.putInt(messageType);
			bb.putInt(sharedQueryId.toString().getBytes().length);
			bb.put(sharedQueryId.toString().getBytes());
			bb.putInt(pqlAsBytes.length);
			bb.put(pqlAsBytes);
			break;
		case NEW_SENDER:
			break;
		case NEW_RECEIVER:
			int newReceiverLength = newReceiver.toString().getBytes().length;
			bbsize = 4 + 4 + newReceiverLength + 4 + sharedQueryIdLength;
			bb = ByteBuffer.allocate(bbsize);
			bb.putInt(messageType);
			bb.putInt(sharedQueryIdLength);
			bb.put(sharedQueryId.toString().getBytes());
			bb.putInt(newReceiverLength);
			bb.put(newReceiver.toString().getBytes());
			break;
		case BE_BUDDY:
			bbsize = 4 + 4 + sharedQueryIdLength;
			bb = ByteBuffer.allocate(bbsize);
			bb.putInt(messageType);
			bb.putInt(sharedQueryIdLength);
			bb.put(sharedQueryId.toString().getBytes());
			break;
		}

		bb.flip();
		return bb.array();
	}

	/**
	 * Parses message from byte array.
	 */
	@Override
	public void fromBytes(byte[] data) {
		// TODO Not finished yet
		ByteBuffer bb = ByteBuffer.wrap(data);
		messageType = bb.getInt();
		int sharedQueryIdLength = bb.getInt();
		byte[] sharedQueryIdByte = new byte[sharedQueryIdLength];
		bb.get(sharedQueryIdByte, 0, sharedQueryIdLength);
		String sharedQueryIdString = new String(sharedQueryIdByte);
		try {
			URI uri = new URI(sharedQueryIdString);
			sharedQueryId = ID.create(uri);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		switch (messageType) {
		case ADD_QUERY:
			int pqlLength = bb.getInt();
			byte[] pqlAsByte = new byte[pqlLength];
			bb.get(pqlAsByte);
			pqlQuery = new String(pqlAsByte);
			break;
		case NEW_RECEIVER:
			int newReceiverLength = bb.getInt();
			byte[] newReceiverByte = new byte[newReceiverLength];
			bb.get(newReceiverByte, 0, newReceiverLength);
			String newReceiverString = new String(newReceiverByte);
			try {
				URI uri = new URI(newReceiverString);
				newReceiver = PeerID.create(uri);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
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

	public ID getSharedQueryId() {
		return sharedQueryId;
	}

	public void setSharedQueryId(ID sharedQueryId) {
		this.sharedQueryId = sharedQueryId;
	}

}
