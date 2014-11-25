package de.uniol.inf.is.odysseus.peer.recovery.messages;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

import net.jxta.id.ID;
import net.jxta.impl.id.UUID.UUID;
import net.jxta.impl.id.UUID.UUIDFactory;
import net.jxta.peer.PeerID;
import net.jxta.pipe.PipeID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;

/**
 * Class of messages to update either a sender or a receiver.
 * 
 * @author Michael Brand, Tobias Brandt
 */
public class RecoveryUpdatePipeMessage implements IMessage {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(RecoveryUpdatePipeMessage.class);

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

	/**
	 * The affected pipe.
	 */
	private PipeID mPipe;

	/**
	 * The affected pipe.
	 * 
	 * @return The id of a pipe.
	 */
	public PipeID getPipeId() {
		return this.mPipe;
	}

	/**
	 * The new sending or receiving peer.
	 */
	private PeerID mPeer;

	/**
	 * The new sending or receiving peer.
	 * 
	 * @return The id of a peer.
	 */
	public PeerID getNewPeerId() {
		return this.mPeer;
	}

	/**
	 * The affected shared query.
	 */
	private ID mSharedQuery;

	/**
	 * The affected shared query.
	 * 
	 * @return The id of a shared query.
	 */
	public ID getSharedQueryId() {
		return this.mSharedQuery;
	}

	/**
	 * True for a sender update message; false for a receiver update message.
	 */
	private boolean mSenderUpdate;

	/**
	 * Checks, if the instruction is either to update a sender or to update a
	 * receiver.
	 * 
	 * @return True for a sender update message; false for a receiver update
	 *         message.
	 */
	public boolean isSenderUpdateInstruction() {
		return this.mSenderUpdate;
	}

	/**
	 * Empty default constructor.
	 */
	public RecoveryUpdatePipeMessage() {
		// Empty default constructor.
	}

	/**
	 * Creates a new recovery update pipe message.
	 * 
	 * @param pipe
	 *            The affected pipe. <br />
	 *            Must be not null.
	 * @param peer
	 *            The new sending or receiving peer. <br />
	 *            Must be not null.
	 * @param sharedQuery
	 *            The affected shared query. <br />
	 *            Must be not null.
	 * @param senderUpdate
	 *            True for a sender update message; false for a receiver update
	 *            message.
	 */
	public RecoveryUpdatePipeMessage(PipeID pipe, PeerID peer, ID sharedQuery,
			boolean senderUpdate) {
		Preconditions.checkNotNull(pipe);
		Preconditions.checkNotNull(peer);
		Preconditions.checkNotNull(sharedQuery);

		this.mPipe = pipe;
		this.mPeer = peer;
		this.mSharedQuery = sharedQuery;
		this.mSenderUpdate = senderUpdate;
	}

	@Override
	public byte[] toBytes() {
		byte[] idBytes = this.mID.toString().getBytes();
		byte[] pipeBytes = this.mPipe.toString().getBytes();
		byte[] peerBytes = this.mPeer.toString().getBytes();
		byte[] sharedQueryBytes = this.mSharedQuery.toString().getBytes();

		int bufferSize = 4 + idBytes.length + 4 + pipeBytes.length + 4
				+ peerBytes.length + 4 + sharedQueryBytes.length + 4;
		// last size for mSenderUpdate
		ByteBuffer buffer = ByteBuffer.allocate(bufferSize);

		buffer.putInt(idBytes.length);
		buffer.put(idBytes);
		buffer.putInt(pipeBytes.length);
		buffer.put(pipeBytes);
		buffer.putInt(peerBytes.length);
		buffer.put(peerBytes);
		buffer.putInt(sharedQueryBytes.length);
		buffer.put(sharedQueryBytes);
		buffer.putInt((this.mSenderUpdate) ? 1 : 0);

		buffer.flip();
		return buffer.array();
	}

	@Override
	public void fromBytes(byte[] data) {
		ByteBuffer buffer = ByteBuffer.wrap(data);

		int idBytesLength = buffer.getInt();
		byte[] idBytes = new byte[idBytesLength];
		buffer.get(idBytes);
		this.mID = new UUID(new String(idBytes));

		try {
			int pipeBytesLength = buffer.getInt();
			byte[] pipeBytes = new byte[pipeBytesLength];
			buffer.get(pipeBytes);
			URI pipeURI = new URI(new String(pipeBytes));
			this.mPipe = PipeID.create(pipeURI);
		} catch (URISyntaxException e) {
			LOG.error("Could not create pipe id from bytes!", e);
		}

		try {
			int peerBytesLength = buffer.getInt();
			byte[] peerBytes = new byte[peerBytesLength];
			buffer.get(peerBytes);
			URI peerURI = new URI(new String(peerBytes));
			this.mPeer = PeerID.create(peerURI);
		} catch (URISyntaxException e) {
			LOG.error("Could not create peer id from bytes!", e);
		}
		
		try {
			int sharedQueryBytesLength = buffer.getInt();
			byte[] sharedQueryBytes = new byte[sharedQueryBytesLength];
			buffer.get(sharedQueryBytes);
			URI sharedQueryURI = new URI(new String(sharedQueryBytes));
			this.mSharedQuery = ID.create(sharedQueryURI);
		} catch (URISyntaxException e) {
			LOG.error("Could not create shared query id from bytes!", e);
		}

		this.mSenderUpdate = (buffer.getInt() == 1) ? true : false;
	}

}