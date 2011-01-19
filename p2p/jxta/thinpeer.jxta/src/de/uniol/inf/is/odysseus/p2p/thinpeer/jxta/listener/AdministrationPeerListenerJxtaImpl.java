package de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.listener;

import java.io.IOException;
import java.util.Enumeration;

import net.jxta.discovery.DiscoveryEvent;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.protocol.DiscoveryResponseMsg;
import de.uniol.inf.is.odysseus.p2p.thinpeer.listener.IAdministrationPeerListener;
import de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.ThinPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.ExtendedPeerAdvertisement;

public class AdministrationPeerListenerJxtaImpl implements
		IAdministrationPeerListener, DiscoveryListener {

	// Wie oft soll nach AdminPeers gesucht werden
	private int WAIT_TIME = 10000;

	// Wieviel Advertisements pro Peer
	private int ADVS_PER_PEER = 6;

	private ThinPeerJxtaImpl thinPeerJxtaImpl;

	public AdministrationPeerListenerJxtaImpl(ThinPeerJxtaImpl thinPeerJxtaImpl) {
		this.thinPeerJxtaImpl = thinPeerJxtaImpl;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(WAIT_TIME);
			} catch (InterruptedException e) {
			}
			thinPeerJxtaImpl.getAdminPeers().clear();
			try {
				thinPeerJxtaImpl.getDiscoveryService()
						.getLocalAdvertisements(DiscoveryService.ADV, "type",
								"AdministrationPeer");
			} catch (IOException e) {
				e.printStackTrace();
			}
			thinPeerJxtaImpl.getDiscoveryService()
					.getRemoteAdvertisements(null, DiscoveryService.ADV,
							"type", "AdministrationPeer", ADVS_PER_PEER, this);
		}
	}

	@Override
	public void discoveryEvent(DiscoveryEvent ev) {
		DiscoveryResponseMsg res = ev.getResponse();
		Enumeration<Advertisement> en = res.getAdvertisements();
		if (en != null) {
			while (en.hasMoreElements()) {
				Object o = en.nextElement();
				if (!(o instanceof ExtendedPeerAdvertisement)) {
					continue;
				}
				ExtendedPeerAdvertisement adv = (ExtendedPeerAdvertisement) o;
				synchronized (thinPeerJxtaImpl.getAdminPeers()) {
					thinPeerJxtaImpl.getAdminPeers().put(adv.getPeerId(), adv);
				}

			}
		}

	}

}
