package de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.sender;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import net.jxta.document.AdvertisementFactory;
import net.jxta.id.IDFactory;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.peer.ddc.DDC;
import de.uniol.inf.is.odysseus.peer.ddc.DDCEntry;
import de.uniol.inf.is.odysseus.peer.ddc.IDDCListener;
import de.uniol.inf.is.odysseus.peer.ddc.MissingDDCEntryException;
import de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.DDCAdvertisement;
import de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.DDCAdvertisementType;
import de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.sender.DDCChange.DDCChangeType;

public class DDCAdvertisementGenerator implements IDDCListener {
	private static DDCAdvertisementGenerator instance;

	private static final int TIMEOUT_SECONDS = 300;
	private static final int WAITING_TIME_SECONDS = 1;
	private IP2PNetworkManager p2pNetworkManager;

	private List<DDCChange> ddcChanges = new ArrayList<DDCChange>();
	private Date lastDDCDistribution;

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
		DDC.getInstance().addListener(this);
	}

	public final void deactivate() {
		DDC.getInstance().removeListener(this);
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
			ddcAdvertisement.setType(DDCAdvertisementType.initialDistribution);

			for (String[] key : ddc.getKeys()) {
				try {
					ddcAdvertisement.addAddedEntry(ddc.get(key));
				} catch (MissingDDCEntryException e) {
					// do nothing
				}
			}
			
			return ddcAdvertisement;
		}
		return null;

	}

	public DDCAdvertisement generateChanges() {
		if (lastDDCDistribution == null) {
			return generate(DDC.getInstance());
		} else {
			DDCAdvertisement ddcAdvertisement = (DDCAdvertisement) AdvertisementFactory
					.newAdvertisement(DDCAdvertisement.getAdvertisementType());
			ddcAdvertisement.setID(IDFactory.newPipeID(p2pNetworkManager
					.getLocalPeerGroupID()));
			ddcAdvertisement.setOwnerPeerId(p2pNetworkManager.getLocalPeerID());
			UUID advertisementUid = UUID.randomUUID();
			ddcAdvertisement.setDDCAdvertisementUid(advertisementUid);
			ddcAdvertisement.setType(DDCAdvertisementType.changeDistribution);

			for (DDCChange ddcChange : ddcChanges) {
				switch (ddcChange.getDdcChangeType()) {
				case ddcEntryAdded:
					ddcAdvertisement.addAddedEntry(ddcChange.getDdcEntry());
					break;
				case ddcEntryRemoved:
					ddcAdvertisement.addRemovedEntry(ddcChange.getDdcEntry().getKey());
					break;
				}
			}
			ddcChanges.clear();
			return ddcAdvertisement;
		}
	}

	private boolean p2pNetworkAvailable() {
		long startTime = System.currentTimeMillis();

		while (!p2pNetworkManager.isStarted()) {
			waitSomeTime(WAITING_TIME_SECONDS * 1000);

			if ((System.currentTimeMillis() - startTime) > TIMEOUT_SECONDS * 1000) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void ddcEntryAdded(DDCEntry ddcEntry) {
		addChange(new DDCChange(ddcEntry, DDCChangeType.ddcEntryAdded));
	}

	@Override
	public void ddcEntryRemoved(DDCEntry ddcEntry) {
		addChange(new DDCChange(ddcEntry, DDCChangeType.ddcEntryRemoved));
	}

	private void addChange(DDCChange change) {
		ddcChanges.add(change);
	}

	public List<DDCChange> getDdcChanges() {
		return ddcChanges;
	}

	private void waitSomeTime(int milliSeconds) {
		try {
			Thread.sleep(milliSeconds);
		} catch (InterruptedException e) {
		}
	}
}
