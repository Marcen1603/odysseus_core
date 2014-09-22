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
		ByteBuffer bb = ByteBuffer.allocate(32);
		bb.putLong(timestamp);
		return bb.array();
	}

	@Override
	public void fromBytes(byte[] data) {
		ByteBuffer buffer = ByteBuffer.wrap(data);

		timestamp = buffer.getLong();
	}
	
	public long getTimestamp() {
		return timestamp;
	}
}
