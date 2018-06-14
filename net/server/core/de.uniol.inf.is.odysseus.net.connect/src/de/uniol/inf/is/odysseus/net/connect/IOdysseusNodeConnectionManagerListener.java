package de.uniol.inf.is.odysseus.net.connect;

public interface IOdysseusNodeConnectionManagerListener {

	public void nodeConnected( IOdysseusNodeConnection connection);
	public void nodeDisconnected( IOdysseusNodeConnection connection);
	
}
