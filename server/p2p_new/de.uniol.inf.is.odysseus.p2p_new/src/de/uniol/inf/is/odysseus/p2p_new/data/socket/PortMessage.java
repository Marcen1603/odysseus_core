package de.uniol.inf.is.odysseus.p2p_new.data.socket;

import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;

public class PortMessage implements IMessage {

	private int port;
	
	public PortMessage() {
		
	}
	
	public PortMessage( int port ) {
		this.port = port;
	}
	
	@Override
	public byte[] toBytes() {
		return ByteBuffer.allocate(4).putInt(port).array();
	}

	@Override
	public void fromBytes(byte[] data) {
		port = ByteBuffer.wrap(data).getInt();
	}

	public int getPort() {
		return port;
	}
}
