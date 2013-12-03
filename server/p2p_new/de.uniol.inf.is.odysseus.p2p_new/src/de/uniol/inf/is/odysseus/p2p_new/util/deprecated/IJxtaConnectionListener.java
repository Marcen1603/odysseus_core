package de.uniol.inf.is.odysseus.p2p_new.util.deprecated;


@Deprecated
public interface IJxtaConnectionListener {

	public void onConnect(IJxtaConnectionOld sender);
	public void onReceiveData(IJxtaConnectionOld sender, byte[] data);
	public void onDisconnect(IJxtaConnectionOld sender);
	
}
