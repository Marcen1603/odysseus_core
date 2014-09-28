package de.uniol.inf.is.odysseus.peer.recovery.messages;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;

public class BackupJxtaInfoMessage implements IMessage {

	private PeerID peerId;
	private ID sharedQueryId;
	private String key;
	private String value;
	
	public BackupJxtaInfoMessage() {
		
	}

	public BackupJxtaInfoMessage(PeerID peerId, ID sharedQueryId, String key,
			String value) {
		super();
		this.peerId = peerId;
		this.sharedQueryId = sharedQueryId;
		this.key = key;
		this.value = value;
	}

	public PeerID getPeerId() {
		return peerId;
	}

	public void setPeerId(PeerID peerId) {
		this.peerId = peerId;
	}

	public ID getSharedQueryId() {
		return sharedQueryId;
	}

	public void setSharedQueryId(ID sharedQueryId) {
		this.sharedQueryId = sharedQueryId;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public byte[] toBytes() {

		ByteBuffer bb = null;

		int peerIdLength = peerId.toString().getBytes().length;
		int queryIdLength = sharedQueryId.toString().getBytes().length;
		int keyLength = key.getBytes().length;
		int valueLength = value.getBytes().length;
		int saveLengthsLength = 4 * 4;

		int bbsize = peerIdLength + queryIdLength + keyLength + valueLength
				+ saveLengthsLength;
		bb = ByteBuffer.allocate(bbsize);

		bb.putInt(peerIdLength);
		bb.put(peerId.toString().getBytes());

		bb.putInt(queryIdLength);
		bb.put(sharedQueryId.toString().getBytes());

		bb.putInt(keyLength);
		bb.put(key.getBytes());

		bb.putInt(valueLength);
		bb.put(value.getBytes());

		bb.flip();
		return bb.array();
	}

	@Override
	public void fromBytes(byte[] data) {
		ByteBuffer bb = ByteBuffer.wrap(data);
		
		// PeerId
		int peerIdLength = bb.getInt();
		byte[] peerIdByte = new byte[peerIdLength];
		bb.get(peerIdByte, 0, peerIdLength);
		String peerIdString = new String(peerIdByte);
		try {
			URI peerUri = new URI(peerIdString);
			peerId = PeerID.create(peerUri);
		} catch (URISyntaxException e) {

		}
		
		// Shared query id
		int sharedQueryIdLength = bb.getInt();
		byte[] sharedQueryIdByte = new byte[sharedQueryIdLength];
		bb.get(sharedQueryIdByte, 0, sharedQueryIdLength);
		String sharedQueryIdString = new String(sharedQueryIdByte);
		try {
			URI queryUri = new URI(sharedQueryIdString);
			sharedQueryId = ID.create(queryUri);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		// key
		int keyLength = bb.getInt();
		byte[] keyByte = new byte[keyLength];
		bb.get(keyByte, 0, keyLength);
		key = new String(keyByte);
		
		// value
		int valueLength = bb.getInt();
		byte[] valueByte = new byte[valueLength];
		bb.get(valueByte, 0, valueLength);
		value = new String(valueByte);
		
	}

}
