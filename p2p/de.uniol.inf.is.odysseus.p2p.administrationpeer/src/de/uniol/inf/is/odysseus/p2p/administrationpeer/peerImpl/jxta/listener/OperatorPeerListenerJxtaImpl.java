package de.uniol.inf.is.odysseus.p2p.administrationpeer.peerImpl.jxta.listener;

import java.io.IOException;
import java.util.Enumeration;

import net.jxta.discovery.DiscoveryEvent;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.protocol.DiscoveryResponseMsg;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.listener.IOperatorPeerListener;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.peerImpl.jxta.AdministrationPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.advertisements.ExtendedPeerAdvertisement;

public class OperatorPeerListenerJxtaImpl implements IOperatorPeerListener,
		DiscoveryListener {
	// Wie oft soll nach AdminPeers gesucht werden
	private int WAIT_TIME = 10000;

	// Wieviel Advertisements pro Peer
	private int ADVS_PER_PEER = 6;

	public OperatorPeerListenerJxtaImpl() {
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(WAIT_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			AdministrationPeerJxtaImpl.getInstance().getOperatorPeers().clear();
			try {
				AdministrationPeerJxtaImpl.getInstance().getDiscoveryService()
						.getLocalAdvertisements(DiscoveryService.ADV, "type",
								"OperatorPeer");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			AdministrationPeerJxtaImpl.getInstance().getDiscoveryService()
					.getRemoteAdvertisements(null, DiscoveryService.ADV,
							"type", "OperatorPeer", ADVS_PER_PEER, this);
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
				synchronized (AdministrationPeerJxtaImpl.getInstance()
						.getOperatorPeers()) {
					AdministrationPeerJxtaImpl.getInstance().getOperatorPeers()
							.put(adv.getPeerId().toString(), adv);
				}

			}
		}

	}

}
