package de.uniol.inf.is.odysseus.p2p_new.communication;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;

public class PeerCloseMessage implements IMessage {

	@Override
	public byte[] toBytes() {
		return new byte[0];
	}

	@Override
	public void fromBytes(byte[] data) {
	}

}
