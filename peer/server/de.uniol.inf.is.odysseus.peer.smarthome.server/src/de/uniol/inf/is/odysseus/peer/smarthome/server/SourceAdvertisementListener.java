package de.uniol.inf.is.odysseus.peer.smarthome.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jxta.document.Advertisement;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementDiscovererListener;
import de.uniol.inf.is.odysseus.p2p_new.InvalidP2PSource;
import de.uniol.inf.is.odysseus.p2p_new.PeerException;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.MultipleSourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.SourceAdvertisement;
import de.uniol.inf.is.odysseus.peer.smarthome.server.advertisement.SmartDeviceAdvertisement;

public class SourceAdvertisementListener implements IAdvertisementDiscovererListener {
	private static final Logger LOG = LoggerFactory
			.getLogger(SmartHomeServerPlugIn.class);
	private static SourceAdvertisementListener instance;

	@Override
	public void advertisementDiscovered(Advertisement advertisement) {
		//LOG.debug("advertisementDiscovered AdvType:"
		//+ advertisement.getAdvType());

		if (advertisement != null
				&& advertisement instanceof SmartDeviceAdvertisement
				&& advertisement.getAdvType().equals(
						SmartDeviceAdvertisement.getAdvertisementType())) {

			SmartDeviceAdvertisement adv = (SmartDeviceAdvertisement) advertisement;
			SmartHomeServerPlugIn.getSmartDeviceInformations(adv);

		} else if (advertisement != null
				&& advertisement instanceof SourceAdvertisement
				&& advertisement.getAdvType().equals(
						SourceAdvertisement.getAdvertisementType())) {
			SourceAdvertisement srcAdv = (SourceAdvertisement) advertisement;

			SmartHomeServerPlugIn.importIfNeccessary(srcAdv);
		} else if (advertisement != null
				&& advertisement instanceof MultipleSourceAdvertisement
				&& advertisement.getAdvType().equals(
						MultipleSourceAdvertisement.getAdvertisementType())) {
			MultipleSourceAdvertisement multiSourceAdv = (MultipleSourceAdvertisement) advertisement;

			String peerIDStr = multiSourceAdv.getPeerID().intern().toString();

			// TODO:
			if (SmartHomeServerPlugIn.getSourcesNeededForImport()
					.containsValue(peerIDStr)) {
				for (SourceAdvertisement sAdv : multiSourceAdv
						.getSourceAdvertisements()) {
					try {
						LOG.debug("---importIfNeccessary---Source:"
								+ sAdv.getName());

						if (!SmartHomeServerPlugIn.getP2PDictionary()
								.isImported(sAdv.getName())) {
							LOG.debug("---importIfNeccessary---importSource:"
									+ sAdv.getName());

							SmartHomeServerPlugIn.getP2PDictionary()
									.importSource(sAdv, sAdv.getName());

							SmartHomeServerPlugIn
									.removeSourceNeededForImport(sAdv.getName());
						}
					} catch (PeerException e) {
						e.printStackTrace();
					} catch (InvalidP2PSource e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public void updateAdvertisements() {

	}

	public static SourceAdvertisementListener getInstance() {
		if(instance==null){
			instance = new SourceAdvertisementListener();
		}
		return instance;
	}
}
