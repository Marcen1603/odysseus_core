package de.uniol.inf.is.odysseus.p2p.peer.communication;


public interface ISocketServerListener extends Runnable {
	public IServerSocketConnectionHandler getServerSocketConnectionHandler();
	public void setServerSocketConnectionHandler(IServerSocketConnectionHandler connectionHandler);
}
