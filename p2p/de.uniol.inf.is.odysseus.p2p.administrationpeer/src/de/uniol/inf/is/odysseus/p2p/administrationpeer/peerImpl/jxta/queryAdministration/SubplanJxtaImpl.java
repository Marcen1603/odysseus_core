package de.uniol.inf.is.odysseus.p2p.administrationpeer.peerImpl.jxta.queryAdministration;

import java.util.HashMap;

import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.queryAdministration.Subplan;


public class SubplanJxtaImpl extends Subplan{
	
	public SubplanJxtaImpl(String id, AbstractLogicalOperator op){
		super(id, op);
	}
	
	private HashMap<String, PipeAdvertisement> hotPeers = new HashMap<String, PipeAdvertisement>();
		
	String responseSocket;

	public String getResponseSocket() {
		return responseSocket;
	}

	public void setResponseSocket(String responseSocket) {
		this.responseSocket = responseSocket;
	}

	public HashMap<String, PipeAdvertisement> getHotPeers() {
		return hotPeers;
	}

	public void setHotPeers(HashMap<String, PipeAdvertisement> hotPeers) {
		this.hotPeers = hotPeers;
	}
	
	
	
	
}
