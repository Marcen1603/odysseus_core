package de.uniol.inf.is.odysseus.p2p_new.util;


public interface IJxtaServerConnectionListener {

	void connectionAdded( IJxtaServerConnection sender, IJxtaConnection addedConnection );
	void connectionRemoved( IJxtaServerConnection sender, IJxtaConnection removedConnection );
	
}
