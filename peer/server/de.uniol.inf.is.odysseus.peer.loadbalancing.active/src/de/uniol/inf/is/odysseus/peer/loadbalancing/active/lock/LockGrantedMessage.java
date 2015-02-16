package de.uniol.inf.is.odysseus.peer.loadbalancing.active.lock;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;

public class LockGrantedMessage implements IMessage {

	
	public LockGrantedMessage() {
		
	}
	
	@Override
	public byte[] toBytes() {
		return new byte[0];
	}

	@Override
	public void fromBytes(byte[] data) {
		
	}

}
