package de.uniol.inf.is.odysseus.net;

public interface IOdysseusNetStartup {

	public OdysseusNetStartupData start() throws OdysseusNetException;
	public void stop();
	
}
