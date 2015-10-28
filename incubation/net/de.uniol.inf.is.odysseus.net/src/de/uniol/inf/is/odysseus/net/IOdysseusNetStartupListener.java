package de.uniol.inf.is.odysseus.net;

public interface IOdysseusNetStartupListener {

	public void odysseusNetStarted(IOdysseusNetStartupManager sender, OdysseusNetStartupData data);
	public void odysseusNetStopped(IOdysseusNetStartupManager sender);
	
}
