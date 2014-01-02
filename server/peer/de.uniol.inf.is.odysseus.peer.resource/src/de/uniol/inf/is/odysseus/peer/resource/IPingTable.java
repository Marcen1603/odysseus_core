package de.uniol.inf.is.odysseus.peer.resource;

import java.util.Collection;

import com.google.common.base.Optional;

import net.jxta.peer.PeerID;

public interface IPingTable {

	public Collection<PeerID> getPeerIDs();
	
	public Optional<Long> getPing( PeerID source, PeerID destination);
	
}
