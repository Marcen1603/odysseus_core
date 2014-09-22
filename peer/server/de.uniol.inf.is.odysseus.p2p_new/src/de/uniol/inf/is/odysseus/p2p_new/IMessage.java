package de.uniol.inf.is.odysseus.p2p_new;

public interface IMessage {

	public byte[] toBytes();
	public void fromBytes( byte[] data);
	
}
