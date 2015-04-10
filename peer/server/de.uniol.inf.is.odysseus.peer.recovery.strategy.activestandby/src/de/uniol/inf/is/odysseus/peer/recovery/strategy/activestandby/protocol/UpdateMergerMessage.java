package de.uniol.inf.is.odysseus.peer.recovery.strategy.activestandby.protocol;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

import net.jxta.impl.id.UUID.UUID;
import net.jxta.impl.id.UUID.UUIDFactory;
import net.jxta.pipe.PipeID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.peer.communication.IMessage;

/**
 * Class of messages to update a recovery merger that one of it's inputs has
 * been recovered.
 * 
 * @author Michael Brand
 */
public class UpdateMergerMessage implements IMessage {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(UpdateMergerMessage.class);

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
	 * The affected pipe to find connected mergers.
	 */
	private PipeID mPipe;

	/**
	 * The affected pipe to find connected mergers.
	 * 
	 * @return The id of a pipe.
	 */
	public PipeID getPipeId() {
		return this.mPipe;
	}

	/**
	 * Empty default constructor.
	 */
	public UpdateMergerMessage() {
		// Empty default constructor.
	}

	/**
	 * Creates a new update merger message.
	 * 
	 * @param pipe
	 *            The affected pipe to find connected mergers.
	 */
	public UpdateMergerMessage(PipeID pipe) {
		Preconditions.checkNotNull(pipe);
		this.mPipe = pipe;
	}

	@Override
	public byte[] toBytes() {
		byte[] idBytes = this.mID.toString().getBytes();
		byte[] pipeBytes = this.mPipe.toString().getBytes();

		int bufferSize = 4 + idBytes.length + 4 + pipeBytes.length;
		ByteBuffer buffer = ByteBuffer.allocate(bufferSize);

		buffer.putInt(idBytes.length);
		buffer.put(idBytes);

		buffer.putInt(pipeBytes.length);
		buffer.put(pipeBytes);

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
	}

}