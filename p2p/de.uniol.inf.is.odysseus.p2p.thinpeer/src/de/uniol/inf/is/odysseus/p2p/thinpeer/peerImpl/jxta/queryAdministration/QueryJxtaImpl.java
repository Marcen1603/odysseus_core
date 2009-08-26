package de.uniol.inf.is.odysseus.p2p.thinpeer.peerImpl.jxta.queryAdministration;

import java.util.Date;

import net.jxta.protocol.PeerAdvertisement;
import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.p2p.thinpeer.queryAdministration.Query;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.BidJxtaImpl;

public class QueryJxtaImpl extends Query {
	
	
	private PipeAdvertisement adminPeerPipe;
	
	
	public PipeAdvertisement getAdminPeerPipe() {
		return adminPeerPipe;
	}

	public void setAdminPeerPipe(PipeAdvertisement adminPeerPipe) {
		this.adminPeerPipe = adminPeerPipe;
	}

	public QueryJxtaImpl(String query, String id) {
		super(query, id);
		// TODO Auto-generated constructor stub
	}
	
	public void addBidding(PipeAdvertisement socket, PeerAdvertisement peerAdv){
		synchronized(this.biddings){
			BidJxtaImpl bid = new BidJxtaImpl();
			bid.setResponseSocket(socket);
			bid.setDate(new Date());
			bid.setPeer(peerAdv);
			this.biddings.put(socket.getPipeID().toString(), bid);
		}
	}
	
	
}
