package de.uniol.inf.is.odysseus.p2p.jxta;

import java.util.HashMap;

import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.p2p.Subplan;


public class SubplanJxtaImpl extends Subplan{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1577798037401864479L;

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

	//TODO: HotPeer Nutzung sollte nicht fest sein, da es u.U. andere Strategien geben kann, daher sollte dies  durch den jewiligen Peer verwaltet werden und nicht im Subplan.
	public void setHotPeers(HashMap<String, PipeAdvertisement> hotPeers) {
		this.hotPeers = hotPeers;
	}
	
	
	
	
}
