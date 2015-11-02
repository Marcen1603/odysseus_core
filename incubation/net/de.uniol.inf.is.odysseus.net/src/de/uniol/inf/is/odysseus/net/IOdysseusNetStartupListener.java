package de.uniol.inf.is.odysseus.net;

public interface IOdysseusNetStartupListener {

	public void odysseusNetStarted(IOdysseusNetStartupManager sender, IOdysseusNode localNode);
	public void odysseusNetStopped(IOdysseusNetStartupManager sender);
	
}
