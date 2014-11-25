package de.uniol.inf.is.odysseus.peer.recovery.messages;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Iterator;

import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import net.jxta.impl.id.UUID.UUID;
import net.jxta.impl.id.UUID.UUIDFactory;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryBackupInformation;
import de.uniol.inf.is.odysseus.peer.recovery.internal.BackupInformation;

/**
 * The message to send backup information from one peer to another.
 * 
 * @see IRecoveryBackupInformation
 * @author Michael Brand
 *
 */
public class BackupInformationMessage implements IMessage {

	/**
	 * Update information will only be saved on the peer which receives this
	 * message if he already had this information (for this peer for this
	 * sharedQueryId)
	 */
	public final static int UPDATE_INFO = 0;

	/**
	 * This type of information will be saved on the receiving peer
	 */
	public final static int NEW_INFO = 1;

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(BackupInformationMessage.class);
	
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
	 * The backup information, which has to be sent or which has received.
	 */
	private IRecoveryBackupInformation mInfo = new BackupInformation();

	/**
	 * Either {@link #NEW_INFO} or {@link #UPDATE_INFO}.
	 */
	private int mMessageType;

	/**
	 * Empty default constructor.
	 */
	public BackupInformationMessage() {

		// Empty default constructor.

	}

	/**
	 * Creates a new backup information message.
	 * 
	 * @param info
	 *            The information to send. <br />
	 *            Must be not null.
	 */
	public BackupInformationMessage(IRecoveryBackupInformation info,
			int messageType) {
		Preconditions.checkNotNull(info);
		this.mInfo = info;
		this.mMessageType = messageType;
	}

	/**
	 * Gets the backup information, which has to be sent or which has received.
	 * 
	 * @return The backup information, which has to be sent or which has
	 *         received.
	 */
	public IRecoveryBackupInformation getInfo() {

		Preconditions.checkNotNull(this.mInfo);
		return this.mInfo;

	}

	public int getMessageType() {
		return this.mMessageType;
	}

	@Override
	public byte[] toBytes() {

		Preconditions.checkNotNull(this.mInfo);

		int bufferSize = 4; // type of info (new or update)

		byte[] sharedQueryBytes = this.determineSharedQueryBytes();
		bufferSize += 4 + sharedQueryBytes.length;

		byte[] pqlBytes = this.determinePQLBytes();
		bufferSize += 4 + pqlBytes.length;

		byte[] peerBytes = this.determinePeerBytes();
		bufferSize += 4 + peerBytes.length;

		byte[][] subsequentPartsInfoBytes = this
				.determineSubsequentPartsInfoBytes();
		bufferSize += 4;
		for (int i = 0; i < subsequentPartsInfoBytes.length; i++) {

			bufferSize += 4 + subsequentPartsInfoBytes[i].length;

		}

		byte[] localPqlBytes = this.determineLocalPQLBytes();
		bufferSize += 4 + localPqlBytes.length;

		byte[] locationPeerBytes = this.determineLocationPeerBytes();
		bufferSize += 4 + locationPeerBytes.length;

		byte[] idBytes = this.determineIdBytes();
		bufferSize += 4 + idBytes.length;

		ByteBuffer buffer = ByteBuffer.allocate(bufferSize);

		buffer.putInt(mMessageType);

		buffer.putInt(sharedQueryBytes.length);
		buffer.put(sharedQueryBytes);

		buffer.putInt(pqlBytes.length);
		buffer.put(pqlBytes);

		buffer.putInt(peerBytes.length);
		buffer.put(peerBytes);

		buffer.putInt(subsequentPartsInfoBytes.length);
		for (int i = 0; i < subsequentPartsInfoBytes.length; i++) {

			buffer.putInt(subsequentPartsInfoBytes[i].length);
			buffer.put(subsequentPartsInfoBytes[i]);

		}

		buffer.putInt(localPqlBytes.length);
		buffer.put(localPqlBytes);

		buffer.putInt(locationPeerBytes.length);
		buffer.put(locationPeerBytes);

		buffer.putInt(idBytes.length);
		buffer.put(idBytes);

		buffer.flip();
		return buffer.array();

	}

