package de.uniol.inf.is.odysseus.peer.ping;

import net.jxta.peer.PeerID;

import org.apache.commons.math.geometry.Vector3D;

import com.google.common.base.Optional;

public interface IPingMap {

	public Optional<Double> getPing(PeerID peer);
	public Optional<Double> getRemotePing( PeerID start, PeerID end );

	public Optional<IPingMapNode> getNode(PeerID peer);
	public Vector3D getLocalPosition();
	public void setPosition(PeerID peerID, Vector3D position);

	public void addListener( IPingMapListener listener );
	public void removeListener( IPingMapListener listener );
	
}
