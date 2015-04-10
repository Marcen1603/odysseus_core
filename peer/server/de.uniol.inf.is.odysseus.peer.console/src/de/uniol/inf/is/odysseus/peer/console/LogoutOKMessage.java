package de.uniol.inf.is.odysseus.peer.console;

import de.uniol.inf.is.odysseus.peer.communication.IMessage;

public class LogoutOKMessage implements IMessage {

	@Override
	public byte[] toBytes() {
		return new byte[0];
	}

	@Override
	public void fromBytes(byte[] data) {

	}

}
