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

import de.uniol.inf.is.odysseus.core.collection.IPair;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryBackupInformation;
import de.uniol.inf.is.odysseus.peer.recovery.internal.BackupInformation;

public class BackupInformationMessage implements IMessage {

	private static final Logger LOG = LoggerFactory
			.getLogger(BackupInformationMessage.class);

	private IRecoveryBackupInformation mInfo;

	public BackupInformationMessage() {

		this.mInfo = new BackupInformation();

	}

	public BackupInformationMessage(IRecoveryBackupInformation info) {

		Preconditions.checkNotNull(info);
		this.mInfo = info;

	}

	public IRecoveryBackupInformation getInfo() {

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

		byte[][][] subsequentPartsInfoBytes = this
				.determineSubsequentPartsInfoBytes();
		bufferSize += 4;
		for (int i = 0; i < subsequentPartsInfoBytes.length; i++) {

			for (int j = 0; j < subsequentPartsInfoBytes[i].length; j++) {

				bufferSize += 4 + subsequentPartsInfoBytes[i][j].length;

			}

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

			for (int j = 0; j < subsequentPartsInfoBytes[i].length; j++) {

				buffer.putInt(subsequentPartsInfoBytes[i][j].length);
				buffer.put(subsequentPartsInfoBytes[i][j]);

			}

		}

		buffer.flip();
		return buffer.array();

	}

	private byte[] determinePeerBytes() {

		return this.mInfo.getPeer().toString().getBytes();

	}

	private byte[] determinePQLBytes() {

		return this.mInfo.getPQL().getBytes();

	}

	private byte[][][] determineSubsequentPartsInfoBytes() {

		byte[][][] subsequentPartsInfoBytes = new byte[this.mInfo
				.getSubsequentPartsInformation().size()][][];

		int i = 0;
		Iterator<IPair<String, PeerID>> iter = this.mInfo
				.getSubsequentPartsInformation().iterator();
		while (iter.hasNext()) {

			IPair<String, PeerID> pair = iter.next();
			subsequentPartsInfoBytes[i] = new byte[2][];
			subsequentPartsInfoBytes[i][0] = pair.getE1().getBytes();
			subsequentPartsInfoBytes[i][1] = pair.getE2().toString().getBytes();
			i++;

		}

		return subsequentPartsInfoBytes;

	}

	private byte[] determineSharedQueryBytes() {

		return this.mInfo.getSharedQuery().toString().getBytes();

	}

	@Override
	public void fromBytes(byte[] data) {

		ByteBuffer buffer = ByteBuffer.wrap(data);

		this.sharedQueryFromBytes(buffer);
		this.pqlFromBytes(buffer);
		this.peerFromBytes(buffer);
		this.subsequentPartsInfoFromBytes(buffer);

	}

	private void peerFromBytes(ByteBuffer buffer) {

		byte[] peerBytes = new byte[buffer.getInt()];
		buffer.get(peerBytes);
		this.mInfo.setPeer(toPeerID(new String(peerBytes)));

	}

	private void pqlFromBytes(ByteBuffer buffer) {

		byte[] pqlBytes = new byte[buffer.getInt()];
		buffer.get(pqlBytes);
		this.mInfo.setPQL(new String(pqlBytes));

	}

	private void subsequentPartsInfoFromBytes(ByteBuffer buffer) {

		Collection<IPair<String, PeerID>> info = Sets.newHashSet();
		int numInfo = buffer.getInt();

		for (int i = 0; i < numInfo; i++) {

			byte[] pqlBytes = new byte[buffer.getInt()];
			buffer.get(pqlBytes);
			byte[] peerBytes = new byte[buffer.getInt()];
			buffer.get(peerBytes);

			info.add(new Pair<String, PeerID>(new String(pqlBytes),
					toPeerID(new String(peerBytes))));

		}

		this.mInfo.setSubsequentPartsInformation(info);

	}

	private void sharedQueryFromBytes(ByteBuffer buffer) {

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