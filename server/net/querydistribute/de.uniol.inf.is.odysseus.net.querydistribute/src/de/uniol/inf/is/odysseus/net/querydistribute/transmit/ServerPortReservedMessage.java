package de.uniol.inf.is.odysseus.net.querydistribute.transmit;

import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.net.communication.IMessage;

public class ServerPortReservedMessage implements IMessage {

	private int reservedPort;

	public ServerPortReservedMessage() {
	}
	
	public ServerPortReservedMessage( int reservedPort ) {
		this.reservedPort = reservedPort;
	}
	
	public int getReservedPort() {
		return reservedPort;
	}

	@Override
	public byte[] toBytes() {
		return ByteBuffer.allocate(4).putInt(reservedPort).array();
	}

	@Override
	public void fromBytes(byte[] data) {
		ByteBuffer bb = ByteBuffer.wrap(data);
		reservedPort = bb.getInt();
	}

}
