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
 * Class of messages to request the state of a shared query.
 * 
 * @author Michael Brand
 */
public class RequestQueryStateMessage implements IMessage {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(RequestQueryStateMessage.class);

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
	 * The shared query.
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
	 * Empty default constructor.
	 */
	public RequestQueryStateMessage() {
		// Empty default constructor.
	}

	/**
	 * Creates a new query state request message.
	 * 
	 * @param sharedQuery
	 *            The shared query. <br />
	 *            Must be not null.
	 */
	public RequestQueryStateMessage(ID sharedQuery) {
		Preconditions.checkNotNull(sharedQuery);

		this.mSharedQuery = sharedQuery;
	}

	@Override
	public byte[] toBytes() {
		byte[] idBytes = this.mID.toString().getBytes();
		byte[] sharedQueryBytes = this.mSharedQuery.toString().getBytes();

		int bufferSize = 4 + idBytes.length + 4 + sharedQueryBytes.length;
		ByteBuffer buffer = ByteBuffer.allocate(bufferSize);

		buffer.putInt(idBytes.length);
		buffer.put(idBytes);
		buffer.putInt(sharedQueryBytes.length);
		buffer.put(sharedQueryBytes);

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
			int sharedQueryBytesLength = buffer.getInt();
			byte[] sharedQueryBytes = new byte[sharedQueryBytesLength];
			buffer.get(sharedQueryBytes);
			URI sharedQueryURI = new URI(new String(sharedQueryBytes));
			this.mSharedQuery = ID.create(sharedQueryURI);
		} catch (URISyntaxException e) {
			LOG.error("Could not create shared query id from bytes!", e);
		}
	}

}