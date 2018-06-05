package de.uniol.inf.is.odysseus.net.ping.impl;

import java.nio.ByteBuffer;

import org.apache.commons.math.geometry.Vector3D;

import de.uniol.inf.is.odysseus.net.communication.IMessage;

public class PongMessage implements IMessage {

	private long timestamp;
	private Vector3D position;
	
	public PongMessage() {
	}
	
	public PongMessage( PingMessage pingMessage, Vector3D position ) {
		timestamp = pingMessage.getTimestamp();
		this.position = position;
	}
	
	@Override
	public byte[] toBytes() {
		ByteBuffer bb = ByteBuffer.allocate(32);
		bb.putLong(timestamp);
		bb.putDouble(position.getX());
		bb.putDouble(position.getY());
		bb.putDouble(position.getZ());
		return bb.array();
	}

	@Override
	public void fromBytes(byte[] data) {
		ByteBuffer buffer = ByteBuffer.wrap(data);

		timestamp = buffer.getLong();
		
		double x = buffer.getDouble();
		double y = buffer.getDouble();
		double z = buffer.getDouble();
		position = new Vector3D(x,y,z);
	}
	
	public Vector3D getPosition() {
		return position;
	}
	
	public long getTimestamp() {
		return timestamp;
	}

}
