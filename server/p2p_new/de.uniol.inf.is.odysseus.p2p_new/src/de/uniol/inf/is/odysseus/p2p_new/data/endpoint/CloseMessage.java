package de.uniol.inf.is.odysseus.p2p_new.data.endpoint;

import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;

public class CloseMessage implements IMessage {

	private int idHash;
	
	public CloseMessage( int idHash ) {
		this.idHash = idHash;
	}
	
	public CloseMessage() {
		
	}
	
	@Override
	public byte[] toBytes() {
		return ByteBuffer.allocate(5).putInt(idHash).array();
	}

	@Override
	public void fromBytes(byte[] data) {
		idHash = ByteBuffer.wrap(data).getInt();
	}

	public int getIdHash() {
		return idHash;
	}
}
