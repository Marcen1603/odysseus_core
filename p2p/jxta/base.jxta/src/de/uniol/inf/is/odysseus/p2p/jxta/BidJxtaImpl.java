package de.uniol.inf.is.odysseus.p2p.jxta;

import de.uniol.inf.is.odysseus.p2p.queryhandling.Bid;
import net.jxta.protocol.PeerAdvertisement;
import net.jxta.protocol.PipeAdvertisement;

public class BidJxtaImpl extends Bid{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2911499561644551179L;
	private PipeAdvertisement responseSocket;
	private PeerAdvertisement peer;
	
	public PeerAdvertisement getPeer() {
		return peer;
	}

	public void setPeer(PeerAdvertisement peer) {
		this.peer = peer;
	}

	public PipeAdvertisement getResponseSocket() {
		return responseSocket;
	}

	public void setResponseSocket(PipeAdvertisement responseSocket) {
		this.responseSocket = responseSocket;
	}

}
