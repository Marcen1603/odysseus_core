package de.uniol.inf.is.odysseus.peer.resource.impl;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;

public class AskUsageMessage implements IMessage {

	public AskUsageMessage() {
	}
	
	@Override
	public byte[] toBytes() {
		return new byte[0];
	}

	@Override
	public void fromBytes(byte[] data) {
	}

}
