package de.uniol.inf.is.odysseus.p2p_new;

public class EmptyMessage implements IMessage {

	@Override
	public byte[] toBytes() {
		return new byte[0];
	}

	@Override
	public void fromBytes(byte[] data) {
	}

}
