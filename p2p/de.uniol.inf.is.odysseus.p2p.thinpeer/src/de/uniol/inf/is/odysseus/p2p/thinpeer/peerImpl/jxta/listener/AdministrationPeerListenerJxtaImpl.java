package de.uniol.inf.is.odysseus.p2p.thinpeer.peerImpl.jxta.listener;

import java.io.IOException;
import java.util.Enumeration;

import net.jxta.discovery.DiscoveryEvent;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.protocol.DiscoveryResponseMsg;
import de.uniol.inf.is.odysseus.p2p.thinpeer.listener.IAdministrationPeerListener;
import de.uniol.inf.is.odysseus.p2p.thinpeer.peerImpl.jxta.ThinPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.advertisements.ExtendedPeerAdvertisement;

public class AdministrationPeerListenerJxtaImpl implements
		IAdministrationPeerListener, DiscoveryListener {

	// Wie oft soll nach AdminPeers gesucht werden
	private int WAIT_TIME = 10000;

	// Wieviel Advertisements pro Peer
	private int ADVS_PER_PEER = 6;

	public AdministrationPeerListenerJxtaImpl() {
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(WAIT_TIME);
			} catch (InterruptedException e) {
			}
			ThinPeerJxtaImpl.adminPeers.clear();
			try {
				ThinPeerJxtaImpl.getInstance().getDiscoveryService()
						.getLocalAdvertisements(DiscoveryService.ADV, "type",
								"AdministrationPeer");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ThinPeerJxtaImpl.getInstance().getDiscoveryService()
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
				synchronized (ThinPeerJxtaImpl.adminPeers) {
					ThinPeerJxtaImpl.adminPeers.put(adv.getPeerId(), adv);
				}

			}
		}

	}

}