	/**
	 * Encodes the id.
	 * 
	 * @return The bytes of {@link IRecoveryBackupInformation#getUUID()}.
	 */
	private byte[] determineIdBytes() {

		Preconditions.checkNotNull(this.mInfo);
		return this.mID.toString().getBytes();

	}

	/**
	 * Encodes the local PQL code.
	 * 
	 * @return The bytes of {@link IRecoveryBackupInformation#getLocalPQL()}.
	 */
	private byte[] determineLocalPQLBytes() {

		Preconditions.checkNotNull(this.mInfo);
		if (this.mInfo.getLocalPQL() != null)
			return this.mInfo.getLocalPQL().getBytes();
		return "".getBytes();

	}

	/**
	 * Encodes the peer id "locationPeer"
	 * 
	 * @return The bytes of {@link IRecoveryBackupInformation#getLocationPeer()}
	 *         .
	 */
	private byte[] determineLocationPeerBytes() {
		Preconditions.checkNotNull(this.mInfo);
		if (this.mInfo.getLocationPeer() != null)
			return this.mInfo.getLocationPeer().toString().getBytes();
		return new byte[0];
	}

	/**
	 * Encodes the peer id.
	 * 
	 * @return The bytes of {@link IRecoveryBackupInformation#getAboutPeer()}.
	 */
	private byte[] determinePeerBytes() {

		Preconditions.checkNotNull(this.mInfo);
		if (this.mInfo.getAboutPeer() != null)
			return this.mInfo.getAboutPeer().toString().getBytes();

		byte[] empty = new byte[0];
		return empty;

	}

	/**
	 * Encodes the PQL code.
	 * 
	 * @return The bytes of {@link IRecoveryBackupInformation#getPQL()}.
	 */
	private byte[] determinePQLBytes() {

		Preconditions.checkNotNull(this.mInfo);
		if (this.mInfo.getPQL() != null)
			return this.mInfo.getPQL().getBytes();

		return "".getBytes();

	}

	/**
	 * Encodes the information about subsequent parts.
	 * 
	 * @return The bytes of
	 *         {@link IRecoveryBackupInformation#getSubsequentPartsInformation()}
	 *         as an array.
	 */
	private byte[][] determineSubsequentPartsInfoBytes() {

		Preconditions.checkNotNull(this.mInfo);
		byte[][] subsequentPartsInfoBytes = new byte[this.mInfo
				.getSubsequentPartsInformation().size()][];

		int i = 0;
		Iterator<IRecoveryBackupInformation> iter = this.mInfo
				.getSubsequentPartsInformation().iterator();
		while (iter.hasNext()) {

			subsequentPartsInfoBytes[i] = new BackupInformationMessage(
					iter.next(), this.mMessageType).toBytes();
			i++;

		}

		return subsequentPartsInfoBytes;

	}

	/**
	 * Encodes the shared query id.
	 * 
	 * @return The bytes of {@link IRecoveryBackupInformation#getSharedQuery()}.
	 */
	private byte[] determineSharedQueryBytes() {

		Preconditions.checkNotNull(this.mInfo);
		return this.mInfo.getSharedQuery().toString().getBytes();

	}

	@Override
	public void fromBytes(byte[] data) {

		Preconditions.checkNotNull(data);
		ByteBuffer buffer = ByteBuffer.wrap(data);
		this.mMessageType = buffer.getInt();

		this.sharedQueryFromBytes(buffer);
		this.pqlFromBytes(buffer);
		this.peerFromBytes(buffer);
		this.subsequentPartsInfoFromBytes(buffer);
		this.localPqlFromBytes(buffer);
		this.locationPeerFromBytes(buffer);
		this.idFromBytes(buffer);

	}

