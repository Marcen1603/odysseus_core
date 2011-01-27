package de.uniol.inf.is.odysseus.p2p.jxta;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.jxta.protocol.PeerAdvertisement;
import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.p2p.queryhandling.P2PQuery;

public class P2PQueryJxtaImpl extends P2PQuery{
	
	private static final long serialVersionUID = -8784146315565652081L;
	private PipeAdvertisement responseSocketThinPeer;
	private PipeAdvertisement adminPeerPipe;
	private Map<String,BidJxtaImpl> adminPeerBidding;


	public Map<String, BidJxtaImpl> getAdminPeerBidding() {
		return adminPeerBidding;
	}

	public P2PQueryJxtaImpl() {
		this.adminPeerBidding = new HashMap<String, BidJxtaImpl>();
	}

	public PipeAdvertisement getResponseSocketThinPeer() {
		return responseSocketThinPeer;
	}

	public void setResponseSocketThinPeer(PipeAdvertisement responseSocketThinPeer) {
		this.responseSocketThinPeer = responseSocketThinPeer;
	}


	public void addBidding(PipeAdvertisement socket, String peerId, String subPlanId, String bid){
		BidJxtaImpl bidElem = new BidJxtaImpl(socket, new Date(), peerId, bid);
		getSubPlans().get(subPlanId).addBit(bidElem);
	}
	
	public void addAdminBidding(PipeAdvertisement socket, PeerAdvertisement peerAdv){
		synchronized(this.adminPeerBidding){
			BidJxtaImpl bid = new BidJxtaImpl(socket, new Date(), peerAdv);
			this.adminPeerBidding.put(""+socket.getPipeID(), bid);
		}
	}
		
	

	
	public void setAdminPeerPipe(PipeAdvertisement adminPipe) {
		this.adminPeerPipe = adminPipe;
	}

	public PipeAdvertisement getAdminPeerPipe() {
		return adminPeerPipe;
	}
	


}
