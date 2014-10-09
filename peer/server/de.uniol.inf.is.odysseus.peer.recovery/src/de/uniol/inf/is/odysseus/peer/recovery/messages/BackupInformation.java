package de.uniol.inf.is.odysseus.peer.recovery.messages;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.Map;

import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;

/**
 * The backup information for a shared query is a PQL statement, an ID of the
 * peer, where that statement is installed and a list of all subsequent query
 * parts.
 * 
 * @author Michael Brand
 *
 */
public final class BackupInformation implements IMessage {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(BackupInformation.class);

	/**
	 * The byte size for integers.
	 */
	private static final int INT_BUFFER_SIZE = 4;

	/**
	 * A helper class to code and decode a pair of PQL statement and peer ID.
	 * 
	 * @author Michael Brand
	 *
	 */
	private class PartialBIMessage implements IMessage {

		/**
		 * The PQL statement to store.
		 */
		String mPqlStatement;

		/**
		 * The ID of the allocated peer.
		 */
		PeerID mPeerId;

		/**
		 * Empty default constructor.
		 */
		public PartialBIMessage() {

			// Empty default constructor.

		}

		/**
		 * Creates a new helper message.
		 * 
		 * @param peerId
		 *            The ID of the allocated peer.
		 * @param pqlStatement
		 *            The pql statement to store.
		 */
		public PartialBIMessage(PeerID peerId, String pqlStatement) {

			this.mPeerId = peerId;
			this.mPqlStatement = pqlStatement;

		}

		@Override
		public byte[] toBytes() {

			byte[] pqlStatementBytes = this.mPqlStatement.getBytes();
			byte[] peerIdBytes = this.mPeerId.toString().getBytes();

			int bufferSize = INT_BUFFER_SIZE + pqlStatementBytes.length
					+ INT_BUFFER_SIZE + peerIdBytes.length;

			ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
			buffer.putInt(pqlStatementBytes.length);
			buffer.put(pqlStatementBytes);
			buffer.putInt(peerIdBytes.length);
			buffer.put(peerIdBytes);

			buffer.flip();
			return buffer.array();

		}

		@Override
		public void fromBytes(byte[] data) {

			ByteBuffer buffer = ByteBuffer.wrap(data);

			int pqlStatementLength = buffer.getInt();
			byte[] pqlStatement = new byte[pqlStatementLength];
			buffer.get(pqlStatement);
			this.mPqlStatement = new String(pqlStatement);

			int peerIdLength = buffer.getInt();
			byte[] peerIdBytes = new byte[peerIdLength];
			buffer.get(peerIdBytes);
			this.mPeerId = toPeerID(new String(peerIdBytes));

		}

	}

	/**
	 * The ID of the distributed query.
	 */
	private ID mSharedQueryId;

	/**
	 * The stored PQL statement.
	 */
	private String mPqlStatement;

	/**
	 * The ID of the peer, where the PQL statement is installed.
	 */
	private PeerID mPeerId;

	/**
	 * The subsequent parts for the PQL statement.
	 */
	private final Map<String, PeerID> mSubsequentParts = Maps.newHashMap();

	/**
	 * Empty default constructor.
	 */
	public BackupInformation() {

		// Nothing to do.

	}

	/**
	 * Creates new backup information about a given PQL statement.
	 * 
	 * @param sharedQueryId
	 *            The ID of the distributed query. <br />
	 *            Must be not null!
	 * @param pqlStatement
	 *            The PQL statement to store. <br />
	 *            Must be not null.
	 */
	public BackupInformation(ID sharedQueryId, String pqlStatement) {

		Preconditions.checkNotNull(sharedQueryId,
				"The shared query ID must be not null!");
		this.mSharedQueryId = sharedQueryId;

		Preconditions.checkNotNull(pqlStatement,
				"The pql statement must be not null!");
		this.mPqlStatement = pqlStatement;

	}

	/**
	 * The ID of the distributed query.
	 */
	public ID getSharedQueryID() {

		return this.mSharedQueryId;

	}

	/**
	 * The stored PQL statement.
	 */
	public String getPQLStatement() {

		return this.mPqlStatement;

	}

	/**
	 * Sets the ID of the peer, where the PQL statement is installed. <br />
	 * <code>peerId</code> must be not null!
	 */
	public void setPeer(PeerID peerId) {

		Preconditions.checkNotNull(peerId, "The peer ID must be not null!");
		this.mPeerId = peerId;

	}

	/**
	 * The ID of the peer, where the PQL statement is installed.
	 */
	public PeerID getPeer() {

		return this.mPeerId;

	}

	/**
	 * Adds a subsequent part for the PQL statement.
	 * 
	 * @param pqlStatement
	 *            The PQL statement of the subsequent part. <br />
	 *            Must be not null!
	 * @param peer
	 *            The ID of the peer, where the subsequent PQL statement is
	 *            installed. <br />
	 *            Must be not null!
	 */
	public void addSubsequentPart(String pqlStatement, PeerID peer) {

		Preconditions.checkNotNull(pqlStatement,
				"The pql statement must be not null!");
		Preconditions.checkNotNull(peer, "The peer ID must be not null!");
		this.mSubsequentParts.put(pqlStatement, peer);

	}

