package de.uniol.inf.is.odysseus.net.communication;

public interface IMessage {

	public byte[] toBytes();
	public void fromBytes( byte[] data);
	
}
