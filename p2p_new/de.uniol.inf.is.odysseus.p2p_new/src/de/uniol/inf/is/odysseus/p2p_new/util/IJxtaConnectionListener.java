package de.uniol.inf.is.odysseus.p2p_new.util;

public interface IJxtaConnectionListener {

	public void onConnect(AbstractJxtaConnection sender);

	public void onDisconnect(AbstractJxtaConnection sender);

	public void onReceiveData(AbstractJxtaConnection sender, byte[] data);

}
