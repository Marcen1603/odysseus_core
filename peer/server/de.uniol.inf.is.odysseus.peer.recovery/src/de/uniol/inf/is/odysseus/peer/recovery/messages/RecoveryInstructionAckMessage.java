package de.uniol.inf.is.odysseus.peer.recovery.messages;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.UUID;

import net.jxta.id.ID;
import net.jxta.pipe.PipeID;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;

/**
 * The acknowledge message for a recovery instruction.
 * 
 * @see RecoveryInstructionMessage
 * @author Michael Brand
 * 
 */
public class RecoveryInstructionAckMessage implements IMessage {

	public static final int HOLD_ON_ACK = 0;

	public static final int GO_ON_ACK = 1;

	public static final int ADD_QUERY_ACK = 2;

	public static final int UPDATE_SENDER_ACK = 3;

	public static final int UPDATE_RECEIVER_ACK = 4;

	private int mMessageType;
	private PipeID mPipeId;
	private UUID recoveryProcessStateId;
	private String pqlQueryPart;
	private ID sharedQueryId;

	public RecoveryInstructionAckMessage() {
		// Empty default constructor
	}

	public RecoveryInstructionAckMessage(PipeID pipeId, int messageType) {
		this.mMessageType = messageType;
		this.mPipeId = pipeId;
	}

	public static RecoveryInstructionAckMessage createAddQueryAckMessage(
			ID sharedQueryId, UUID recoveryProcessStateId) {
		RecoveryInstructionAckMessage addQueryAckMessage = new RecoveryInstructionAckMessage();
		addQueryAckMessage.setMessageType(ADD_QUERY_ACK);
		addQueryAckMessage.setRecoveryProcessStateId(recoveryProcessStateId);
		return addQueryAckMessage;
	}

	@Override
	public byte[] toBytes() {
		ByteBuffer bb = null;
		int bbsize;
		int pipeIdLength = 0;
		int sharedQueryIdLength = 0;

		switch (mMessageType) {
		case ADD_QUERY_ACK:
			sharedQueryIdLength = sharedQueryId.toString().getBytes().length;
			byte[] pqlAsBytes = pqlQueryPart.getBytes();
			pipeIdLength = this.mPipeId.toString().getBytes().length;
			byte[] processIdAsBytes = recoveryProcessStateId.toString()
					.getBytes();

			bbsize = 4 + 4 + pipeIdLength + 4 + processIdAsBytes.length + 4
					+ pqlAsBytes.length + 4 + sharedQueryIdLength;
			bb = ByteBuffer.allocate(bbsize);
			bb.putInt(this.mMessageType);
			bb.putInt(pipeIdLength);
			bb.put(this.mPipeId.toString().getBytes());
			bb.putInt(processIdAsBytes.length);
			bb.put(processIdAsBytes);
			bb.putInt(pqlAsBytes.length);
			bb.put(pqlAsBytes);
			bb.putInt(sharedQueryId.toString().getBytes().length);
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
		ByteBuffer bb = ByteBuffer.wrap(data);
		this.mMessageType = bb.getInt();

		switch (mMessageType) {
		case ADD_QUERY_ACK:
			int pipeIdLength = bb.getInt();
			byte[] pipeIdByte = new byte[pipeIdLength];
			String pipeIdString = new String(pipeIdByte);
			try {
				URI uri = new URI(pipeIdString);
				this.mPipeId = PipeID.create(uri);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}

			int processIdLength = bb.getInt();
			byte[] processIdAsBytes = new byte[processIdLength];
			bb.get(processIdAsBytes);
			recoveryProcessStateId = UUID.fromString(new String(
					processIdAsBytes));
			
			int pqlLength = bb.getInt();
			byte[] pqlAsByte = new byte[pqlLength];
			bb.get(pqlAsByte);
			pqlQueryPart = new String(pqlAsByte);
			
			extractSharedQueryId(bb);
			
			break;
		}
	}

	private void extractSharedQueryId(ByteBuffer bb) {
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
	}
	
	public int getMessageType() {
		return this.mMessageType;
	}

	public void setMessageType(int messageType) {
		this.mMessageType = messageType;
	}

	public PipeID getPipeId() {
		return this.mPipeId;
	}

	public void setPipeId(PipeID pipeId) {
		this.mPipeId = pipeId;
	}

	public UUID getRecoveryProcessStateId() {
		return recoveryProcessStateId;
	}

	public void setRecoveryProcessStateId(UUID recoveryProcessStateId) {
		this.recoveryProcessStateId = recoveryProcessStateId;
	}

	public String getPqlQueryPart() {
		return pqlQueryPart;
	}

	public void setPqlQueryPart(String pqlQueryPart) {
		this.pqlQueryPart = pqlQueryPart;
	}

	public ID getSharedQueryId() {
		return sharedQueryId;
	}

	public void setSharedQueryId(ID sharedQueryId) {
		this.sharedQueryId = sharedQueryId;
	}
}