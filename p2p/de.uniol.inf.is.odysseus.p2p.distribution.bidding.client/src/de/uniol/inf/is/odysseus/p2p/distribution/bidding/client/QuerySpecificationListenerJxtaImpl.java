package de.uniol.inf.is.odysseus.p2p.distribution.bidding.client;

import java.util.Enumeration;
import net.jxta.discovery.DiscoveryEvent;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.protocol.DiscoveryResponseMsg;
//import de.uniol.inf.is.odysseus.p2p.operatorpeer.jxta.OperatorPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.QueryExecutionSpezification;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.PeerGroupTool;
import de.uniol.inf.is.odysseus.p2p.peer.AbstractPeer;

public class QuerySpecificationListenerJxtaImpl implements
		IQuerySpecificationListener, DiscoveryListener {

	private AbstractPeer aPeer;
	
	public QuerySpecificationListenerJxtaImpl(AbstractPeer aPeer) {
		PeerGroupTool.getPeerGroup().getDiscoveryService().addDiscoveryListener(this);
		this.aPeer = aPeer;
	}

	public void run() {
		while (true) {
			try {
				Thread.sleep(6000);
			} catch (InterruptedException e) {
			}
			PeerGroupTool.getPeerGroup().getDiscoveryService().getRemoteAdvertisements(null, DiscoveryService.ADV,
							"type", "QueryExecutionAdv", 6, null);
		}
	}

	@Override
	public synchronized void discoveryEvent(DiscoveryEvent ev) {


		DiscoveryResponseMsg res = ev.getResponse();
		Enumeration<Advertisement> en = res.getAdvertisements();
		if (en != null) {
			while (en.hasMoreElements()) {

				Object temp2 = en.nextElement();
				if (temp2 instanceof QueryExecutionSpezification) {
					System.out.println("Eine QueryExecutionSpezifikation zu subplan " +((QueryExecutionSpezification)temp2).getSubplanId());

//					if(!this.qes.containsKey(((QueryExecutionSpezification)temp2).getID().toString())) {
//						this.qes.put(((QueryExecutionSpezification)temp2).getID().toString(), ((QueryExecutionSpezification)temp2));
					new QuerySpecificationHandlerJxtaImpl((QueryExecutionSpezification) temp2, aPeer) ;
//					}
				} else {
					return;
				}
			}

		}

	}
	
	public AbstractPeer getaPeer() {
		return aPeer;
	}
}
