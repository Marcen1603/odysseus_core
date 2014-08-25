package de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.sender;

import net.jxta.document.AdvertisementFactory;
import net.jxta.id.IDFactory;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.peer.ddc.DDC;
import de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.DDCAdvertisement;

public class DDCAdvertisementGenerator {

	private IP2PNetworkManager p2pNetworkManager;
	private static DDCAdvertisementGenerator instance;

	// called by OSGi-DS
	public void bindP2PNetworkManager(IP2PNetworkManager serv) {
		p2pNetworkManager = serv;
	}

	// called by OSGi-DS
	public void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		if (p2pNetworkManager == serv) {
			p2pNetworkManager = null;
		}
	}
	
	public final void activate() {
		instance = this;
	}

	public final void deactivate() {

		instance = null;
	}

	public static DDCAdvertisementGenerator getInstance() {
		return instance;
	}
	

	public DDCAdvertisement generate(DDC ddc) {
		DDCAdvertisement ddcAdvertisement = (DDCAdvertisement) AdvertisementFactory
				.newAdvertisement(DDCAdvertisement.getAdvertisementType());
		ddcAdvertisement.setID(IDFactory.newPipeID(p2pNetworkManager
				.getLocalPeerGroupID()));
		ddcAdvertisement.setOwnerPeerId(p2pNetworkManager.getLocalPeerID());

		return ddcAdvertisement;
	}

	public DDCAdvertisement generateChanges(DDC ddc) {
		DDCAdvertisement ddcAdvertisement = (DDCAdvertisement) AdvertisementFactory
				.newAdvertisement(DDCAdvertisement.getAdvertisementType());
		ddcAdvertisement.setID(IDFactory.newPipeID(p2pNetworkManager
				.getLocalPeerGroupID()));
		ddcAdvertisement.setOwnerPeerId(p2pNetworkManager.getLocalPeerID());

		return ddcAdvertisement;
	}

}
