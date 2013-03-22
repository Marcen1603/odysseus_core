package de.uniol.inf.is.odysseus.p2p_new;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableCollection;

public interface IPeerManager {

	public void addListener(IPeerListener listener);

	public void checkNewPeers();

	public void removeListener(IPeerListener listener);
	
	public ImmutableCollection<String> getPeerIDs();
	
	public Optional<String> getPeerName( String peerID );

}
