package de.uniol.inf.is.odysseus.net;

public interface IOdysseusNetStartupManager {

	public void start() throws OdysseusNetException ;
	public void stop();
	
	public boolean isStarted();
}
