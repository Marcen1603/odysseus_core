package de.uniol.inf.is.odysseus.p2p.administrationpeer.peerImpl.jxta.queryAdministration;

import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.queryAdministration.Bid;

public class BidJxtaImpl extends Bid{
	
	private PipeAdvertisement responseSocket;
	
	public PipeAdvertisement getResponseSocket() {
		return responseSocket;
	}

	public void setResponseSocket(PipeAdvertisement responseSocket) {
		this.responseSocket = responseSocket;
	}
	
	

}
