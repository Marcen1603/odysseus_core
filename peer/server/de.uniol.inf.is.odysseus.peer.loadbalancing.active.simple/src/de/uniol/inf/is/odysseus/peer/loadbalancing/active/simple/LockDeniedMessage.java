package de.uniol.inf.is.odysseus.peer.loadbalancing.active.simple;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;

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