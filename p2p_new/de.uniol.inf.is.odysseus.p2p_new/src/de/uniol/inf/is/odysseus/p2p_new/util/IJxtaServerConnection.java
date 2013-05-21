package de.uniol.inf.is.odysseus.p2p_new.util;

import java.io.IOException;

import net.jxta.protocol.PipeAdvertisement;

import com.google.common.collect.ImmutableList;

public interface IJxtaServerConnection {

	void addListener( IJxtaServerConnectionListener listener);
	void removeListener( IJxtaServerConnectionListener listener);
	
	void start() throws IOException;
	void stop();
	boolean isStarted();
	
	ImmutableList<IJxtaConnection> getConnections();
	PipeAdvertisement getPipeAdvertisement();
}
