package de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.DDCAdvertisement;
import de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.DDCAdvertisementType;
import net.jxta.document.Advertisement;

public class DDCAdvertisementListener implements IDDCAdvertisementListener {

	private static final Logger LOG = LoggerFactory
			.getLogger(DDCAdvertisementListener.class);

	private IP2PNetworkManager p2pNetworkManager;
	private List<UUID> receivedDDCAdvertisements = new ArrayList<UUID>();

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
			DDCAdvertisement ddcAdvertisement = (DDCAdvertisement) advertisement;

			// check if listener already received this ddc advertisement
			if (!receivedDDCAdvertisements.contains(ddcAdvertisement
					.getAdvertisementUid())) {
				receivedDDCAdvertisements.add(ddcAdvertisement
						.getAdvertisementUid());
				if (ddcAdvertisement.getType().equals(
						DDCAdvertisementType.ddcCreated)) {
					// TODO put entries into DDC
				} else if (ddcAdvertisement.getType().equals(
						DDCAdvertisementType.ddcUpdated)) {
					// TODO update DDC
				} else{					
					LOG.debug("Could not detect DDCAdvertisement type. Changes not processed to DDC");
				}
			}
		}
	}

	@Override
	public void updateAdvertisements() {
		// no operation
	}

}
