package de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.jxta.document.Advertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.peer.ddc.DDC;
import de.uniol.inf.is.odysseus.peer.ddc.DDCEntry;
import de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.DistributedDataContainerAdvertisement;

/**
 * Listener for DDCAdvertisements. The Changes from Advertisements will be
 * written to DDC
 * 
 * @author ChrisToenjesDeye
 * 
 */
public class DistributedDataContainerAdvertisementListener implements
		IDistributedDataContainerAdvertisementListener {

	private static final Logger LOG = LoggerFactory
			.getLogger(DistributedDataContainerAdvertisementListener.class);

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

	/**
	 * Processes DDCAdvertisements and writes changes to DDC. If
	 * DDCAdvertisement is already processed it will be ignored
	 */
	@Override
	public void advertisementDiscovered(Advertisement advertisement) {
		if (advertisement instanceof DistributedDataContainerAdvertisement) {
			DistributedDataContainerAdvertisement ddcAdvertisement = (DistributedDataContainerAdvertisement) advertisement;
			// check if listener already received this ddc advertisement
			if (!receivedDDCAdvertisements.contains(ddcAdvertisement
					.getAdvertisementUid())) {
				receivedDDCAdvertisements.add(ddcAdvertisement
						.getAdvertisementUid());

				DDC ddc = DDC.getInstance();
				switch (ddcAdvertisement.getType()) {
				case initialDistribution:
					// write only added DDC entries to DDC on initial
					// distribution
					if (ddcAdvertisement.getAddedDDCEntires() != null) {
						for (DDCEntry addedDdcEntry : ddcAdvertisement
								.getAddedDDCEntires()) {
							ddc.add(addedDdcEntry);
						}
					}
					break;
				case changeDistribution:
					// write added entries in DDC
					if (ddcAdvertisement.getAddedDDCEntires() != null) {
						for (DDCEntry addedDdcEntry : ddcAdvertisement
								.getAddedDDCEntires()) {
							ddc.add(addedDdcEntry);
						}
					}
					// remove entries from DDC
					if (ddcAdvertisement.getRemovedDDCEntires() != null) {
						for (String[] deletedDdcEntryKey : ddcAdvertisement
								.getRemovedDDCEntires()) {
							ddc.remove(deletedDdcEntryKey);
						}
					}
					break;
				default:
					LOG.debug("Could not detect DDCAdvertisement type. Changes not processed to DDC");
					break;
				}

			}
		}
	}

	@Override
	public void updateAdvertisements() {
		// no operation
	}

}
