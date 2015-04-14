package de.uniol.inf.is.odysseus.peer.transmission.endpoint;

import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.peer.communication.IMessage;

public class OpenAckMessage implements IMessage {
	
	private int idHash;
	
	public OpenAckMessage( int idHash ) {
		this.idHash = idHash;
	}
	
	public OpenAckMessage() {
		
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
