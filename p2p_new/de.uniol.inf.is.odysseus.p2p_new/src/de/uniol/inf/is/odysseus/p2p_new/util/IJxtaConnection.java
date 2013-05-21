package de.uniol.inf.is.odysseus.p2p_new.util;

import java.io.IOException;

public interface IJxtaConnection {

	void addListener( IJxtaConnectionListener listener );
	void removeListener( IJxtaConnectionListener listener );
	
	void send(byte[] data) throws IOException;
	
	void disconnect();
}