	/**
	 * Removes a subsequent part for the PQL statement.
	 * 
	 * @param pqlStatement
	 *            The PQL statement of the subsequent part. <br />
	 *            Must be not null!
	 */
	public void removeSubsequentPart(String pqlStatement) {

		Preconditions.checkNotNull(pqlStatement,
				"The pql statement must be not null!");
		this.mSubsequentParts.remove(pqlStatement);

	}

	/**
	 * The subsequent parts of the PQL statement.
	 * 
	 * @return The PQL statements of the subsequent parts and the IDs of the
	 *         peers, where the subsequent PQL statements are installed.
	 */
	public ImmutableMap<String, PeerID> getSubsequentParts() {

		return ImmutableMap.copyOf(this.mSubsequentParts);

	}

	@Override
	public String toString() {

		StringBuffer buffer = new StringBuffer();

		buffer.append("Shared query: ");
		buffer.append(this.mSharedQueryId);
		buffer.append("\n");

		buffer.append("PQL statement: ");
		buffer.append(this.mPqlStatement);
		buffer.append("\n");

		buffer.append("Peer ID: ");
		buffer.append(this.mPeerId);
		buffer.append("\n");

		buffer.append("Subsequent parts: ");
		buffer.append(this.mSubsequentParts);

		return buffer.toString();

	}

	@Override
	public byte[] toBytes() {

		int numSubsequentParts = this.mSubsequentParts.keySet().size();

		byte[] sharedQueryIdBytes = this.mSharedQueryId.toString().getBytes();
		byte[] pqlStatementBytes = this.mPqlStatement.getBytes();
		byte[] peerIdBytes = this.mPeerId.toString().getBytes();
		byte[][] subsequentPartsBytes = new byte[numSubsequentParts][];
		int index = 0;
		for (String pqlStatement : this.mSubsequentParts.keySet()) {

			subsequentPartsBytes[index++] = new PartialBIMessage(
					this.mSubsequentParts.get(pqlStatement), pqlStatement)
					.toBytes();

		}

		int bufferSize = INT_BUFFER_SIZE + sharedQueryIdBytes.length
				+ INT_BUFFER_SIZE + pqlStatementBytes.length + INT_BUFFER_SIZE
				+ peerIdBytes.length + INT_BUFFER_SIZE;
		for (byte[] subPartBytes : subsequentPartsBytes) {

			bufferSize += INT_BUFFER_SIZE + subPartBytes.length;

		}

		ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
		buffer.putInt(sharedQueryIdBytes.length);
		buffer.put(sharedQueryIdBytes);
		buffer.putInt(pqlStatementBytes.length);
		buffer.put(pqlStatementBytes);
		buffer.putInt(peerIdBytes.length);
		buffer.put(peerIdBytes);
		buffer.putInt(numSubsequentParts);
		for (byte[] subPartBytes : subsequentPartsBytes) {

			buffer.putInt(subPartBytes.length);
			buffer.put(subPartBytes);

		}

		buffer.flip();
		return buffer.array();

	}

	@Override
	public void fromBytes(byte[] data) {

		ByteBuffer buffer = ByteBuffer.wrap(data);

		int sharedQueryIdLength = buffer.getInt();
		byte[] sharedQueryIdBytes = new byte[sharedQueryIdLength];
		buffer.get(sharedQueryIdBytes);
		this.mSharedQueryId = toID(new String(sharedQueryIdBytes));

		int pqlStatementLength = buffer.getInt();
		byte[] pqlStatementBytes = new byte[pqlStatementLength];
		buffer.get(pqlStatementBytes);
		this.mPqlStatement = new String(pqlStatementBytes);

		int peerIdLength = buffer.getInt();
		byte[] peerIdBytes = new byte[peerIdLength];
		buffer.get(peerIdBytes);
		this.mPeerId = toPeerID(new String(peerIdBytes));

		int numSubsequentParts = buffer.getInt();
		for (int index = 0; index < numSubsequentParts; index++) {

			int partialBiMessageLength = buffer.getInt();
			byte[] partialBiMessageBytes = new byte[partialBiMessageLength];
			buffer.get(partialBiMessageBytes);
			PartialBIMessage partialBiMessage = new PartialBIMessage();
			partialBiMessage.fromBytes(partialBiMessageBytes);
			this.mSubsequentParts.put(partialBiMessage.mPqlStatement,
					partialBiMessage.mPeerId);

		}

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

		try {

			final URI id = new URI(text);
			return IDFactory.fromURI(id);

		} catch (URISyntaxException | ClassCastException ex) {

			LOG.error("Could not get id from text {}", text, ex);
			return null;

		}

	}

	/**
	 * Creates a peer id from String.
	 * 
	 * @param text
	 *            The given String.
	 * @return The peer ID represented by <code>text</code> or null, if an error
	 *         occurs.
	 */
	private static PeerID toPeerID(String text) {

		try {

			final URI id = new URI(text);
			return PeerID.create(id);

		} catch (URISyntaxException | ClassCastException ex) {

			LOG.error("Could not get id from text {}", text, ex);
			return null;

		}

	}

}