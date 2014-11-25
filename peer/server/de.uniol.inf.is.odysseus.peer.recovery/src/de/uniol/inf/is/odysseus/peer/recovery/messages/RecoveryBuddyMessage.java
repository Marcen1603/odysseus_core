package de.uniol.inf.is.odysseus.peer.recovery.messages;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.List;

import net.jxta.id.ID;
import net.jxta.impl.id.UUID.UUID;
import net.jxta.impl.id.UUID.UUIDFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;

/**
 * Class of messages to send buddy information.
 * 
 * @author Michael Brand, Tobias Brandt
 */
public class RecoveryBuddyMessage implements IMessage {

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
	 * The PQL codes to backup.
	 */
	private List<String> mPQLs;

	/**
	 * The PQL codes to backup.
	 * 
	 * @return A collection of PQL codes.
	 */
	public ImmutableList<String> getPQLCodes() {
		return ImmutableList.copyOf(this.mPQLs);
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
	 * Empty default constructor.
	 */
	public RecoveryBuddyMessage() {
		// Empty default constructor.
	}

	/**
	 * Creates a new recovery buddy message.
	 * 
	 * @param pqls
	 *            The PQL codes to backup. <br />
	 *            Must be not null.
	 * @param sharedQuery
	 *            The affected shared query. <br />
	 *            Must be not null.
	 */
	public RecoveryBuddyMessage(List<String> pqls, ID sharedQuery) {
		Preconditions.checkNotNull(pqls);
		Preconditions.checkNotNull(sharedQuery);

		this.mPQLs = pqls;
		this.mSharedQuery = sharedQuery;
	}

	@Override
	public byte[] toBytes() {
		byte[] idBytes = this.mID.toString().getBytes();
		byte[] sharedQueryBytes = this.mSharedQuery.toString().getBytes();
		int bufferSizePQLs = 4; // first size for number of PQL codes
		byte[][] pqlBytes = new byte[this.mPQLs.size()][];
		for (int i = 0; i < this.mPQLs.size(); i++) {
			String pql = this.mPQLs.get(i);
			pqlBytes[i] = pql.getBytes();
			bufferSizePQLs += 4 + pqlBytes[i].length;
		}

		int bufferSize = 4 + idBytes.length + 4 + sharedQueryBytes.length
				+ bufferSizePQLs;
		ByteBuffer buffer = ByteBuffer.allocate(bufferSize);

		buffer.putInt(idBytes.length);
		buffer.put(idBytes);
		buffer.putInt(sharedQueryBytes.length);
		buffer.put(sharedQueryBytes);
		buffer.putInt(pqlBytes.length);
		for (int i = 0; i < this.mPQLs.size(); i++) {
			buffer.putInt(pqlBytes[i].length);
			buffer.put(pqlBytes[i]);
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

		try {
			int sharedQueryBytesLength = buffer.getInt();
			byte[] sharedQueryBytes = new byte[sharedQueryBytesLength];
			buffer.get(sharedQueryBytes);
			URI sharedQueryURI = new URI(new String(sharedQueryBytes));
			this.mSharedQuery = ID.create(sharedQueryURI);
		} catch (URISyntaxException e) {
			LOG.error("Could not create shared query id from bytes!", e);
		}

		List<String> pqlCodes = Lists.newArrayList();
		int numPQLCodes = buffer.getInt();
		for (int i = 0; i < numPQLCodes; i++) {
			int pqlBytesLength = buffer.getInt();
			byte[] pqlBytes = new byte[pqlBytesLength];
			buffer.get(pqlBytes);
			pqlCodes.add(new String(pqlBytes));
		}
		this.mPQLs = pqlCodes;
	}

}