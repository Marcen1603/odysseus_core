package de.uniol.inf.is.odysseus.peer.resource;

import java.util.concurrent.Future;

import net.jxta.peer.PeerID;

import com.google.common.base.Optional;

public interface IPeerResourceUsageManager {
	public Future<Optional<IResourceUsage>> getRemoteResourceUsage( PeerID peerID );
	public IResourceUsage getLocalResourceUsage();
}
