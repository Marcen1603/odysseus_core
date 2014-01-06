package de.uniol.inf.is.odysseus.peer.ping;

import net.jxta.peer.PeerID;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableCollection;

public interface IPingMap {

	public Optional<Double> getPing(PeerID peer);
	public Optional<Double> getRemotePing( PeerID start, PeerID end );

	public Optional<IPingMapNode> getNode(PeerID peer);
	public ImmutableCollection<PeerID> getPeerIDs();

}
