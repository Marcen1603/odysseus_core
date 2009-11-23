package de.uniol.inf.is.odysseus.p2p.jxta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.jxta.protocol.PeerAdvertisement;
import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;

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

	@Override
	public void setSubplans(ArrayList<AbstractLogicalOperator> list) {
		int i=0;
		for (AbstractLogicalOperator abstractLogicalOperator : list) {
			i++;
			this.subPlans.put(""+i,new SubplanJxtaImpl(""+i,abstractLogicalOperator));
		}
		
	}
	public void addBidding(PipeAdvertisement socket, String peerId, String subPlanId){
		
		synchronized(getSubPlans().get(subPlanId).getBiddings()){
			BidJxtaImpl bid = new BidJxtaImpl();
			bid.setResponseSocket(socket);
			bid.setDate(new Date());
			bid.setPeerId(peerId);
			getSubPlans().get(subPlanId).getBiddings().add(bid);
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
