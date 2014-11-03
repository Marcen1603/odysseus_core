package de.uniol.inf.is.odysseus.peer.recovery.messages;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;
import net.jxta.pipe.PipeID;
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
	 * The peer can go on sending the tuples
	 */
	public static final int GO_ON = 1;

	/**
	 * The peer has to install the attached queries
	 */
	public static final int ADD_QUERY = 2;

	public static final int UPDATE_SENDER = 3;

	public static final int UPDATE_RECEIVER = 4;

	/**
	 * The peer which receives this message is the buddy of the sender, so the
	 * receiving peer is (also) responsible for recovery if the sender peer
	 * fails
	 */
	public static final int BE_BUDDY = 5;

	private String pqlQuery;
	private int messageType;
	private PeerID newSender;
	private PeerID newReceiver;
	private ID sharedQueryId;
	private PipeID pipeId;
	private List<String> pql;

	/**
	 * This message tells that the receiver of this message should hold on. This
	 * means that the peer stores the results of that query-part and sends it
	 * further when he receives a goOnMessage.
	 * 
	 * @param queryId
	 *            ID of the query which has to hold on.
	 * @return A message which tells this.
	 */
	public static RecoveryInstructionMessage createHoldOnMessage(PipeID pipeId) {
		RecoveryInstructionMessage holdOnMessage = new RecoveryInstructionMessage();
		holdOnMessage.setMessageType(HOLD_ON);
		holdOnMessage.setPipeId(pipeId);
		return holdOnMessage;
	}

	/**
	 * This message tells that the receiver of this message can go in with
	 * sending messages to the next peer for the given peerId. This will empty
	 * the buffer.
	 * 
	 * @param pipeId
	 * @return
	 */
	public static RecoveryInstructionMessage createGoOnMessage(PipeID pipeId) {
		RecoveryInstructionMessage goOnMessage = new RecoveryInstructionMessage();
		goOnMessage.setMessageType(GO_ON);
		goOnMessage.setPipeId(pipeId);
		return goOnMessage;
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
	public static RecoveryInstructionMessage createAddQueryMessage(String pqlQuery, ID sharedQueryId) {
		RecoveryInstructionMessage addQueryMessage = new RecoveryInstructionMessage();
		addQueryMessage.setMessageType(ADD_QUERY);
		addQueryMessage.setPqlQuery(pqlQuery);
		addQueryMessage.setSharedQueryId(sharedQueryId);
		return addQueryMessage;
	}

	public static RecoveryInstructionMessage createUpdateSenderMessage(PeerID newSender, ID sharedQueryId) {
		RecoveryInstructionMessage newSenderMessage = new RecoveryInstructionMessage();
		newSenderMessage.setMessageType(UPDATE_SENDER);
		newSenderMessage.setNewSender(newSender);
		newSenderMessage.setSharedQueryId(sharedQueryId);
		return newSenderMessage;
	}

	/**
	 * Tells the receiver of this message, that it has to update it's receiver
	 * to receive the tuples from a new peer
	 * 
	 * @param newSender
	 *            The new sender for the receiver of the peer where this message
	 *            arrives
	 * @param sharedQueryId
	 *            The shared query-id which indicated, which parts on different
	 *            peers belong together
	 * @return A message which tells to receive the tuples from a new peer
	 */
	public static RecoveryInstructionMessage createUpdateReceiverMessage(PeerID newSender, PipeID pipeId) {
		RecoveryInstructionMessage upateReceiverMessage = new RecoveryInstructionMessage();
		upateReceiverMessage.setMessageType(UPDATE_RECEIVER);
		upateReceiverMessage.setNewSender(newSender);
		upateReceiverMessage.setPipeId(pipeId);
		return upateReceiverMessage;
	}

	/**
	 * Tells the receiver of this message, that it is a buddy of the sender
	 * peer. That means, that this peer is (also) responsible for the recovery
	 * if the sender peer fails. The sender peer has to send his
	 * backup-information to this peer.
	 * 
	 * @return A message which tells the receiver that he is a buddy of the
	 *         sender for the specified sharedQueryId
	 */
	public static RecoveryInstructionMessage createBeBuddyMessage(ID sharedQueryId, List<String> infos) {
		RecoveryInstructionMessage beBuddyMessage = new RecoveryInstructionMessage();
		beBuddyMessage.setMessageType(BE_BUDDY);
		beBuddyMessage.setSharedQueryId(sharedQueryId);
		beBuddyMessage.setPql(infos);
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

		int sharedQueryIdLength = 0;
		int pipeIdLength = 0;

		switch (messageType) {
		case HOLD_ON:
		case GO_ON:
			pipeIdLength = pipeId.toString().getBytes().length;
			bbsize = 4 + 4 + pipeIdLength;
			bb = ByteBuffer.allocate(bbsize);
			// 1. MessageType
			bb.putInt(messageType);
			// 2. Length of the pipeId
			bb.putInt(pipeIdLength);
			// 3. SharedQueryId
			bb.put(pipeId.toString().getBytes());
			break;
		case ADD_QUERY:
			sharedQueryIdLength = sharedQueryId.toString().getBytes().length;
			byte[] pqlAsBytes = pqlQuery.getBytes();
			bbsize = 4 + 4 + sharedQueryIdLength + 4 + pqlAsBytes.length;
			bb = ByteBuffer.allocate(bbsize);
			bb.putInt(messageType);
			bb.putInt(sharedQueryId.toString().getBytes().length);
			bb.put(sharedQueryId.toString().getBytes());
			bb.putInt(pqlAsBytes.length);
			bb.put(pqlAsBytes);
			break;
		case UPDATE_SENDER:
			break;
		case UPDATE_RECEIVER:
			int newSenderLength = newSender.toString().getBytes().length;
			pipeIdLength = pipeId.toString().getBytes().length;
			bbsize = 4 + 4 + newSenderLength + 4 + pipeIdLength;
			bb = ByteBuffer.allocate(bbsize);
			bb.putInt(messageType);
			bb.putInt(newSenderLength);
			bb.put(newSender.toString().getBytes());
			bb.putInt(pipeIdLength);
			bb.put(pipeId.toString().getBytes());
			break;
		case BE_BUDDY:
			// for the length of the list
			int listLength = 4;
			// integers for the lengths of the pql strings
			listLength += 4 * pql.toArray().length;
			// length of the strings
			for (String pqlString : pql) {
				listLength += pqlString.getBytes().length;
			}
			bbsize = 4 + 4 + sharedQueryIdLength + listLength;
			bb = ByteBuffer.allocate(bbsize);
			bb.putInt(messageType);
			bb.putInt(sharedQueryIdLength);
			bb.put(sharedQueryId.toString().getBytes());
			// Now the list
			bb.putInt(pql.toArray().toString().length());
			for (String pqlString : pql) {
				bb.putInt(pqlString.getBytes().length);
				bb.put(pqlString.getBytes());
			}
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

		int pipeIdLength = 0;
		byte[] pipeIdByte;
		String pipeIdString;

		int sharedQueryIdLength = 0;
		byte[] sharedQueryIdByte;
		String sharedQueryIdString;

		switch (messageType) {
		case HOLD_ON:
		case GO_ON:
			pipeIdLength = bb.getInt();
			pipeIdByte = new byte[pipeIdLength];
			bb.get(pipeIdByte, 0, pipeIdLength);
			pipeIdString = new String(pipeIdByte);
			try {
				URI uri = new URI(pipeIdString);
				this.pipeId = PipeID.create(uri);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			break;
		case ADD_QUERY:
			sharedQueryIdLength = bb.getInt();
			sharedQueryIdByte = new byte[sharedQueryIdLength];
			bb.get(sharedQueryIdByte, 0, sharedQueryIdLength);
			sharedQueryIdString = new String(sharedQueryIdByte);
			try {
				URI uri = new URI(sharedQueryIdString);
				sharedQueryId = ID.create(uri);
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			int pqlLength = bb.getInt();
			byte[] pqlAsByte = new byte[pqlLength];
			bb.get(pqlAsByte);
			pqlQuery = new String(pqlAsByte);
			break;
		case UPDATE_RECEIVER:
			int newSenderLength = bb.getInt();
			byte[] newSenderByte = new byte[newSenderLength];
			bb.get(newSenderByte, 0, newSenderLength);
			String newSenderString = new String(newSenderByte);
			try {
				URI uri = new URI(newSenderString);
				newSender = PeerID.create(uri);
			} catch (Exception e) {
				e.printStackTrace();
			}

			pipeIdLength = bb.getInt();
			pipeIdByte = new byte[pipeIdLength];
			bb.get(pipeIdByte, 0, pipeIdLength);
			pipeIdString = new String(pipeIdByte);
			try {
				URI uri = new URI(pipeIdString);
				pipeId = PipeID.create(uri);
			} catch (Exception e) {
				e.printStackTrace();
			}

			break;
		case BE_BUDDY:
			sharedQueryIdLength = bb.getInt();
			sharedQueryIdByte = new byte[sharedQueryIdLength];
			bb.get(sharedQueryIdByte, 0, sharedQueryIdLength);
			sharedQueryIdString = new String(sharedQueryIdByte);
			try {
				URI uri = new URI(sharedQueryIdString);
				sharedQueryId = ID.create(uri);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			int listLength = bb.getInt();
			pql = new ArrayList<String>();
			for (int i = 0; i < listLength; i++) {
				int stringLength = bb.getInt();
				byte[] pqlByte = new byte[stringLength];
				bb.get(pqlByte, 0, stringLength);
				String pqlString = new String(pqlByte);
				pql.add(pqlString);
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

	public PipeID getPipeId() {
		return pipeId;
	}

	public void setPipeId(PipeID pipeId) {
		this.pipeId = pipeId;
	}

	public List<String> getPql() {
		return pql;
	}

	public void setPql(List<String> pql) {
		this.pql = pql;
	}

}
