package de.uniol.inf.is.odysseus.peer.recovery.messages;

import java.net.URI;
import java.nio.ByteBuffer;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;

/**
 * The fail message for a recovery agreement.
 * 
 * @see RecoveryAgreementMessage
 * @author Michael Brand
 *
 */
public class RecoveryAgreementFailMessage implements IMessage {

	/**
	 * The ID of the failed peer.
	 */
	private PeerID mPeer;

	/**
	 * The ID of the shared query.
	 */
	private ID mSharedQueryId;

	/**
	 * The error message
	 */
	private String mMessage = "FAILED";

	/**
	 * Empty default constructor.
	 */
	public RecoveryAgreementFailMessage() {

		// Empty default constructor.

	}

	/**
	 * Creates a new failure message for a recovery agreement.
	 * 
	 * @param failedPeer
	 *            The ID of the failed peer. <br />
	 *            Must be not null.
	 * @param sharedQueryId
	 *            The ID of the shared query. <br />
	 *            Must be not null.
	 */
	public RecoveryAgreementFailMessage(PeerID failedPeer, ID sharedQueryId) {

		Preconditions.checkNotNull(failedPeer);
		Preconditions.checkNotNull(sharedQueryId);

		this.mPeer = failedPeer;
		this.mSharedQueryId = sharedQueryId;

	}

	/**
	 * Creates a new failure message for a recovery agreement.
	 * 
	 * @param failedPeer
	 *            The ID of the failed peer. <br />
	 *            Must be not null.
	 * @param sharedQueryId
	 *            The ID of the shared query. <br />
	 *            Must be not null.
	 * @param errorMessage
	 *            The error message.
	 */
	public RecoveryAgreementFailMessage(PeerID failedPeer, ID sharedQueryId,
			String errorMessage) {

		this(failedPeer, sharedQueryId);
		if (!Strings.isNullOrEmpty(errorMessage)) {

			this.mMessage = errorMessage;

		}

	}

	@Override
	public byte[] toBytes() {

		ByteBuffer bb = null;
		int bbsize;
		int peerIdLength = mPeer.toString().getBytes().length;
		int sharedQueryIdLength = mSharedQueryId.toString().getBytes().length;
		int messageLength = mMessage.getBytes().length;

		bbsize = 4 + peerIdLength + 4 + sharedQueryIdLength + 4 + messageLength;
		bb = ByteBuffer.allocate(bbsize);

		bb.putInt(peerIdLength);
		bb.put(mPeer.toString().getBytes(), 0, peerIdLength);

		bb.putInt(sharedQueryIdLength);
		bb.put(mSharedQueryId.toString().getBytes(), 0, sharedQueryIdLength);

		bb.putInt(messageLength);
		bb.put(mMessage.getBytes(), 0, messageLength);

		bb.flip();
		return bb.array();

	}

	@Override
	public void fromBytes(byte[] data) {

		ByteBuffer bb = ByteBuffer.wrap(data);

		int peerIdLength = bb.getInt();
		byte[] peerIdByte = new byte[peerIdLength];
		bb.get(peerIdByte, 0, peerIdLength);
		String peerIdString = new String(peerIdByte);

		int sharedQueryIdLength = bb.getInt();
		byte[] sharedQueryIdByte = new byte[sharedQueryIdLength];
		bb.get(sharedQueryIdByte, 0, sharedQueryIdLength);
		String sharedQueryIdString = new String(sharedQueryIdByte);

		try {
			URI peerIdUri = new URI(peerIdString);
			mPeer = PeerID.create(peerIdUri);

			URI sharedQueryIdUri = new URI(sharedQueryIdString);
			mSharedQueryId = ID.create(sharedQueryIdUri);
		} catch (Exception e) {
			e.printStackTrace();
		}

		int messageLength = bb.getInt();
		byte[] messageByte = new byte[messageLength];
		bb.get(messageByte, 0, messageLength);
		this.mMessage = new String(messageByte);

	}

	public PeerID getFailedPeer() {
		return mPeer;
	}

	public ID getSharedQueryId() {
		return mSharedQueryId;
	}

	/**
	 * Gets the error message.
	 * 
	 * @return The error message.
	 */
	public String getErrorMessage() {

		return this.mMessage;

	}

}