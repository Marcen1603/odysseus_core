package de.uniol.inf.is.odysseus.p2p_new;

public interface IPeerManager {

	public void addListener(IPeerListener listener);

	public void checkNewPeers();

	public void removeListener(IPeerListener listener);

}
