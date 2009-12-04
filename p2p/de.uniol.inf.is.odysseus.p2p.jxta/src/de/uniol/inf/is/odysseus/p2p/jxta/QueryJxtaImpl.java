package de.uniol.inf.is.odysseus.p2p.jxta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.jxta.protocol.PeerAdvertisement;
import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Subplan;

public class QueryJxtaImpl extends Query implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PipeAdvertisement responseSocketThinPeer;
	private PipeAdvertisement adminPeerPipe;
	private Map<String,BidJxtaImpl> adminPeerBidding;


	public Map<String, BidJxtaImpl> getAdminPeerBidding() {
		return adminPeerBidding;
	}

	public QueryJxtaImpl() {
		this.adminPeerBidding = new HashMap<String, BidJxtaImpl>();
	}

	public PipeAdvertisement getResponseSocketThinPeer() {
		return responseSocketThinPeer;
	}

	public void setResponseSocketThinPeer(PipeAdvertisement responseSocketThinPeer) {
		this.responseSocketThinPeer = responseSocketThinPeer;
	}

	public void setSubplans(ArrayList<AbstractLogicalOperator> list) {
		int planCounter = 1;
		for(ILogicalOperator op : list) {
			addSubPlan(""+planCounter, new Subplan(""+planCounter, op));
			planCounter++;
		}
		
	}
	public void addBidding(PipeAdvertisement socket, String peerId, String subPlanId, String bid){
		
		synchronized(getSubPlans().get(subPlanId).getBiddings()){
			BidJxtaImpl bidElem = new BidJxtaImpl();
			bidElem.setResponseSocket(socket);
			bidElem.setDate(new Date());
			bidElem.setPeerId(peerId);
			bidElem.setBid(bid);
			getSubPlans().get(subPlanId).getBiddings().add(bidElem);
		}
	}
	
	public void addAdminBidding(PipeAdvertisement socket, PeerAdvertisement peerAdv){
		synchronized(this.adminPeerBidding){
			BidJxtaImpl bid = new BidJxtaImpl();
			bid.setResponseSocket(socket);
			bid.setDate(new Date());
			bid.setPeer(peerAdv);
			this.adminPeerBidding.put(socket.getPipeID().toString(), bid);
		}
	}
		
	

	
	public void setAdminPeerPipe(PipeAdvertisement adminPipe) {
		this.adminPeerPipe = adminPipe;
	}

	public PipeAdvertisement getAdminPeerPipe() {
		return adminPeerPipe;
	}
	


}
