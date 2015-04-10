package de.uniol.inf.is.odysseus.peer.loadbalancing.active.simple;

import de.uniol.inf.is.odysseus.peer.communication.IMessage;

public class LockDeniedMessage implements IMessage {

	public LockDeniedMessage() {
		
	}
	
	@Override
	public byte[] toBytes() {
		return new byte[0];
	}

	@Override
	public void fromBytes(byte[] data) {
		
	}

}
