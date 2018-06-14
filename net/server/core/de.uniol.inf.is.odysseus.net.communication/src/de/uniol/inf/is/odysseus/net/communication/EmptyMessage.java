package de.uniol.inf.is.odysseus.net.communication;


public class EmptyMessage implements IMessage {

	@Override
	public final byte[] toBytes() {
		return new byte[0];
	}

	@Override
	public final void fromBytes(byte[] data) {
	}

}
