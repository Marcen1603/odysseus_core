package de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.sender;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.jxta.document.AdvertisementFactory;
import net.jxta.id.IDFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.peer.ddc.DDC;
import de.uniol.inf.is.odysseus.peer.ddc.DDCEntry;
import de.uniol.inf.is.odysseus.peer.ddc.IDDCListener;
import de.uniol.inf.is.odysseus.peer.ddc.MissingDDCEntryException;
import de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.DistributedDataContainerAdvertisement;
import de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.DistributedDataContainerAdvertisementType;
import de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.sender.DistributedDataContainerChange.DistributedDataContainerChangeType;

/**
 * DDCAdvertisementGenerator generates DDCAdvertisements for distributing
 * DDCEntries to other peers
 * 
 * @author ChrisToenjesDeye
 * 
 */
public class DistributedDataContainerAdvertisementGenerator implements
		IDDCListener {
	private static final Logger LOG = LoggerFactory
			.getLogger(DistributedDataContainerAdvertisementGenerator.class);

	private static DistributedDataContainerAdvertisementGenerator instance;

	private static final int TIMEOUT_SECONDS = 300;
	private static final int WAITING_TIME_SECONDS = 1;
	private IP2PNetworkManager p2pNetworkManager;

	private List<DistributedDataContainerChange> ddcChanges = new ArrayList<DistributedDataContainerChange>();

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

	// called by OSGi-DS
	public final void activate() {
		instance = this;
		// listen for DDC changes
		DDC.getInstance().addListener(this);
	}

	// called by OSGi-DS
	public final void deactivate() {
		// stop listening for DDC changes
		DDC.getInstance().removeListener(this);
		instance = null;
	}

	public static DistributedDataContainerAdvertisementGenerator getInstance() {
		return instance;
	}

	/**
	 * Generate @DDCAdvertisement from all existing @DDCEntry's
	 * 
	 * @return DDCAdvertisement
	 */
	public DistributedDataContainerAdvertisement generate() {
		// P2P Network need to be available, before local PeerID can be detected
		// and DDCAdvertisement created
		if (p2pNetworkAvailable()) {
			DDC ddc = DDC.getInstance();
			DistributedDataContainerAdvertisement ddcAdvertisement = (DistributedDataContainerAdvertisement) AdvertisementFactory
					.newAdvertisement(DistributedDataContainerAdvertisement
							.getAdvertisementType());
			ddcAdvertisement.setID(IDFactory.newPipeID(p2pNetworkManager
					.getLocalPeerGroupID()));
			ddcAdvertisement.setOwnerPeerId(p2pNetworkManager.getLocalPeerID());
			UUID advertisementUid = UUID.randomUUID();
			ddcAdvertisement.setDDCAdvertisementUid(advertisementUid);
			ddcAdvertisement
					.setType(DistributedDataContainerAdvertisementType.initialDistribution);

			if (ddc.getKeys().size() == 0) {
				// no need to send an advertisement if DDC is empty
				LOG.debug("No DDCAdvertisement created, because there are no keys in DDC");
				return null;
			}

			// get all entries from DDC and add them to DDCAdvertisement
			for (String[] key : ddc.getKeys()) {
				try {
					ddcAdvertisement.addAddedEntry(ddc.get(key));
				} catch (MissingDDCEntryException e) {
					// do nothing
				}
			}
			ddcChanges.clear();
			return ddcAdvertisement;
		}
		return null;

	}

	/**
	 * Generate @DDCAdvertisement from all existing @DDCChange's
	 * 
	 * @return DDCAdvertisement or null if network is not available after
	 *         timeout or no changes in DDC
	 */
	public DistributedDataContainerAdvertisement generateChanges() {
		// P2P Network need to be available, before local PeerID can be detected
		// and DDCAdvertisement created
		if (p2pNetworkAvailable()) {
			DistributedDataContainerAdvertisement ddcAdvertisement = (DistributedDataContainerAdvertisement) AdvertisementFactory
					.newAdvertisement(DistributedDataContainerAdvertisement
							.getAdvertisementType());
			ddcAdvertisement.setID(IDFactory.newPipeID(p2pNetworkManager
					.getLocalPeerGroupID()));
			ddcAdvertisement.setOwnerPeerId(p2pNetworkManager.getLocalPeerID());
			UUID advertisementUid = UUID.randomUUID();
			ddcAdvertisement.setDDCAdvertisementUid(advertisementUid);
			ddcAdvertisement
					.setType(DistributedDataContainerAdvertisementType.changeDistribution);

			if (ddcChanges.isEmpty()) {
				// no need to send an advertisement if no changes exists
				LOG.debug("No DDCAdvertisement created, because there are no changes in DDC");
				return null;
			}

			// get all ddc changes and add them to DDCAdvertisement
			for (DistributedDataContainerChange ddcChange : ddcChanges) {
				switch (ddcChange.getDdcChangeType()) {
				case ddcEntryAdded:
					ddcAdvertisement.addAddedEntry(ddcChange.getDdcEntry());
					break;
				case ddcEntryRemoved:
					ddcAdvertisement.addRemovedEntry(ddcChange.getDdcEntry()
							.getKey());
					break;
				}
			}
			ddcChanges.clear();
			return ddcAdvertisement;
		}
		return null;
	}

	/**
	 * Wait until P2P network is available or timeout is over
	 * 
	 * @return true if P2P Network is available or false after timeout
	 */
	private boolean p2pNetworkAvailable() {
		long startTime = System.currentTimeMillis();

		while (!p2pNetworkManager.isStarted()) {
			waitSomeTime(WAITING_TIME_SECONDS * 1000);

			if ((System.currentTimeMillis() - startTime) > TIMEOUT_SECONDS * 1000) {
				LOG.warn("Waiting for P2P network is timeouted. DDCAdvertisement is not send");
				return false;
			}
		}
		return true;
	}

	@Override
	public void ddcEntryAdded(DDCEntry ddcEntry) {
		ddcChanges.add(new DistributedDataContainerChange(ddcEntry,
				DistributedDataContainerChangeType.ddcEntryAdded));
	}

	@Override
	public void ddcEntryRemoved(DDCEntry ddcEntry) {
		ddcChanges.add(new DistributedDataContainerChange(ddcEntry,
				DistributedDataContainerChangeType.ddcEntryRemoved));
	}

	public List<DistributedDataContainerChange> getDDCChanges() {
		return ddcChanges;
	}

	private void waitSomeTime(int milliSeconds) {
		try {
			Thread.sleep(milliSeconds);
		} catch (InterruptedException e) {
		}
	}
}
