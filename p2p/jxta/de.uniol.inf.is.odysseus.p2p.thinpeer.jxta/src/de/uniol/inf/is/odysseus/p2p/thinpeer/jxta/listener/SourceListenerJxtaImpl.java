package de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.listener;

import java.util.Enumeration;

import net.jxta.discovery.DiscoveryEvent;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.protocol.DiscoveryResponseMsg;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.SourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p.thinpeer.listener.ISourceListener;
import de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.ThinPeerJxtaImpl;

public class SourceListenerJxtaImpl implements ISourceListener,
		DiscoveryListener {

	//Wie oft soll nach neuen Quellen gesucht werden
	private int WAIT_TIME=10000;
	
	//Wieviele Advertisements pro Peer
	private int ADVS_PER_PEER=20;

	private ThinPeerJxtaImpl thinPeerJxtaImpl;
	
	public SourceListenerJxtaImpl(ThinPeerJxtaImpl thinPeerJxtaImpl) {
		this.thinPeerJxtaImpl = thinPeerJxtaImpl;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(WAIT_TIME);
			} catch (InterruptedException e) {
			}
			thinPeerJxtaImpl.getDiscoveryService().getRemoteAdvertisements(null,
					DiscoveryService.ADV, "sourceName", "*", ADVS_PER_PEER, this);
		}
	}

	@Override
	public void discoveryEvent(DiscoveryEvent ev) {
		DiscoveryResponseMsg res = ev.getResponse();
		SourceAdvertisement adv = null;
		Enumeration<Advertisement> en = res.getAdvertisements();
		if (en != null) {
			while (en.hasMoreElements()) {
				try {
					Object temp2 = en.nextElement();
					if (temp2 instanceof SourceAdvertisement) {
						adv = (SourceAdvertisement) temp2;
						thinPeerJxtaImpl.getSources().put(adv.getSourceName(),
								adv);
					} else {
						return;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
	}

}
