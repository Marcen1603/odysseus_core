package de.uniol.inf.is.odysseus.net;

import java.net.InetAddress;

public interface INode {

	public String getName();
	public InetAddress getInetAddress();
	public int getPort();
	
}
