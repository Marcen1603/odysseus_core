package de.uniol.inf.is.odysseus.p2p.peer.communication;

public interface ISocketServerListener extends Runnable {
	public boolean registerMessageHandler(IMessageHandler messageHandler);
	public boolean deregisterMessageHandler(IMessageHandler messageHandler);
	
}
