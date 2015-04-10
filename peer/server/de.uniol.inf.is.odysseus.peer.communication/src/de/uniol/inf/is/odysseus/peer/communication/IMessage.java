package de.uniol.inf.is.odysseus.peer.communication;

public interface IMessage {

	public byte[] toBytes();
	public void fromBytes( byte[] data);
	
}
