package de.uniol.inf.is.odysseus.net;

public interface IOdysseusNetStartup {

	public IOdysseusNode start() throws OdysseusNetException;
	public void stop();
	
}
