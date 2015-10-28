package de.uniol.inf.is.odysseus.net;

import java.net.InetAddress;

public interface IOdysseusNode {

	public OdysseusNodeID getID();
	
	public String getName();
	public InetAddress getInetAddress();
	
}
