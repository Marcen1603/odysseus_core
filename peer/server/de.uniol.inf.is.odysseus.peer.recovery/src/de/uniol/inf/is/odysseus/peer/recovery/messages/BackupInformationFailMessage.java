package de.uniol.inf.is.odysseus.peer.recovery.messages;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

import net.jxta.id.ID;
import net.jxta.id.IDFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(BackupInformationAckMessage.class);

	/**
	 * The id of the distributed query.
	 */
	private ID mSharedQueryID;

	/**
	 * The PQL code of the query part.
	 */
	private String mPQL;

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
		this.mSharedQueryID = info.getSharedQuery();
		this.mPQL = info.getPQL();

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
	 * Gets the id of the distributed query.
	 * 
	 * @return sharedQuery The id.
	 */
	public ID getSharedQueryID() {

		Preconditions.checkNotNull(this.mSharedQueryID);
		return this.mSharedQueryID;

	}

	/**
	 * Gets the PQL code of the query part.
	 * 
	 * @return The PQL code.
	 */
	public String getPQL() {

		Preconditions.checkNotNull(this.mPQL);
		return this.mPQL;

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

		Preconditions.checkNotNull(this.mSharedQueryID);
		Preconditions.checkNotNull(this.mPQL);

		int bufferSize = 0;

		byte[] sharedQueryBytes = this.determineSharedQueryBytes();
		bufferSize += 4 + sharedQueryBytes.length;

		byte[] pqlBytes = this.determinePQLBytes();
		bufferSize += 4 + pqlBytes.length;

		byte[] messageBytes = this.determineErrorMessageBytes();
		bufferSize += 4 + messageBytes.length;

		ByteBuffer buffer = ByteBuffer.allocate(bufferSize);

		buffer.putInt(sharedQueryBytes.length);
		buffer.put(sharedQueryBytes);

		buffer.putInt(pqlBytes.length);
		buffer.put(pqlBytes);

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
	 * Encodes the PQL code.
	 * 
	 * @return The bytes of {@link IRecoveryBackupInformation#getPQL()}.
	 */
	private byte[] determinePQLBytes() {

		Preconditions.checkNotNull(this.mPQL);
		return this.mPQL.getBytes();

	}

	/**
	 * Encodes the shared query id.
	 * 
	 * @return The bytes of {@link IRecoveryBackupInformation#getSharedQuery()}.
	 */
	private byte[] determineSharedQueryBytes() {

		Preconditions.checkNotNull(this.mSharedQueryID);
		return this.mSharedQueryID.toString().getBytes();

	}

	@Override
	public void fromBytes(byte[] data) {

		Preconditions.checkNotNull(data);
		ByteBuffer buffer = ByteBuffer.wrap(data);

		this.sharedQueryFromBytes(buffer);
		this.pqlFromBytes(buffer);
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
	 * Decodes the PQL code from bytes.
	 * 
	 * @param buffer
	 *            The current byte buffer, where the information about the PQL
	 *            code are next. <br />
	 *            Must be not null.
	 */
	private void pqlFromBytes(ByteBuffer buffer) {

		Preconditions.checkArgument(this.mPQL == null);
		Preconditions.checkNotNull(buffer);

		byte[] pqlBytes = new byte[buffer.getInt()];
		buffer.get(pqlBytes);
		this.mPQL = new String(pqlBytes);

	}

	/**
	 * Decodes the shared query id from bytes.
	 * 
	 * @param buffer
	 *            The current byte buffer, where the information about the
	 *            shared query id are next. <br />
	 *            Must be not null.
	 */
	private void sharedQueryFromBytes(ByteBuffer buffer) {

		Preconditions.checkArgument(this.mSharedQueryID == null);
		Preconditions.checkNotNull(buffer);
		byte[] idBytes = new byte[buffer.getInt()];
		buffer.get(idBytes);
		this.mSharedQueryID = toID(new String(idBytes));

	}

	/**
	 * Creates a shared query id from String.
	 * 
	 * @param text
	 *            The given String.
	 * @return The ID represented by <code>text</code> or null, if an error
	 *         occurs.
	 */
	private static ID toID(String text) {

		Preconditions.checkNotNull(text);

		try {

			final URI id = new URI(text);
			return IDFactory.fromURI(id);

		} catch (URISyntaxException | ClassCastException
				| IllegalArgumentException ex) {

			LOG.error("Could not get id from text {}", text, ex);
			return null;

		}

	}

}