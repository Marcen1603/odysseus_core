package de.uniol.inf.is.odysseus.net.querydistribute.transmit;

import java.util.UUID;

import de.uniol.inf.is.odysseus.net.communication.IMessage;

public class AbortQueryPartAddMessage implements IMessage {

	private UUID sharedQueryID;
	
	public AbortQueryPartAddMessage(UUID sharedQueryID) {
		this.sharedQueryID = sharedQueryID;
	}
	
	public AbortQueryPartAddMessage() {
		
	}

	@Override
	public byte[] toBytes() {
		return sharedQueryID.toString().getBytes();
	}

	@Override
	public void fromBytes(byte[] data) {
		String idString = new String(data);
		sharedQueryID = UUID.fromString(idString);
	}

	public UUID getSharedQueryID() {
		return sharedQueryID;
	}
}
