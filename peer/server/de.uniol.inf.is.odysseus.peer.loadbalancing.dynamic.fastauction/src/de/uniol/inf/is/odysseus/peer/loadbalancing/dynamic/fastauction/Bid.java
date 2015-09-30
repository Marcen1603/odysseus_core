package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.fastauction;

import net.jxta.peer.PeerID;

public class Bid {
	private Double bid;
	private int auctionID;
	private PeerID peerID;
	
	public Bid(int auctionID, PeerID biddingPeer, double bid) {
		this.bid = bid;
		this.auctionID = auctionID;
		this.peerID = biddingPeer;
	}
	
	
	public Double getBid() {
		return bid;
	}
	public void setBid(Double bid) {
		this.bid = bid;
	}
	public int getAuctionID() {
		return auctionID;
	}
	public void setAuctionID(int auctionID) {
		this.auctionID = auctionID;
	}
	public PeerID getPeerID() {
		return peerID;
	}
	public void setPeerID(PeerID peerID) {
		this.peerID = peerID;
	}
}
