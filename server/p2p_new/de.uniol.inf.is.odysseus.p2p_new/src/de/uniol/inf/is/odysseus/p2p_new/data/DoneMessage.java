package de.uniol.inf.is.odysseus.p2p_new.data;

import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;

public class DoneMessage implements IMessage {

	private int idHash;
	
	public DoneMessage( int idHash ) {
		this.idHash = idHash;
	}
	
	public DoneMessage() {
		
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
