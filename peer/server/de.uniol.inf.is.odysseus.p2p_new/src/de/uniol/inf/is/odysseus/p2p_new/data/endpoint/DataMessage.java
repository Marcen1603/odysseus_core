package de.uniol.inf.is.odysseus.p2p_new.data.endpoint;

import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.peer.communication.IMessage;

public class DataMessage implements IMessage {

	private int idHash;
	private byte[] data;
	
	public DataMessage() {
		
	}
	
	public DataMessage( int idHash, byte[] data ) {
		this.idHash = idHash;
		this.data = data;
	}
	
	@Override
	public byte[] toBytes() {
		ByteBuffer bb = ByteBuffer.allocate(data.length + 4);
		bb.putInt(idHash);
		bb.put(data);
		return bb.array();
	}

	@Override
	public void fromBytes(byte[] data) {
		ByteBuffer bb = ByteBuffer.wrap(data);
		idHash = bb.getInt();
		this.data = new byte[data.length - 4];
		bb.get(this.data);
	}

	public int getIdHash() {
		return idHash;
	}
	
	public byte[] getData() {
		return data;
	}
}
