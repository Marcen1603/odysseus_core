package de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.listener;

import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.DDCAdvertisement;
import net.jxta.document.Advertisement;

public class DDCAdvertisementListener implements IDDCAdvertisementListener {

	private IP2PNetworkManager p2pNetworkManager;

	// called by OSGi-DS
	public void bindP2PNetworkManager(IP2PNetworkManager serv) {
		p2pNetworkManager = serv;
		p2pNetworkManager.addAdvertisementListener(this);
	}

	// called by OSGi-DS
	public void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		if (p2pNetworkManager == serv) {
			p2pNetworkManager.removeAdvertisementListener(this);
			p2pNetworkManager = null;
		}
	}

	@Override
	public void advertisementDiscovered(Advertisement advertisement) {
		if (advertisement instanceof DDCAdvertisement) {
			// DDCAdvertisement ddcAdvertisement = (DDCAdvertisement)
			// advertisement;

		}
	}

	@Override
	public void updateAdvertisements() {
		// TODO Auto-generated method stub

	}

}
