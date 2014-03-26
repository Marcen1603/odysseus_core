package de.uniol.inf.is.odysseus.peer.ping.impl;

import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;

public class PingMessage implements IMessage {

	private long timestamp;
	
	public PingMessage() {
		timestamp = System.currentTimeMillis();
	}
	
	@Override
	public byte[] toBytes() {
		return ByteBuffer.allocate(8).putLong(System.currentTimeMillis()).array();
	}

	@Override
	public void fromBytes(byte[] data) {
		timestamp = ByteBuffer.wrap(data).getLong();
	}
	
	public long getTimestamp() {
		return timestamp;
	}

}
