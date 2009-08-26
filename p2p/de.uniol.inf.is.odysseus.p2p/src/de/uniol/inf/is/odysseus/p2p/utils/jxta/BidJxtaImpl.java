package de.uniol.inf.is.odysseus.p2p.utils.jxta;

import net.jxta.protocol.PeerAdvertisement;
import net.jxta.protocol.PipeAdvertisement;

public class BidJxtaImpl extends Bid {

	private PeerAdvertisement peer;

	private PipeAdvertisement responseSocket;

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
