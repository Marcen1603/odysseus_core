package de.uniol.inf.is.odysseus.peer.recovery.messages;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

import net.jxta.id.ID;
import net.jxta.impl.id.UUID.UUID;
import net.jxta.impl.id.UUID.UUIDFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;

/**
 * Class of messages to send an add query instruction.
 * 
 * @author Michael Brand, Tobias Brandt
 */
public class RecoveryAddQueryMessage implements IMessage {

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
	 * The PQL code to execute.
	 */
	private String mPQL;

	/**
	 * The PQL code to execute.
	 * 
	 * @return A valid PQL code.
	 */
	public String getPQLCode() {
		return this.mPQL;
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
	 * The id of the recovery process.
	 */
	private java.util.UUID mProcessId;

	/**
	 * The id of the recovery process.
	 * 
	 * @return A unique identifier.
	 */
	public java.util.UUID getRecoveryProcessId() {
		return this.mProcessId;
	}

	/**
	 * Empty default constructor.
	 */
	public RecoveryAddQueryMessage() {
		// Empty default constructor.
	}

	/**
	 * Creates a new recovery add query message.
	 * 
	 * @param pqls
	 *            The PQL code to execute. <br />
	 *            Must be not null.
	 * @param sharedQuery
	 *            The affected shared query. <br />
	 *            Must be not null.
	 * @param processId
	 *            The id of the recovery process. <br />
	 *            Must be not null,
	 */
	public RecoveryAddQueryMessage(String pql, ID sharedQuery,
			java.util.UUID processId) {
		Preconditions.checkNotNull(pql);
		Preconditions.checkNotNull(sharedQuery);
		Preconditions.checkNotNull(processId);

		this.mPQL = pql;
		this.mSharedQuery = sharedQuery;
		this.mProcessId = processId;
	}

	@Override
	public byte[] toBytes() {
		byte[] idBytes = this.mID.toString().getBytes();
		byte[] pqlBytes = this.mPQL.getBytes();
		byte[] sharedQueryBytes = this.mSharedQuery.toString().getBytes();
		byte[] processIdBytes = this.mProcessId.toString().getBytes();

		int bufferSize = 4 + idBytes.length + 4 + pqlBytes.length + 4
				+ sharedQueryBytes.length + 4 + processIdBytes.length;
		ByteBuffer buffer = ByteBuffer.allocate(bufferSize);

		buffer.putInt(idBytes.length);
		buffer.put(idBytes);
		buffer.putInt(pqlBytes.length);
		buffer.put(pqlBytes);
		buffer.putInt(sharedQueryBytes.length);
		buffer.put(sharedQueryBytes);
		buffer.putInt(processIdBytes.length);
		buffer.put(processIdBytes);

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

		int pqlBytesLength = buffer.getInt();
		byte[] pqlBytes = new byte[pqlBytesLength];
		buffer.get(pqlBytes);
		this.mPQL = new String(pqlBytes);

		try {
			int sharedQueryBytesLength = buffer.getInt();
			byte[] sharedQueryBytes = new byte[sharedQueryBytesLength];
			buffer.get(sharedQueryBytes);
			URI sharedQueryURI = new URI(new String(sharedQueryBytes));
			this.mSharedQuery = ID.create(sharedQueryURI);
		} catch (URISyntaxException e) {
			LOG.error("Could not create shared query id from bytes!", e);
		}
		
		int processIdBytesLength = buffer.getInt();
		byte[] processIdBytes = new byte[processIdBytesLength];
		buffer.get(processIdBytes);
		this.mProcessId = java.util.UUID.fromString(new String(processIdBytes));
	}

}