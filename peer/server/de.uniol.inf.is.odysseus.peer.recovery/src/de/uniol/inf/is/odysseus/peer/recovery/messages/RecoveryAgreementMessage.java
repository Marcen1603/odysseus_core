package de.uniol.inf.is.odysseus.peer.recovery.messages;

import java.net.URI;
import java.nio.ByteBuffer;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;

/**
 * This message is used to get an agreement if more than one peer could do and
 * wants to do the recovery for a failed peer. (It's always send if a peer
 * detects a failure)
 * 
 * @author Tobias Brandt
 *
 */
public class RecoveryAgreementMessage implements IMessage {

	private PeerID failedPeer;

	/**
	 * Creates a new message to agree with other peers who will do the recovery
	 * 
	 * @param failedPeer The peerId from the failed peer
	 * @return
	 */
	public static RecoveryAgreementMessage createRecoveryAgreementMessage(
			PeerID failedPeer) {
		RecoveryAgreementMessage message = new RecoveryAgreementMessage();
		message.setFailedPeer(failedPeer);
		return message;
	}

	@Override
	public byte[] toBytes() {
		ByteBuffer bb = null;
		int bbsize;
		int peerIdLength = failedPeer.toString().getBytes().length;

		bbsize = 4 + peerIdLength;
		bb = ByteBuffer.allocate(bbsize);

		bb.putInt(peerIdLength);
		bb.put(failedPeer.toString().getBytes(), 0, peerIdLength);

		bb.flip();
		return bb.array();
	}

	@Override
	public void fromBytes(byte[] data) {
		ByteBuffer bb = ByteBuffer.wrap(data);
		int peerIdLength = bb.getInt();

		byte[] peerIdByte = new byte[peerIdLength];
		String peerIdString = new String(peerIdByte);

		try {
			URI peerIdUri = new URI(peerIdString);
			failedPeer = PeerID.create(peerIdUri);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public PeerID getFailedPeer() {
		return failedPeer;
	}

	public void setFailedPeer(PeerID failedPeer) {
		this.failedPeer = failedPeer;
	}

}
