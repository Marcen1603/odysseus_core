package de.uniol.inf.is.odysseus.peer.recovery.messages;

import java.nio.ByteBuffer;
import java.util.UUID;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryBackupInformation;

/**
 * The acknowledge message for backup information.
 * 
 * @see BackupInformationMessage
 * @author Michael Brand
 *
 */
public class BackupInformationAckMessage implements IMessage {

	/**
	 * The id of the backup information.
	 */
	private UUID mID;

	/**
	 * Empty default constructor.
	 */
	public BackupInformationAckMessage() {

		// Empty default constructor.

	}

	/**
	 * Creates a new backup information acknowledge message.
	 * 
	 * @param info
	 *            The received information. <br />
	 *            Must be not null.
	 */
	public BackupInformationAckMessage(IRecoveryBackupInformation info) {

		Preconditions.checkNotNull(info);
		this.mID = info.toUUID();

	}

	/**
	 * Gets the id of the backup information.
	 * 
	 * @return id The id.
	 */
	public UUID getUUID() {

		Preconditions.checkNotNull(this.mID);
		return this.mID;

	}

	@Override
	public byte[] toBytes() {

		Preconditions.checkNotNull(this.mID);

		int bufferSize = 0;

		byte[] idBytes = this.determineIDBytes();
		bufferSize += 4 + idBytes.length;

		ByteBuffer buffer = ByteBuffer.allocate(bufferSize);

		buffer.putInt(idBytes.length);
		buffer.put(idBytes);

		buffer.flip();
		return buffer.array();

	}

	/**
	 * Encodes the backup information id.
	 * 
	 * @return The bytes of {@link IRecoveryBackupInformation#toUUID()}.
	 */
	private byte[] determineIDBytes() {

		Preconditions.checkNotNull(this.mID);
		return this.mID.toString().getBytes();

	}

	@Override
	public void fromBytes(byte[] data) {

		Preconditions.checkNotNull(data);
		ByteBuffer buffer = ByteBuffer.wrap(data);

		this.idFromBytes(buffer);

	}

	/**
	 * Decodes the id from bytes.
	 * 
	 * @param buffer
	 *            The current byte buffer, where the information about the id
	 *            are next. <br />
	 *            Must be not null.
	 */
	private void idFromBytes(ByteBuffer buffer) {

		Preconditions.checkArgument(this.mID == null);
		Preconditions.checkNotNull(buffer);
		byte[] idBytes = new byte[buffer.getInt()];
		buffer.get(idBytes);
		this.mID = toID(new String(idBytes));

	}

	/**
	 * Creates a backup information id from String.
	 * 
	 * @param text
	 *            The given String.
	 * @return The UUID represented by <code>text</code> or null, if an error
	 *         occurs.
	 */
	private static UUID toID(String text) {

		Preconditions.checkNotNull(text);
		return UUID.fromString(text);

	}

}