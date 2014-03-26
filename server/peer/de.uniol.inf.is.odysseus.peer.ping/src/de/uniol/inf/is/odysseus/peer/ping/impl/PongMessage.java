package de.uniol.inf.is.odysseus.peer.ping.impl;

import java.nio.ByteBuffer;

import org.apache.commons.math.geometry.Vector3D;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;

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
		byte[] longArray = ByteBuffer.allocate(8).putLong(timestamp).array();
		byte[] doubleXArray = ByteBuffer.allocate(8).putDouble(position.getX()).array();
		byte[] doubleYArray = ByteBuffer.allocate(8).putDouble(position.getY()).array();
		byte[] doubleZArray = ByteBuffer.allocate(8).putDouble(position.getZ()).array();

		byte[] message = new byte[32];
		System.arraycopy(longArray, 0, message, 0, longArray.length);
		System.arraycopy(doubleXArray, 0, message, 8, doubleXArray.length);
		System.arraycopy(doubleYArray, 0, message, 16, doubleYArray.length);
		System.arraycopy(doubleZArray, 0, message, 24, doubleZArray.length);

		return message;
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
