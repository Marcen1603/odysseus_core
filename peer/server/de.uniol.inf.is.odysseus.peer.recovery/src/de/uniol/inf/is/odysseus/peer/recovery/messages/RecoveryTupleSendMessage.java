package de.uniol.inf.is.odysseus.peer.recovery.messages;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

import net.jxta.impl.id.UUID.UUID;
import net.jxta.impl.id.UUID.UUIDFactory;
import net.jxta.peer.PeerID;
import net.jxta.pipe.PipeID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.peer.communication.IMessage;

/**
 * Class of messages to either hold on sending tuples to the next peer or go on sending them.
 * 
 * @author Michael Brand, Tobias Brandt
 */
public class RecoveryTupleSendMessage implements IMessage {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(RecoveryTupleSendMessage.class);

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
	 * True for a hold on message; false for a go on message.
	 */
	private boolean mHoldOn;

	/**
	 * The id of the failed peer
	 */
	private PeerID mFailedPeer;

	/**
	 * The affected pipe.
	 * 
	 * @return The id of a pipe.
	 */
	public PipeID getPipeId() {
		return this.mPipe;
	}

	public PeerID getFailedPeerId() {
		return this.mFailedPeer;
	}

	/**
	 * Checks, if the instruction is either to hold on or to go on.
	 * 
	 * @return True for a hold on message; false for a go on message.
	 */
	public boolean isHoldOnInstruction() {
		return this.mHoldOn;
	}

	/**
	 * Empty default constructor.
	 */
	public RecoveryTupleSendMessage() {
		// Empty default constructor.
	}

	/**
	 * Creates a new recovery tuple send message.
	 * 
	 * @param pipe
	 *            The affected pipe. <br />
	 *            Must be not null.
	 * @param holdOn
	 *            True for a hold on message; false for a go on message.
	 */
	public RecoveryTupleSendMessage(PipeID pipe, PeerID failedPeer, boolean holdOn) {
		Preconditions.checkNotNull(pipe);

		this.mPipe = pipe;
		this.mHoldOn = holdOn;
		this.mFailedPeer = failedPeer;
	}

	@Override
	public byte[] toBytes() {
		byte[] idBytes = this.mID.toString().getBytes();
		byte[] pipeBytes = this.mPipe.toString().getBytes();

		int bufferSize = 4 + idBytes.length + 4 + pipeBytes.length + 4;

		byte[] failedPeerBytes = null;
		if (this.mFailedPeer != null) {
			// We don't have the failed peer in a goOn message, just in a holdOn message
			failedPeerBytes = this.mFailedPeer.toString().getBytes();
			bufferSize += 4 + failedPeerBytes.length;
		} else {
			// Save in the message, that there is no failedPeerId
			bufferSize += 4;
		}

		// last size for mHoldOn
		ByteBuffer buffer = ByteBuffer.allocate(bufferSize);

		buffer.putInt(idBytes.length);
		buffer.put(idBytes);

		buffer.putInt(pipeBytes.length);
		buffer.put(pipeBytes);

		if (failedPeerBytes != null) {
			buffer.putInt(failedPeerBytes.length);
			buffer.put(failedPeerBytes);
		} else {
			// Save "No failedPeerId"
			buffer.putInt(-1);
		}

		buffer.putInt((this.mHoldOn) ? 1 : 0);

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

		int peerBytesLength = buffer.getInt();
		if (peerBytesLength > 0) {
			try {

				byte[] failedPeerBytes = new byte[peerBytesLength];
				buffer.get(failedPeerBytes);
				URI peerURI = new URI(new String(failedPeerBytes));
				this.mFailedPeer = PeerID.create(peerURI);
			} catch (URISyntaxException e) {
				LOG.error("Could not create failed peer id from bytes!", e);
			}
		}

		this.mHoldOn = (buffer.getInt() == 1) ? true : false;
	}

}