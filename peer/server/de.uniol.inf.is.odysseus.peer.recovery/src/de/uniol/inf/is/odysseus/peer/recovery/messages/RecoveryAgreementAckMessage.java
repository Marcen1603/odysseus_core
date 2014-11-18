package de.uniol.inf.is.odysseus.peer.recovery.messages;

import java.net.URI;
import java.nio.ByteBuffer;

import com.google.common.base.Preconditions;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;

/**
 * The acknowledge message for a recovery agreement.
 * 
 * @see RecoveryAgreementMessage
 * @author Michael Brand
 *
 */
public class RecoveryAgreementAckMessage implements IMessage {

	/**
	 * The ID of the failed peer.
	 */
	private PeerID mPeer;
	
	/**
	 * The ID of the shared query.
	 */
	private ID mSharedQueryId;
	
	/**
	 * Empty default constructor.
	 */
	public RecoveryAgreementAckMessage() {
		
		// Empty default constructor.
		
	}

	/**
	 * Creates a new acknowledge message for a recovery agreement.
	 * 
	 * @param failedPeer
	 *            The ID of the failed peer. <br />
	 *            Must be not null.
	 * @param sharedQueryId
	 *            The ID of the shared query. <br />
	 *            Must be not null.
	 */
	public RecoveryAgreementAckMessage(PeerID failedPeer, ID sharedQueryId) {
		
		Preconditions.checkNotNull(failedPeer);
		Preconditions.checkNotNull(sharedQueryId);
		
		this.mPeer = failedPeer;
		this.mSharedQueryId = sharedQueryId;
		
	}

	@Override
	public byte[] toBytes() {
		
		ByteBuffer bb = null;
		int bbsize;
		int peerIdLength = mPeer.toString().getBytes().length;
		int sharedQueryIdLength = mSharedQueryId.toString().getBytes().length;

		bbsize = 4 + peerIdLength + 4 + sharedQueryIdLength;
		bb = ByteBuffer.allocate(bbsize);

		bb.putInt(peerIdLength);
		bb.put(mPeer.toString().getBytes(), 0, peerIdLength);

		bb.putInt(sharedQueryIdLength);
		bb.put(mSharedQueryId.toString().getBytes(), 0, sharedQueryIdLength);
		
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

	}

	public PeerID getFailedPeer() {
		return mPeer;
	}

	public ID getSharedQueryId() {
		return mSharedQueryId;
	}

}