	/**
	 * Decodes the peer from bytes.
	 * 
	 * @param buffer
	 *            The current byte buffer, where the information about the peer
	 *            are next. <br />
	 *            Must be not null.
	 */
	private void peerFromBytes(ByteBuffer buffer) {

		Preconditions.checkArgument(this.mInfo.getAboutPeer() == null);
		Preconditions.checkNotNull(buffer);
		int peerByteSize = buffer.getInt();
		if (peerByteSize > 0) {
			byte[] peerBytes = new byte[peerByteSize];
			buffer.get(peerBytes);
			this.mInfo.setAboutPeer(toPeerID(new String(peerBytes)));
		}
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

		Preconditions.checkArgument(this.mInfo.getPQL() == null);
		Preconditions.checkNotNull(buffer);
		byte[] pqlBytes = new byte[buffer.getInt()];
		buffer.get(pqlBytes);
		this.mInfo.setPQL(new String(pqlBytes));

	}

	/**
	 * Decodes the local PQL code from bytes.
	 * 
	 * @param buffer
	 *            The current byte buffer, where the information about the local
	 *            PQL code are next. <br />
	 *            Must be not null.
	 */
	private void localPqlFromBytes(ByteBuffer buffer) {

		Preconditions.checkArgument(this.mInfo.getLocalPQL() == null);
		Preconditions.checkNotNull(buffer);
		byte[] pqlBytes = new byte[buffer.getInt()];
		buffer.get(pqlBytes);
		this.mInfo.setLocalPQL(new String(pqlBytes));

	}

	/**
	 * Decodes the information about subsequent parts from bytes.
	 * 
	 * @param buffer
	 *            The current byte buffer, where the information about
	 *            subsequent parts are next. <br />
	 *            Must be not null.
	 */
	private void subsequentPartsInfoFromBytes(ByteBuffer buffer) {

		Preconditions.checkArgument(this.mInfo.getSubsequentPartsInformation()
				.isEmpty());
		Preconditions.checkNotNull(buffer);
		Collection<IRecoveryBackupInformation> info = Sets.newHashSet();
		int numInfo = buffer.getInt();

		for (int i = 0; i < numInfo; i++) {

			byte[] bytes = new byte[buffer.getInt()];
			buffer.get(bytes);

			BackupInformationMessage message = new BackupInformationMessage();
			message.fromBytes(bytes);
			info.add(message.getInfo());

		}

		this.mInfo.setSubsequentPartsInformation(info);

	}

	/**
	 * Decodes the id from bytes.
	 * 
	 * @param buffer
	 *            The current byte buffer, where the information about the UUID
	 *            are next. <br />
	 *            Must be not null.
	 */
	private void idFromBytes(ByteBuffer buffer) {

		Preconditions.checkNotNull(buffer);
		byte[] idBytes = new byte[buffer.getInt()];
		buffer.get(idBytes);
		this.mID = toUUID(new String(idBytes));

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

		Preconditions.checkArgument(this.mInfo.getSharedQuery() == null);
		Preconditions.checkNotNull(buffer);
		byte[] idBytes = new byte[buffer.getInt()];
		buffer.get(idBytes);
		this.mInfo.setSharedQuery(toID(new String(idBytes)));

	}

	private void locationPeerFromBytes(ByteBuffer buffer) {
		Preconditions.checkArgument(this.mInfo.getLocationPeer() == null);
		Preconditions.checkNotNull(buffer);
		int locationPeerSize = buffer.getInt();
		if (locationPeerSize > 0) {
			byte[] locationPeerBytes = new byte[locationPeerSize];
			buffer.get(locationPeerBytes);
			this.mInfo.setLocationPeer(toPeerID(new String(locationPeerBytes)));
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

	/**
	 * Creates a UUID from String.
	 * 
	 * @param text
	 *            The given String.
	 * @return The UUID represented by <code>text</code> or null, if an error
	 *         occurs.
	 */
	private static UUID toUUID(String text) {

		Preconditions.checkNotNull(text);
		return new UUID(text);

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

		Preconditions.checkNotNull(text);

		try {

			final URI id = new URI(text);
			return PeerID.create(id);

		} catch (URISyntaxException | ClassCastException
				| IllegalArgumentException ex) {

			LOG.error("Could not get id from text {}", text, ex);
			return null;

		}

	}

}