package de.uniol.inf.is.odysseus.peer.loadbalancing.active.simple;

import de.uniol.inf.is.odysseus.peer.communication.IMessage;

public class RequestLockMessage implements IMessage {
	
	public RequestLockMessage() {
		
	}
	
	@Override
	public byte[] toBytes() {
		return new byte[0];
	}

	@Override
	public void fromBytes(byte[] data) {
		
	}

	
	
}
