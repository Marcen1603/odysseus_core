package de.uniol.inf.is.odysseus.peer.loadbalancing.active.simple;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;

public class ReleaseLockMessage implements IMessage{

	public ReleaseLockMessage() {
		
	}
	
	@Override
	public byte[] toBytes() {
		return new byte[0];
	}

	@Override
	public void fromBytes(byte[] data) {
		
	}

}
