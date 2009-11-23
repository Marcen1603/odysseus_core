package de.uniol.inf.is.odysseus.p2p.peer.communication;

import java.util.Map;

public interface IServerSocketConnectionHandler extends Runnable{
	public void setMessageHandlerList(Map<String, IMessageHandler> messageHandler);
	public Map<String, IMessageHandler> getMessageHandlerList();
}
