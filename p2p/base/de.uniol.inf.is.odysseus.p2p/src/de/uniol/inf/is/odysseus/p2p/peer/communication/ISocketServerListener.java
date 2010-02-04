package de.uniol.inf.is.odysseus.p2p.peer.communication;

import java.util.List;

public interface ISocketServerListener extends Runnable {
	public List<IMessageHandler> registerMessageHandler(IMessageHandler messageHandler);
	public List<IMessageHandler> registerMessageHandler(List<IMessageHandler> messageHandler);
	public boolean deregisterMessageHandler(IMessageHandler messageHandler);
	public boolean deregisterMessageHandler(List<IMessageHandler> messageHandler);
}
