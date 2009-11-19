package de.uniol.inf.is.odysseus.p2p.distribution.provider;

import java.net.Socket;

public interface IServerSocketConnectionHandler extends Runnable{

	void setSocket(Socket socket);

}
