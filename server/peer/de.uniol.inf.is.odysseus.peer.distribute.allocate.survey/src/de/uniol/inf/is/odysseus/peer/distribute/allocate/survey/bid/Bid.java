package de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.bid;

import com.google.common.base.Preconditions;

import net.jxta.peer.PeerID;

public final class Bid implements Comparable<Bid>{

	private final PeerID bidderPeer;
	private final double value;
	
	public Bid( PeerID bidderPeer, double value ) {
		Preconditions.checkNotNull(bidderPeer, "bidderPeer must not be null!");
		Preconditions.checkArgument(value >= 0, "Bid value must be positive or zero!");
		
		this.bidderPeer = bidderPeer;
		this.value = value;
	}

	@Override
	public int compareTo(Bid o) {
		return Double.compare(value, o.value);
	}
	
	public PeerID getBidderPeer() {
		return bidderPeer;
	}
	
	public double getValue() {
		return value;
	}
}
