package de.uniol.inf.is.odysseus.net.connect.socket;

import java.net.Socket;

public interface IServerSocketAcceptListener {
	
	public void acceptedConnection( Socket clientSocket );
	
}
