package de.uniol.inf.is.odysseus.peer.logging;

import net.jxta.peer.PeerID;

public interface IJxtaLogMessageReceiver {

	public void logMessage( PeerID senderPeerID, int logLevel, String loggerName, String text);
	
}
