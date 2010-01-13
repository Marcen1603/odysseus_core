package de.uniol.inf.is.odysseus.p2p.peer.communication;

import java.util.List;

public interface ISocketServerListener extends Runnable {
	public boolean registerMessageHandler(IMessageHandler messageHandler);
	public boolean registerMessageHandler(List<IMessageHandler> messageHandler);
	public boolean deregisterMessageHandler(IMessageHandler messageHandler);
	public boolean deregisterMessageHandler(List<IMessageHandler> messageHandler);
}
