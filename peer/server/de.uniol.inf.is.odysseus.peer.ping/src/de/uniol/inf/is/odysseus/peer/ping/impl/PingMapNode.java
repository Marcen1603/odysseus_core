package de.uniol.inf.is.odysseus.peer.ping.impl;

import net.jxta.peer.PeerID;

import org.apache.commons.math.geometry.Vector3D;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.peer.ping.IPingMapNode;

public final class PingMapNode implements IPingMapNode {

	private final PeerID peerID;
	private Vector3D position = new Vector3D(0,0,0);

	PingMapNode(PeerID peerID) {
		Preconditions.checkNotNull(peerID, "PeerID for being a pingmapnode must not be null!");
		
		this.peerID = peerID;
	}

	@Override
	public Vector3D getPosition() {
		return new Vector3D(position.getX(), position.getY(), position.getZ());
	}
	
	@Override
	public PeerID getPeerID() {
		return peerID;
	}

	public void setPosition(Vector3D position) {
		Preconditions.checkNotNull(position, "Position for ping map node must not be null!");
		
		this.position = position;
	}
}
