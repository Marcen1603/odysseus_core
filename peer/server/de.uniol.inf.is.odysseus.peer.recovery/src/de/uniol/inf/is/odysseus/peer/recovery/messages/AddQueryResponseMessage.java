package de.uniol.inf.is.odysseus.peer.recovery.messages;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.UUID;

import net.jxta.id.ID;
import net.jxta.pipe.PipeID;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;

/**
 * Response Message for AddQuery Messages. The Response could be an ACK or a
 * fail. If the queryPart could not installed a fail response is send and the
 * query Part need to be reallocated. The response also contains most of the
 * information from the message.
 * 
 * @author ChrisToenjesDeye
 * 
 */
public class AddQueryResponseMessage implements IMessage {
	public static final int FAIL = 0;
	public static final int ACK = 1;

	private int mMessageType;
	private UUID recoveryProcessStateId;
	private UUID subprocessId;
	private String pqlQueryPart;
	private ID sharedQueryId;

	public AddQueryResponseMessage() {
		// Empty default constructor
	}

	public AddQueryResponseMessage(PipeID pipeId, int messageType) {
		this.mMessageType = messageType;
	}

	public static AddQueryResponseMessage createAddQueryAckMessage(
			RecoveryAddQueryMessage instruction) {
		return createAddQueryAckMessage(instruction.getSharedQueryId(),
				instruction.getRecoveryProcessId(),
				instruction.getmSubprocessId(), instruction.getPQLCode());
	}

	public static AddQueryResponseMessage createAddQueryAckMessage(
			ID sharedQueryId, UUID recoveryProcessStateId, UUID subprocessId,
			String pqlQueryPart) {
		AddQueryResponseMessage addQueryAckMessage = new AddQueryResponseMessage();
		addQueryAckMessage.setMessageType(ACK);
		addQueryAckMessage.setRecoveryProcessStateId(recoveryProcessStateId);
		addQueryAckMessage.setSubprocessId(subprocessId);
		addQueryAckMessage.setPqlQueryPart(pqlQueryPart);
		addQueryAckMessage.setSharedQueryId(sharedQueryId);
		return addQueryAckMessage;
	}

	public static AddQueryResponseMessage createAddQueryFailMessage(
			RecoveryAddQueryMessage instruction) {
		return createAddQueryFailMessage(instruction.getSharedQueryId(),
				instruction.getRecoveryProcessId(),
				instruction.getmSubprocessId(), instruction.getPQLCode());
	}

	public static AddQueryResponseMessage createAddQueryFailMessage(
			ID sharedQueryId, UUID recoveryProcessStateId, UUID subprocessId,
			String pqlQueryPart) {
		AddQueryResponseMessage addQueryFailMessage = new AddQueryResponseMessage();
		addQueryFailMessage.setMessageType(FAIL);
		addQueryFailMessage.setRecoveryProcessStateId(recoveryProcessStateId);
		addQueryFailMessage.setSubprocessId(subprocessId);
		addQueryFailMessage.setPqlQueryPart(pqlQueryPart);
		addQueryFailMessage.setSharedQueryId(sharedQueryId);
		return addQueryFailMessage;
	}

	@Override
	public byte[] toBytes() {
		ByteBuffer bb = null;
		int bbsize;
		int sharedQueryIdLength = 0;

		sharedQueryIdLength = sharedQueryId.toString().getBytes().length;
		byte[] pqlAsBytes = pqlQueryPart.getBytes();
		byte[] processIdAsBytes = recoveryProcessStateId.toString().getBytes();
		byte[] subprocessIdAsBytes = subprocessId.toString().getBytes();

		bbsize = 4 + 4 + processIdAsBytes.length + 4
				+ subprocessIdAsBytes.length + 4 + pqlAsBytes.length + 4
				+ sharedQueryIdLength;
		bb = ByteBuffer.allocate(bbsize);
		bb.putInt(this.mMessageType);
		bb.putInt(processIdAsBytes.length);
		bb.put(processIdAsBytes);
		bb.putInt(subprocessIdAsBytes.length);
		bb.put(subprocessIdAsBytes);
		bb.putInt(pqlAsBytes.length);
		bb.put(pqlAsBytes);
		bb.putInt(sharedQueryId.toString().getBytes().length);
		bb.put(sharedQueryId.toString().getBytes());

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

		int processIdLength = bb.getInt();
		byte[] processIdAsBytes = new byte[processIdLength];
		bb.get(processIdAsBytes);
		recoveryProcessStateId = UUID.fromString(new String(processIdAsBytes));

		int subprocessIdLength = bb.getInt();
		byte[] subprocessIdAsBytes = new byte[subprocessIdLength];
		bb.get(subprocessIdAsBytes);
		subprocessId = UUID.fromString(new String(subprocessIdAsBytes));

		int pqlLength = bb.getInt();
		byte[] pqlAsByte = new byte[pqlLength];
		bb.get(pqlAsByte);
		pqlQueryPart = new String(pqlAsByte);

		extractSharedQueryId(bb);
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
			e.printStackTrace();
		}
	}

	public int getMessageType() {
		return this.mMessageType;
	}

	public void setMessageType(int messageType) {
		this.mMessageType = messageType;
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

	public UUID getSubprocessId() {
		return subprocessId;
	}

	public void setSubprocessId(UUID subprocessId) {
		this.subprocessId = subprocessId;
	}
}
