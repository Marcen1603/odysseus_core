package de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.sender;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.jxta.document.AdvertisementFactory;
import net.jxta.id.IDFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.peer.ddc.DDCEntry;
import de.uniol.inf.is.odysseus.peer.ddc.IDDCListener;
import de.uniol.inf.is.odysseus.peer.ddc.IDistributedDataContainer;
import de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.DistributedDataContainerAdvertisement;
import de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.DistributedDataContainerChangeAdvertisement;
import de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.DistributedDataContainerRequestAdvertisement;
import de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.sender.DistributedDataContainerChange.DistributedDataContainerChangeType;

/**
 * DDCAdvertisementGenerator generates DDCAdvertisements for distributing
 * DDCEntries to other peers
 * 
 * @author ChrisToenjesDeye, Michael Brand
 * 
 */
public class DistributedDataContainerAdvertisementGenerator implements
		IDDCListener {

	private static final Logger LOG = LoggerFactory
			.getLogger(DistributedDataContainerAdvertisementGenerator.class);

	private static DistributedDataContainerAdvertisementGenerator instance;

	private static final int TIMEOUT_SECONDS = 300;
	private static final int WAITING_TIME_SECONDS = 1;
	private static IDistributedDataContainer ddc;

	private IP2PNetworkManager p2pNetworkManager;
	private Set<DistributedDataContainerChange> ddcChanges = Sets.newHashSet();
	private Map<UUID, ImmutableSet<DistributedDataContainerChange>> ddcChangesToSentAdv = Maps
			.newHashMap();
	private boolean listeningForChanges = true;

	/**
	 * Binds a DDC. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param ddc
	 *            The DDC to bind. <br />
	 *            Must be not null.
	 */
	public static void bindDDC(IDistributedDataContainer ddc) {

		Preconditions.checkNotNull(ddc, "The DDC to bind must be not null!");
		DistributedDataContainerAdvertisementGenerator.ddc = ddc;
		DistributedDataContainerAdvertisementGenerator.LOG.debug(
				"Bound {} as a DDC", ddc.getClass().getSimpleName());

	}

	/**
	 * Removes the binding for a DDC. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param ddc
	 *            The DDC to unbind. <br />
	 *            Must be not null.
	 */
	public static void unbindDDC(IDistributedDataContainer ddc) {

		Preconditions.checkNotNull(ddc, "The DDC to bind must be not null!");
		if (DistributedDataContainerAdvertisementGenerator.ddc == ddc) {

			DistributedDataContainerAdvertisementGenerator.ddc = null;
			DistributedDataContainerAdvertisementGenerator.LOG.debug(
					"Unbound {} as a DDC", ddc.getClass().getSimpleName());

		}

	}

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
	}

	// called by OSGi-DS
	public final void deactivate() {
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
			DistributedDataContainerAdvertisement ddcAdvertisement = (DistributedDataContainerAdvertisement) AdvertisementFactory
					.newAdvertisement(DistributedDataContainerAdvertisement
							.getAdvertisementType());
			ddcAdvertisement.setID(IDFactory.newPipeID(p2pNetworkManager
					.getLocalPeerGroupID()));
			ddcAdvertisement.setInitiatingPeerId(p2pNetworkManager
					.getLocalPeerID());
			UUID advertisementUid = UUID.randomUUID();
			ddcAdvertisement.setDDCAdvertisementUid(advertisementUid);
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
	public DistributedDataContainerChangeAdvertisement generateChanges() {
		// P2P Network need to be available, before local PeerID can be detected
		// and DDCAdvertisement created
		if (p2pNetworkAvailable()) {
			DistributedDataContainerChangeAdvertisement ddcAdvertisement = (DistributedDataContainerChangeAdvertisement) AdvertisementFactory
					.newAdvertisement(DistributedDataContainerChangeAdvertisement
							.getAdvertisementType());
			ddcAdvertisement.setID(IDFactory.newPipeID(p2pNetworkManager
					.getLocalPeerGroupID()));
			ddcAdvertisement.setInitiatingPeerId(p2pNetworkManager
					.getLocalPeerID());
			UUID advertisementUid = UUID.randomUUID();
			ddcAdvertisement.setDDCAdvertisementUid(advertisementUid);

			if (ddcChanges.isEmpty()) {
				// no need to send an advertisement if no changes exists
				LOG.debug("No DDCAdvertisement created, because there are no changes in DDC");
				return null;
			}

			ddcChangesToSentAdv.put(advertisementUid,
					ImmutableSet.copyOf(ddcChanges));
			ddcChanges.clear();
			return ddcAdvertisement;
		}
		return null;
	}

	/**
	 * Generate @DDCRequestAdvertisement.
	 * 
	 * @return DDCRequestAdvertisement
	 */
	public DistributedDataContainerRequestAdvertisement generateRequest() {
		// P2P Network need to be available, before local PeerID can be detected
		// and DDCAdvertisement created
		if (p2pNetworkAvailable()) {
			DistributedDataContainerRequestAdvertisement ddcAdvertisement = (DistributedDataContainerRequestAdvertisement) AdvertisementFactory
					.newAdvertisement(DistributedDataContainerRequestAdvertisement
							.getAdvertisementType());
			ddcAdvertisement.setID(IDFactory.newPipeID(p2pNetworkManager
					.getLocalPeerGroupID()));
			ddcAdvertisement.setInitiatingPeerId(p2pNetworkManager
					.getLocalPeerID());
			UUID advertisementUid = UUID.randomUUID();
			ddcAdvertisement.setDDCAdvertisementUid(advertisementUid);
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
		if (listeningForChanges) {
			ddcChanges.add(new DistributedDataContainerChange(ddcEntry,
					DistributedDataContainerChangeType.ddcEntryAdded));
		}
	}

	@Override
	public void ddcEntryRemoved(DDCEntry ddcEntry) {
		if (listeningForChanges) {
			ddcChanges.add(new DistributedDataContainerChange(ddcEntry,
					DistributedDataContainerChangeType.ddcEntryRemoved));
		}
	}

	private void waitSomeTime(int milliSeconds) {
		try {
			Thread.sleep(milliSeconds);
		} catch (InterruptedException e) {
		}
	}

	public void disableListeningForChanges() {
		listeningForChanges = false;
	}

	public void enableListeningForChanges() {
		listeningForChanges = true;
	}

	public Set<DistributedDataContainerChange> getDDCChanges(UUID advId) {
		if (ddcChangesToSentAdv.containsKey(advId)) {
			return ddcChangesToSentAdv.get(advId);
		}
		return Sets.newHashSet();
	}
}
