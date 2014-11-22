package de.uniol.inf.is.odysseus.peer.recovery.messages;

import java.nio.ByteBuffer;
import java.util.UUID;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryBackupInformation;

/**
 * The fail message for backup information.
 * 
 * @see BackupInformationMessage
 * @author Michael Brand
 *
 */
public class BackupInformationFailMessage implements IMessage {

	/**
	 * The id of the backup information.
	 */
	private UUID mID;

	/**
	 * The error message
	 */
	private String mMessage = "FAILED";

	/**
	 * Empty default constructor.
	 */
	public BackupInformationFailMessage() {

		// Empty default constructor.

	}

	/**
	 * Creates a new backup information failure message.
	 * 
	 * @param info
	 *            The received information. <br />
	 *            Must be not null.
	 */
	public BackupInformationFailMessage(IRecoveryBackupInformation info) {

		Preconditions.checkNotNull(info);
		this.mID = info.toUUID();

	}

	/**
	 * Creates a new backup information failure message.
	 * 
	 * @param info
	 *            The received information. <br />
	 *            Must be not null.
	 * @param errorMessage
	 *            The error message.
	 */
	public BackupInformationFailMessage(IRecoveryBackupInformation info,
			String errorMessage) {

		this(info);
		if (!Strings.isNullOrEmpty(errorMessage)) {

			this.mMessage = errorMessage;

		}

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

	/**
	 * Gets the error message.
	 * 
	 * @return The error message.
	 */
	public String getErrorMessage() {

		return this.mMessage;

	}

	@Override
	public byte[] toBytes() {

		Preconditions.checkNotNull(this.mID);

		int bufferSize = 0;

		byte[] idBytes = this.determineIDBytes();
		bufferSize += 4 + idBytes.length;

		byte[] messageBytes = this.determineErrorMessageBytes();
		bufferSize += 4 + messageBytes.length;

		ByteBuffer buffer = ByteBuffer.allocate(bufferSize);

		buffer.putInt(idBytes.length);
		buffer.put(idBytes);

		buffer.putInt(messageBytes.length);
		buffer.put(messageBytes);

		buffer.flip();
		return buffer.array();

	}

	/**
	 * Encodes the error message.
	 * 
	 * @return The bytes of {@link #mMessage}.
	 */
	private byte[] determineErrorMessageBytes() {

		return this.mMessage.getBytes();

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
		this.errorMessageFromBytes(buffer);

	}

	/**
	 * Decodes the error message from bytes.
	 * 
	 * @param buffer
	 *            The current byte buffer, where the information about the error
	 *            message are next. <br />
	 *            Must be not null.
	 */
	private void errorMessageFromBytes(ByteBuffer buffer) {

		Preconditions.checkNotNull(buffer);

		byte[] messageBytes = new byte[buffer.getInt()];
		buffer.get(messageBytes);
		this.mMessage = new String(messageBytes);

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