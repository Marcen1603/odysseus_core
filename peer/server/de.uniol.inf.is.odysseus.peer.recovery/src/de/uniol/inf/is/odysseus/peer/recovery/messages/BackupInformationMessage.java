package de.uniol.inf.is.odysseus.peer.recovery.messages;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Map;

import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;

/**
 * The message for backup information for recovery. <br />
 * A shared query id and a collection of pql statements will be sent
 * (representing the backup information).
 * 
 * @author Michael Brand
 *
 */
public class BackupInformationMessage implements IMessage {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(BackupInformationMessage.class);

	/**
	 * A helper class to transform a pair of pql statement and allocated peer
	 * to/from byte array.
	 * 
	 * @author Michael Brand
	 *
	 */
	private class PartialBIMessage implements IMessage {

		/**
		 * The ID of the allocated peer.
		 */
		PeerID mPeerId;

		/**
		 * The pql statement to store.
		 */
		String mPqlStatement;

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

			byte[] peerIdBytes = this.mPeerId.toString().getBytes();
			byte[] pqlStatementBytes = this.mPqlStatement.getBytes();

			int bufferSize = INT_BUFFER_SIZE + peerIdBytes.length
					+ INT_BUFFER_SIZE + pqlStatementBytes.length;

			ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
			buffer.putInt(peerIdBytes.length);
			buffer.put(peerIdBytes);
			buffer.putInt(pqlStatementBytes.length);
			buffer.put(pqlStatementBytes);

			buffer.flip();
			return buffer.array();

		}

		@Override
		public void fromBytes(byte[] data) {

			ByteBuffer buffer = ByteBuffer.wrap(data);

			int peerIdLength = buffer.getInt();
			byte[] peerIdBytes = new byte[peerIdLength];
			buffer.get(peerIdBytes);
			this.mPeerId = toPeerID(new String(peerIdBytes));

			int pqlStatementLength = buffer.getInt();
			byte[] pqlStatement = new byte[pqlStatementLength];
			buffer.get(pqlStatement);
			this.mPqlStatement = new String(pqlStatement);

		}

	}

	/**
	 * The byte size for integers.
	 */
	private static final int INT_BUFFER_SIZE = 4;

	/**
	 * The list of backup information to send.
	 */
	private ImmutableMap<PeerID, Collection<String>> mBackupInformation;

	/**
	 * The number of backup information to send.
	 */
	private int mNumBackupInformation;

	/**
	 * The shared query id to send.
	 */
	private ID mSharedQueryID;

	/**
	 * Empty default constructor.
	 */
	public BackupInformationMessage() {

		// Empty construvtor

	}

	/**
	 * Creates a new message with a given shared query id and given backup
	 * information.
	 * 
	 * @param sharedQueryID
	 *            The given shared query id to send.
	 * @param backupInformation
	 *            The given backup information to send.
	 */
	public BackupInformationMessage(ID sharedQueryID,
			Map<PeerID, Collection<String>> backupInformation) {

		Preconditions.checkNotNull(sharedQueryID,
				"The shared query ID must be not null!");
		this.mSharedQueryID = sharedQueryID;

		Preconditions.checkNotNull(backupInformation,
				"The backup information must be not null!");
		Preconditions.checkArgument(!backupInformation.isEmpty(),
				"The backup information must be not empty!");
		this.mBackupInformation = ImmutableMap.copyOf(backupInformation);
		this.mNumBackupInformation = backupInformation.values().size();

	}

	@Override
	public byte[] toBytes() {

		byte[] sharedQueryIDBytes = mSharedQueryID.toString().getBytes();
		byte[][] backupInformationBytes = new byte[this.mNumBackupInformation][];
		int backupInformationCounter = 0;
		for (PeerID peerID : this.mBackupInformation.keySet()) {

			for (String pqlStatement : this.mBackupInformation.get(peerID)) {

				backupInformationBytes[backupInformationCounter++] = new PartialBIMessage(
						peerID, pqlStatement).toBytes();

			}

		}

		int bufferSize = INT_BUFFER_SIZE; // The number of backup information
		for (int index = 0; index < this.mNumBackupInformation; index++) {

			bufferSize += INT_BUFFER_SIZE
					+ backupInformationBytes[index].length;

		}
		bufferSize += INT_BUFFER_SIZE + sharedQueryIDBytes.length;

		ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
		buffer.putInt(this.mNumBackupInformation);
		for (int index = 0; index < this.mNumBackupInformation; index++) {

			buffer.putInt(backupInformationBytes[index].length);
			buffer.put(backupInformationBytes[index]);

		}
		buffer.putInt(sharedQueryIDBytes.length);
		buffer.put(sharedQueryIDBytes);

		buffer.flip();
		return buffer.array();

	}

	@Override
	public void fromBytes(byte[] data) {

		ByteBuffer buffer = ByteBuffer.wrap(data);

		this.mNumBackupInformation = buffer.getInt();

		Map<PeerID, Collection<String>> backupInformation = Maps.newHashMap();
		for (int index = 0; index < this.mNumBackupInformation; index++) {

			int backupInformationLength = buffer.getInt();
			byte[] backupInformationBytes = new byte[backupInformationLength];
			buffer.get(backupInformationBytes);
			PartialBIMessage message = new PartialBIMessage();
			message.fromBytes(backupInformationBytes);
			if (backupInformation.containsKey(message.mPeerId)) {

				backupInformation.get(message.mPeerId).add(
						message.mPqlStatement);

			} else {

				Collection<String> pqlStatements = Lists
						.newArrayList(message.mPqlStatement);
				backupInformation.put(message.mPeerId, pqlStatements);

			}

		}
		this.mBackupInformation = ImmutableMap.copyOf(backupInformation);

		int sharedQueryIDLength = buffer.getInt();
		byte[] sharedQueryIDBytes = new byte[sharedQueryIDLength];
		buffer.get(sharedQueryIDBytes);
		this.mSharedQueryID = toID(new String(sharedQueryIDBytes));

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

	/**
	 * Gets the sent backup information.
	 */
	public ImmutableMap<PeerID, Collection<String>> geBackupInformation() {

		return this.mBackupInformation;

	}

	/**
	 * Gets the sent shared query id.
	 */
	public ID getSharedQueryID() {

		return this.mSharedQueryID;

	}

}