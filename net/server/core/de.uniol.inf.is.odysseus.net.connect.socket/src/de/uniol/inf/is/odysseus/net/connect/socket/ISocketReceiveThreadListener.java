package de.uniol.inf.is.odysseus.net.connect.socket;

public interface ISocketReceiveThreadListener {

	public void bytesReceived( byte[] data );
	public void socketDisconnected();
	
}
