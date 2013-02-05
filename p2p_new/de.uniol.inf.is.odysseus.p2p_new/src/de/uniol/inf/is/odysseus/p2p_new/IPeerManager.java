package de.uniol.inf.is.odysseus.p2p_new;

import com.google.common.collect.ImmutableList;

public interface IPeerManager {
	
	public void discoverPeers();
	public ImmutableList<String> getPeers();
	
}
