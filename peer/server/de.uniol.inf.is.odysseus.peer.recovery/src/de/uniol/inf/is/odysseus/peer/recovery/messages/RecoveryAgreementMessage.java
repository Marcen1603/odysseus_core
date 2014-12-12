package de.uniol.inf.is.odysseus.peer.recovery.messages;

import java.net.URI;
import java.nio.ByteBuffer;

import net.jxta.impl.id.UUID.UUID;
import net.jxta.impl.id.UUID.UUIDFactory;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;

/**
 * This message is used to get an agreement if more than one peer could do and
 * wants to do the recovery for a failed peer. (It's always send if a peer
 * detects a failure)
 * 
 * @author Tobias Brandt, Michael Brand
 *
 */
public class RecoveryAgreementMessage implements IMessage {
	
	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(RecoveryAgreementMessage.class);

	/**
	 * The id of the message.
	 */
	private UUID mID = UUIDFactory.newUUID();

	/**
	 * The id of the message.
	 * 
	 * @return A unique identifier.
	 */
	public UUID getUUID() {
		return this.mID;
	}

	private PeerID mFailedPeer;
	
	public PeerID getFailedPeer() {
		return mFailedPeer;
	}
	
	private int mLocalQueryId;
	
	public int getLocalQueryId() {
		return mLocalQueryId;
	}
	
	/**
	 * Empty default constructor.
	 */
	public RecoveryAgreementMessage() {
		// Empty default constructor.
	}

	/**
	 * Creates a new message to agree with other peers who will do the recovery
	 * for a certain failed peer for a certain shared qurey
	 * 
	 * @param failedPeer
	 *            The peerId from the failed peer
	 * @param sharedQueryId
	 *            The query for which the sending peer wants to do the recovery
	 */
	public RecoveryAgreementMessage(
			PeerID failedPeer, int localQueryId) {
		Preconditions.checkNotNull(failedPeer);
		
		this.mFailedPeer = failedPeer;
		this.mLocalQueryId = localQueryId;
	}

	@Override
	public byte[] toBytes() {
		ByteBuffer bb = null;
		int bbsize;
		int peerIdLength = mFailedPeer.toString().getBytes().length;
		byte[] idBytes = this.mID.toString().getBytes();

		bbsize = 4 + idBytes.length + 4 + peerIdLength + 4;
		bb = ByteBuffer.allocate(bbsize);

		bb.putInt(idBytes.length);
		bb.put(idBytes);
		
		bb.putInt(peerIdLength);
		bb.put(mFailedPeer.toString().getBytes(), 0, peerIdLength);

		bb.putInt(mLocalQueryId);
		
		bb.flip();
		return bb.array();
	}

	@Override
	public void fromBytes(byte[] data) {
		ByteBuffer bb = ByteBuffer.wrap(data);
		
		int idBytesLength = bb.getInt();
		byte[] idBytes = new byte[idBytesLength];
		bb.get(idBytes);
		this.mID = new UUID(new String(idBytes));
		
		int peerIdLength = bb.getInt();
		byte[] peerIdByte = new byte[peerIdLength];
		bb.get(peerIdByte, 0, peerIdLength);
		String peerIdString = new String(peerIdByte);
		
		mLocalQueryId = bb.getInt();
		
		try {
			URI peerIdUri = new URI(peerIdString);
			mFailedPeer = PeerID.create(peerIdUri);
		} catch (Exception e) {
			LOG.error("Could not create failed peer id from bytes!", e);
		}

	}

}
