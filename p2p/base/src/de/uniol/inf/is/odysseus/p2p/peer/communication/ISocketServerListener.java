package de.uniol.inf.is.odysseus.p2p.peer.communication;

import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.p2p.peer.IOdysseusPeer;

public interface ISocketServerListener extends Runnable {
	public void registerMessageHandler(IMessageHandler messageHandler);
	public void registerMessageHandler(List<IMessageHandler> messageHandler);
	public boolean deregisterMessageHandler(IMessageHandler messageHandler);
	public Collection<IMessageHandler> getMessageHandler();
	void setPeer(IOdysseusPeer peer);

}
