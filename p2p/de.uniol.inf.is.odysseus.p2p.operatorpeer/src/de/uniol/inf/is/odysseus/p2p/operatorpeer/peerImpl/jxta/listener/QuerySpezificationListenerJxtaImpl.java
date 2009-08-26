package de.uniol.inf.is.odysseus.p2p.operatorpeer.peerImpl.jxta.listener;

import java.util.Enumeration;

import net.jxta.discovery.DiscoveryEvent;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.protocol.DiscoveryResponseMsg;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.listener.IQuerySpezificationListener;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.peerImpl.jxta.OperatorPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.peerImpl.jxta.handler.QuerySpezificationHandlerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.advertisements.QueryExecutionSpezification;

public class QuerySpezificationListenerJxtaImpl implements
		IQuerySpezificationListener, DiscoveryListener {

	public QuerySpezificationListenerJxtaImpl() {

		OperatorPeerJxtaImpl.getInstance().getDiscoveryService()
				.addDiscoveryListener(this);
	}

	public void run() {
		while (true) {
			try {
				Thread.sleep(6000);
			} catch (InterruptedException e) {
			}
			OperatorPeerJxtaImpl.getInstance().getDiscoveryService()
					.getRemoteAdvertisements(null, DiscoveryService.ADV,
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
					QuerySpezificationHandlerJxtaImpl
							.handleQuerySpezification((QueryExecutionSpezification) temp2);
				} else {
					return;
				}
			}

		}

	}
}
