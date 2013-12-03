package de.uniol.inf.is.odysseus.p2p_new.util.deprecated;

import java.io.IOException;


import net.jxta.protocol.PipeAdvertisement;

@Deprecated
public interface IJxtaConnectionOld {

	void removeListener(IJxtaConnectionListener listener);
	void addListener(IJxtaConnectionListener listener);
	
	void connect() throws IOException;
	void disconnect();
	boolean isConnected();

	void send(byte[] message) throws IOException;
	
	PipeAdvertisement getPipeAdvertisement();
}
