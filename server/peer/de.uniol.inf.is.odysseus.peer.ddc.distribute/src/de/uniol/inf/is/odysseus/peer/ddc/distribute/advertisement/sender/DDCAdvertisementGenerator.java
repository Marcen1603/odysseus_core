package de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.sender;

import java.util.Date;
import java.util.UUID;

import net.jxta.document.AdvertisementFactory;
import net.jxta.id.IDFactory;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.peer.ddc.DDC;
import de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.DDCAdvertisement;

public class DDCAdvertisementGenerator {

	private static final int TIMEOUT_SECONDS = 300;
	private IP2PNetworkManager p2pNetworkManager;
	private Date lastDDCDistribution;
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
		lastDDCDistribution = new Date();
		DDCAdvertisement ddcAdvertisement = (DDCAdvertisement) AdvertisementFactory
				.newAdvertisement(DDCAdvertisement.getAdvertisementType());
		if (p2pNetworkAvailable()) {
			ddcAdvertisement.setID(IDFactory.newPipeID(p2pNetworkManager
					.getLocalPeerGroupID()));
			ddcAdvertisement.setOwnerPeerId(p2pNetworkManager.getLocalPeerID());
			UUID advertisementUid = UUID.randomUUID();
			ddcAdvertisement.setDDCAdvertisementUid(advertisementUid);

			return ddcAdvertisement;
		}
		return null;
	}

	private boolean p2pNetworkAvailable() {
		long startTime = System.currentTimeMillis();

		while (!p2pNetworkManager.isStarted()) {
			waitSomeTime(2000);

			if ((System.currentTimeMillis() - startTime) > TIMEOUT_SECONDS * 1000) {
				return false;
			}
		}
		return true;
	}

	private void waitSomeTime(int milliSeconds) {
		try {
			Thread.sleep(milliSeconds);
		} catch (InterruptedException e) {
		}
	}

	public DDCAdvertisement generateChanges(DDC ddc) {
		if (lastDDCDistribution == null) {
			return generate(ddc);
		} else {
			DDCAdvertisement ddcAdvertisement = (DDCAdvertisement) AdvertisementFactory
					.newAdvertisement(DDCAdvertisement.getAdvertisementType());
			ddcAdvertisement.setID(IDFactory.newPipeID(p2pNetworkManager
					.getLocalPeerGroupID()));
			ddcAdvertisement.setOwnerPeerId(p2pNetworkManager.getLocalPeerID());
			UUID advertisementUid = UUID.randomUUID();
			ddcAdvertisement.setDDCAdvertisementUid(advertisementUid);

			// detect ddc changes and put them into advertisement

			return ddcAdvertisement;
		}
	}

}
