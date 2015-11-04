package de.uniol.inf.is.odysseus.net;

public interface IOdysseusNetStartupManager {

	public void start() throws OdysseusNetException ;
	public void stop();
	
	public boolean isStarted();
	public IOdysseusNode getLocalOdysseusNode() throws OdysseusNetException;
	
	public void addListener( IOdysseusNetStartupListener listener);
	public void removeListener( IOdysseusNetStartupListener listener);
}
