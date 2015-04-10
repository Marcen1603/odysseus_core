package de.uniol.inf.is.odysseus.p2p_new.data.socket;

import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.peer.communication.IMessage;

public class PortMessage implements IMessage {

	private int port;
	private int id;
	
	public PortMessage() {
		
	}
	
	public PortMessage( int port, int id ) {
		this.port = port;
		this.id = id;
	}
	
	@Override
	public byte[] toBytes() {
		return ByteBuffer.allocate(8).putInt(port).putInt(id).array();
	}

	@Override
	public void fromBytes(byte[] data) {
		ByteBuffer bb = ByteBuffer.wrap(data);
		port = bb.getInt();
		id = bb.getInt();
	}

	public int getPort() {
		return port;
	}
	
	public int getId() {
		return id;
	}
}
