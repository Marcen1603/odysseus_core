package de.uniol.inf.is.odysseus.p2p_new;

public class EmptyMessage implements IMessage {

	@Override
	public final byte[] toBytes() {
		return new byte[0];
	}

	@Override
	public final void fromBytes(byte[] data) {
	}

}
