package de.uniol.inf.is.odysseus.peer.recovery.messages;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Iterator;

import net.jxta.id.ID;
import net.jxta.id.IDFactory;
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
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(BackupInformationMessage.class);

	/**
	 * The backup information, which has to be sent or which has received.
	 */
	private IRecoveryBackupInformation mInfo = new BackupInformation();

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
	public BackupInformationMessage(IRecoveryBackupInformation info) {

		Preconditions.checkNotNull(info);
		this.mInfo = info;

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

	@Override
	public byte[] toBytes() {

		Preconditions.checkNotNull(this.mInfo);

		int bufferSize = 0;

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

		ByteBuffer buffer = ByteBuffer.allocate(bufferSize);

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

		buffer.flip();
		return buffer.array();

	}

	/**
	 * Encodes the peer id.
	 * 
	 * @return The bytes of {@link IRecoveryBackupInformation#getPeer()}.
	 */
	private byte[] determinePeerBytes() {

		Preconditions.checkNotNull(this.mInfo);
		return this.mInfo.getPeer().toString().getBytes();

	}

	/**
	 * Encodes the PQL code.
	 * 
	 * @return The bytes of {@link IRecoveryBackupInformation#getPQL()}.
	 */
	private byte[] determinePQLBytes() {

		Preconditions.checkNotNull(this.mInfo);
		return this.mInfo.getPQL().getBytes();

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
					iter.next()).toBytes();
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

		this.sharedQueryFromBytes(buffer);
		this.pqlFromBytes(buffer);
		this.peerFromBytes(buffer);
		this.subsequentPartsInfoFromBytes(buffer);

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

		Preconditions.checkArgument(this.mInfo.getPeer() == null);
		Preconditions.checkNotNull(buffer);
		byte[] peerBytes = new byte[buffer.getInt()];
		buffer.get(peerBytes);
		this.mInfo.setPeer(toPeerID(new String(peerBytes)));

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

		Preconditions.checkNotNull(text);

		try {

			final URI id = new URI(text);
			return PeerID.create(id);

		} catch (URISyntaxException | ClassCastException ex) {

			LOG.error("Could not get id from text {}", text, ex);
			return null;

		}

	}

}