package de.uniol.inf.is.odysseus.peer.update;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;

public class DoUpdateMessage implements IMessage {

	@Override
	public byte[] toBytes() {
		return new byte[0];
	}

	@Override
	public void fromBytes(byte[] data) {
	}

}
