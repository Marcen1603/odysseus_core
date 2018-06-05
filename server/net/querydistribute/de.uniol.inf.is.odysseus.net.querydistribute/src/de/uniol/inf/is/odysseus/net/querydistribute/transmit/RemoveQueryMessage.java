package de.uniol.inf.is.odysseus.net.querydistribute.transmit;

import java.util.UUID;

import de.uniol.inf.is.odysseus.net.communication.IMessage;

public class RemoveQueryMessage implements IMessage {

	private static int counter = 0;
	
	private UUID sharedQueryID;
	private int id;
	
	public RemoveQueryMessage() {
		
	}
	
	public RemoveQueryMessage( UUID sharedQueryID ) {
		this.sharedQueryID = sharedQueryID;
		this.id = counter++;
	}
	
	@Override
	public byte[] toBytes() {
		byte[] idBytes = sharedQueryID.toString().getBytes();
		byte[] data = new byte[idBytes.length + 4];
		
		insertInt(data, 0, id);
		System.arraycopy(idBytes, 0, data, 4, idBytes.length);
		
		return data;
	}

	@Override
	public void fromBytes(byte[] data) {
		id = byteArrayToInt(data, 0);
		
		byte[] sharedIDBytes = new byte[data.length - 4];
		System.arraycopy(data, 4, sharedIDBytes, 0, sharedIDBytes.length);
		
		sharedQueryID = UUID.fromString(new String(sharedIDBytes));
	}

	private static void insertInt(byte[] destArray, int offset, int value) {
		destArray[offset] = (byte) (value >>> 24);
		destArray[offset + 1] = (byte) (value >>> 16);
		destArray[offset + 2] = (byte) (value >>> 8);
		destArray[offset + 3] = (byte) (value);
	}

	private static int byteArrayToInt(byte[] b, int offset) {
		return b[3 + offset] & 0xFF | (b[2 + offset] & 0xFF) << 8 | (b[1 + offset] & 0xFF) << 16 | (b[0 + offset] & 0xFF) << 24;
	}
	
	public int getId() {
		return id;
	}
	
	public UUID getSharedQueryID() {
		return sharedQueryID;
	}
}
