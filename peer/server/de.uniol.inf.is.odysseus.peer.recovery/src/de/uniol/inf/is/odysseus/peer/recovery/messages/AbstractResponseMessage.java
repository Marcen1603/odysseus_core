package de.uniol.inf.is.odysseus.peer.recovery.messages;

import java.nio.ByteBuffer;

import net.jxta.impl.id.UUID.UUID;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;

/**
 * Abstract class for response messages (acks/fails).
 * 
 * @author Michael Brand
 */
public abstract class AbstractResponseMessage implements IMessage {

	/**
	 * The id of the original message.
	 */
	private UUID mID;

	/**
	 * The id of the original message.
	 * 
	 * @return A unique identifier.
	 */
	public UUID getUUID() {
		return this.mID;
	}

	/**
	 * The optional error message.
	 */
	private Optional<String> mErrorMessage = Optional.absent();

	/**
	 * The error message, if the response is negative.
	 * 
	 * @return An error message, if the response is negativ, or
	 *         {@link Optional#absent()}, if the response is positive.
	 */
	public Optional<String> getErrorMessage() {
		return this.mErrorMessage;
	}

	/**
	 * Checks, if the respsonse is an acknowlegde.
	 * 
	 * @return True, if the response is an acknowledge; false, if any error
	 *         occured.
	 */
	public boolean isPositive() {
		return !this.mErrorMessage.isPresent();
	}

	/**
	 * Empty default constructor.
	 */
	public AbstractResponseMessage() {
		// Empty default constructor.
	}

	/**
	 * Creates a new acknowledge message.
	 * 
	 * @param uuid
	 *            The id of the original message. <br />
	 *            Must be not null.
	 */
	public AbstractResponseMessage(UUID uuid) {
		Preconditions.checkNotNull(uuid);

		this.mID = uuid;
	}

	/**
	 * Creates a new failure message.
	 * 
	 * @param uuid
	 *            The id of the original message. <br />
	 *            Must be not null.
	 * @param errorMessage
	 *            The error message. <br />
	 *            Must be not null
	 */
	public AbstractResponseMessage(UUID uuid, String errorMessage) {
		this(uuid);
		Preconditions.checkNotNull(errorMessage);

		this.mErrorMessage = Optional.of(errorMessage);
	}

	@Override
	public byte[] toBytes() {
		byte[] idBytes = this.mID.toString().getBytes();
		byte[] messageBytes = null;

		int bufferSize = 4 + idBytes.length + 4;
		// last size for mPositive
		if (this.mErrorMessage.isPresent()) {
			messageBytes = this.mErrorMessage.get().getBytes();
			bufferSize += 4 + messageBytes.length;
		}
		ByteBuffer buffer = ByteBuffer.allocate(bufferSize);

		buffer.putInt(idBytes.length);
		buffer.put(idBytes);
		buffer.putInt((!this.mErrorMessage.isPresent()) ? 1 : 0);
		if (messageBytes != null) {
			buffer.putInt(messageBytes.length);
			buffer.put(messageBytes);
		}

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

		boolean positive = (buffer.getInt() == 1) ? true : false;
		if (!positive) {
			int messageBytesLength = buffer.getInt();
			byte[] messageBytes = new byte[messageBytesLength];
			buffer.get(messageBytes);
			this.mErrorMessage = Optional.of(new String(messageBytes));
		}
	}

}