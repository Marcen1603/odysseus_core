package de.uniol.inf.is.odysseus.peer.loadbalancing.active.lock;

import de.uniol.inf.is.odysseus.peer.communication.IMessage;

public class LockNotReleasedMessage implements IMessage {

	public LockNotReleasedMessage() {
		
	}
	
	@Override
	public byte[] toBytes() {
		return new byte[0];
	}

	@Override
	public void fromBytes(byte[] data) {
		
	}

}
