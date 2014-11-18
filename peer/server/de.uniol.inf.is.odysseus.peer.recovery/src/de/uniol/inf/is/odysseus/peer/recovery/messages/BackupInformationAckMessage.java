package de.uniol.inf.is.odysseus.peer.recovery.messages;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

import net.jxta.id.ID;
import net.jxta.id.IDFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		this.mSharedQueryID = info.getSharedQuery();
		this.mPQL = info.getPQL();

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

	@Override
	public byte[] toBytes() {

		Preconditions.checkNotNull(this.mSharedQueryID);
		Preconditions.checkNotNull(this.mPQL);

		int bufferSize = 0;

		byte[] sharedQueryBytes = this.determineSharedQueryBytes();
		bufferSize += 4 + sharedQueryBytes.length;

		byte[] pqlBytes = this.determinePQLBytes();
		bufferSize += 4 + pqlBytes.length;

		ByteBuffer buffer = ByteBuffer.allocate(bufferSize);

		buffer.putInt(sharedQueryBytes.length);
		buffer.put(sharedQueryBytes);

		buffer.putInt(pqlBytes.length);
		buffer.put(pqlBytes);

		buffer.flip();
		return buffer.array();

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