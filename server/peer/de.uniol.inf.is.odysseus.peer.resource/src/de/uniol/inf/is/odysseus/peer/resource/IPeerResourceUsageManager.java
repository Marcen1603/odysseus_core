package de.uniol.inf.is.odysseus.peer.resource;

import java.util.Collection;

import net.jxta.peer.PeerID;

import com.google.common.base.Optional;

public interface IPeerResourceUsageManager {

	public Optional<IResourceUsage> getRemoteResourceUsage( PeerID peerID );
	public boolean hasRemoteResourceUsage(PeerID peerID);
	
	public Optional<IResourceUsage> getLocalResourceUsage();
	
	public Collection<PeerID> getRemotePeers();
}
