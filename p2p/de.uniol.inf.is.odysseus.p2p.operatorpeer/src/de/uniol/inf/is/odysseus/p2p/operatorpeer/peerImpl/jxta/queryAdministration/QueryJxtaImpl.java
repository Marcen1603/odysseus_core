package de.uniol.inf.is.odysseus.p2p.operatorpeer.peerImpl.jxta.queryAdministration;

import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.queryAdministration.Query;

public class QueryJxtaImpl extends Query {

	private static final long serialVersionUID = 1L;

	private PipeAdvertisement responseSocketAdminPeer;

	public PipeAdvertisement getResponseSocketAdminPeer() {
		return responseSocketAdminPeer;
	}

	public void setResponseSocketAdminPeer(PipeAdvertisement responseSocket) {
		this.responseSocketAdminPeer = responseSocket;
	}

}
