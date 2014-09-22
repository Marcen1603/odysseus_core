package de.uniol.inf.is.odysseus.peer.console;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;

public class LoginOKMessage implements IMessage {

	@Override
	public byte[] toBytes() {
		return new byte[0];
	}

	@Override
	public void fromBytes(byte[] data) {
	}

}
