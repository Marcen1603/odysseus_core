package de.uniol.inf.is.odysseus.peer.recovery.messages;

import java.net.URI;
import java.nio.ByteBuffer;

import net.jxta.id.ID;
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
	private ID sharedQueryId;

	/**
	 * Creates a new message to agree with other peers who will do the recovery
	 * for a certain failed peer for a certain shared qurey
	 * 
	 * @param failedPeer
	 *            The peerId from the failed peer
	 * @param sharedQueryId
	 *            The query for which the sending peer wants to do the recovery
	 * @return
	 */
	public static RecoveryAgreementMessage createRecoveryAgreementMessage(
			PeerID failedPeer, ID sharedQueryId) {
		RecoveryAgreementMessage message = new RecoveryAgreementMessage();
		message.setFailedPeer(failedPeer);
		message.setSharedQueryId(sharedQueryId);
		return message;
	}

	@Override
	public byte[] toBytes() {
		ByteBuffer bb = null;
		int bbsize;
		int peerIdLength = failedPeer.toString().getBytes().length;
		int sharedQueryIdLength = sharedQueryId.toString().getBytes().length;

		bbsize = 4 + peerIdLength + 4 + sharedQueryIdLength;
		bb = ByteBuffer.allocate(bbsize);

		bb.putInt(peerIdLength);
		bb.put(failedPeer.toString().getBytes(), 0, peerIdLength);

		bb.putInt(sharedQueryIdLength);
		bb.put(sharedQueryId.toString().getBytes(), 0, sharedQueryIdLength);
		
		bb.flip();
		return bb.array();
	}

	@Override
	public void fromBytes(byte[] data) {
		ByteBuffer bb = ByteBuffer.wrap(data);
		
		int peerIdLength = bb.getInt();
		byte[] peerIdByte = new byte[peerIdLength];
		String peerIdString = new String(peerIdByte);
		
		int sharedQueryIdLength = bb.getInt();
		byte[] sharedQueryIdByte = new byte[sharedQueryIdLength];
		String sharedQueryIdString = new String(sharedQueryIdByte);
		
		try {
			URI peerIdUri = new URI(peerIdString);
			failedPeer = PeerID.create(peerIdUri);
			
			URI sharedQueryIdUri = new URI(sharedQueryIdString);
			sharedQueryId = ID.create(sharedQueryIdUri);
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

	public ID getSharedQueryId() {
		return sharedQueryId;
	}

	public void setSharedQueryId(ID sharedQueryId) {
		this.sharedQueryId = sharedQueryId;
	}

}
