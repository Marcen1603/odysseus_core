package de.uniol.inf.is.odysseus.peer.ping.impl;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.peer.ping.IPingMapNode;
import net.jxta.peer.PeerID;

public final class PingMapNode implements IPingMapNode {

	private final PeerID peerID;
	
	private double x = 0.0;
	private double y = 0.0;

	PingMapNode(PeerID peerID) {
		Preconditions.checkNotNull(peerID, "PeerID for being a pingmapnode must not be null!");
		
		this.peerID = peerID;
	}

	@Override
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	@Override
	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	@Override
	public PeerID getPeerID() {
		return peerID;
	}
}
