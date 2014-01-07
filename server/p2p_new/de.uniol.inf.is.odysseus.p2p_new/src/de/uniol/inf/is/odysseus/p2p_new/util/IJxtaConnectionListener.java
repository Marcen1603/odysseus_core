package de.uniol.inf.is.odysseus.p2p_new.util;

public interface IJxtaConnectionListener {
	
	public void onReceiveData(IJxtaConnection sender, byte[] data);
	public void onDisconnect(IJxtaConnection sender);
	public void onConnect(IJxtaConnection sender);
	
}
