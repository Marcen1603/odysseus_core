package de.uniol.inf.is.odysseus.scars.operator.jdvesink.server;

import java.nio.ByteBuffer;

public interface IServer {

	public void sendData(ByteBuffer buffer);
	
	public void close();
	
	public void start();
	
}
