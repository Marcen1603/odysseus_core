package de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.bid;

import com.google.common.base.Preconditions;

import net.jxta.peer.PeerID;

public final class Bid {

	private final PeerID peerID;
	private final double value;
	
	public Bid( PeerID peerID, double value ) {
		Preconditions.checkArgument(value > 0, "Bidvalue must be positive!");
		Preconditions.checkNotNull(peerID, "PeerID for bid must not be null!");
		
		this.peerID = peerID;
		this.value = value;
	}
	
	public PeerID getBidderPeerID() {
		return peerID;
	}
	
	public double getValue() {
		return value;
	}
}
