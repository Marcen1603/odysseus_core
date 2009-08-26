package de.uniol.inf.is.odysseus.p2p.administrationpeer.peerImpl.jxta.queryAdministration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.queryAdministration.Query;

public class QueryJxtaImpl extends Query implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PipeAdvertisement responseSocketThinPeer;

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
			this.subPlans.add(new SubplanJxtaImpl(""+i,abstractLogicalOperator));
		}
		
	}
	
	public void addBidding(PipeAdvertisement socket, String peerId){
		synchronized(this.biddings){
			BidJxtaImpl bid = new BidJxtaImpl();
			bid.setResponseSocket(socket);
			bid.setDate(new Date());
			bid.setPeerId(peerId);
			this.biddings.add(bid);
		}
	}
	
	
	
	

}
