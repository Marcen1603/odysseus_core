package de.uniol.inf.is.odysseus.net.update.message;

import de.uniol.inf.is.odysseus.net.communication.IMessage;

public class DoUpdateMessage implements IMessage {

	@Override
	public byte[] toBytes() {
		return new byte[0];
	}

	@Override
	public void fromBytes(byte[] data) {
	}

}
