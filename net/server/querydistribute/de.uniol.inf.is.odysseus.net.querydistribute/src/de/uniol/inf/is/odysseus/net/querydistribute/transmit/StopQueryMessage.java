package de.uniol.inf.is.odysseus.net.querydistribute.transmit;

import java.util.UUID;

import de.uniol.inf.is.odysseus.net.communication.IMessage;

public class StopQueryMessage implements IMessage {

	private UUID sharedQueryID;
	
	public StopQueryMessage() {
		
	}
	
	public StopQueryMessage( UUID sharedQueryID ) {
		this.sharedQueryID = sharedQueryID;
	}
	
	@Override
	public byte[] toBytes() {
		return sharedQueryID.toString().getBytes();
	}

	@Override
	public void fromBytes(byte[] data) {
		sharedQueryID = UUID.fromString(new String(data));
	}

	public UUID getSharedQueryID() {
		return sharedQueryID;
	}
}
