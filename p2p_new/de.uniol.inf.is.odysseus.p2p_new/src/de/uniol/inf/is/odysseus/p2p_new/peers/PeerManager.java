package de.uniol.inf.is.odysseus.p2p_new.peers;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.peer.PeerID;
import net.jxta.protocol.PeerAdvertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.p2p_new.IPeerListener;
import de.uniol.inf.is.odysseus.p2p_new.IPeerManager;
import de.uniol.inf.is.odysseus.p2p_new.P2PNewPlugIn;
import de.uniol.inf.is.odysseus.p2p_new.util.RepeatingJobThread;

public class PeerManager implements IPeerManager {

	private static final Logger LOG = LoggerFactory.getLogger(PeerManager.class);

	private static final String PEER_DISCOVERY_THREAD_NAME = "Peer discovery thread";
	private static final int PEER_DISCOVERY_INTERVAL_MILLIS = 5000;

	private final List<IPeerListener> listeners = Lists.newArrayList();

	private RepeatingJobThread peerDiscoveryThread;
	private Map<String, String> knownPeerIDs = Maps.newHashMap();

	// called by OSGi-DS
	public final void activate() {
		peerDiscoveryThread = new RepeatingJobThread(PEER_DISCOVERY_INTERVAL_MILLIS, PEER_DISCOVERY_THREAD_NAME) {
			@Override
			public void doJob() {
				try {
					final Enumeration<Advertisement> advertisements = P2PNewPlugIn.getDiscoveryService().getLocalAdvertisements(DiscoveryService.PEER, null, null);
					processPeerAdvertisements(advertisements);
				} catch (final IOException ex) {
					LOG.error("Could not get peer advertisements", ex);
				}
			}
		};
		peerDiscoveryThread.start();

		LOG.debug("Peer manager activated");
	}

	@Override
	public void addListener(IPeerListener listener) {
		Preconditions.checkNotNull(listener, "Peer listener must not be null!");
		synchronized (listeners) {
			listeners.add(listener);
		}
	}

	// called by OSGi-DS
	public void bindPeerListener(IPeerListener listener) {
		addListener(listener);

		LOG.debug("Bound peer listener {}", listener);
	}

	@Override
	public void checkNewPeers() {
		peerDiscoveryThread.doJob();
	}

	// called by OSGi-DS
	public final void deactivate() {
		peerDiscoveryThread.stopRunning();

		LOG.debug("Peer manager deactivated");
	}

	@Override
	public ImmutableCollection<String> getPeerIDs() {
		return ImmutableSet.copyOf(knownPeerIDs.keySet());
	}

	@Override
	public Optional<String> getPeerName(String peerID) {
		return Optional.fromNullable(knownPeerIDs.get(peerID));
	}

	@Override
	public void removeListener(IPeerListener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}

	// called by OSGi-DS
	public void unbindPeerListener(IPeerListener listener) {
		removeListener(listener);

		LOG.debug("Unbound peer listener {}", listener);
	}

	protected final void fireNewPeerEvent(String peerID) {
		synchronized (listeners) {
			for (final IPeerListener listener : listeners) {
				try {
					listener.onPeerFound(this, peerID);
				} catch (final Throwable t) {
					LOG.error("Exception during fireing new peer event", t);
				}
			}
		}
	}

	protected final void firePeerLostEvent(String peerID) {
		synchronized (listeners) {
			for (final IPeerListener listener : listeners) {
				try {
					listener.onPeerLost(this, peerID);
				} catch (final Throwable t) {
					LOG.error("Exception during fireing peer lost event", t);
				}
			}
		}
	}

	private void firePeerLostEvents(Map<String, String> lostPeerIDs) {
		for (final String lostPeerID : lostPeerIDs.keySet()) {
			firePeerLostEvent(lostPeerID);

			try {
				P2PNewPlugIn.getDiscoveryService().flushAdvertisements(lostPeerID, DiscoveryService.PEER);
			} catch (final IOException ex) {
				LOG.error("Could not flush advertisement with id {}", lostPeerID, ex);
			}
		}
	}

	private void processPeerAdvertisements(Enumeration<Advertisement> advertisements) {
		final Map<String, String> newIds = Maps.newHashMap();
		Map<String, String> oldIDs = null;
		synchronized (knownPeerIDs) {
			oldIDs = Maps.newHashMap(knownPeerIDs);
		}

		while (advertisements.hasMoreElements()) {
			final PeerAdvertisement peerAdvertisement = (PeerAdvertisement) advertisements.nextElement();
			final PeerID peerID = peerAdvertisement.getPeerID();

			if (!peerID.equals(P2PNewPlugIn.getOwnPeerID()) && P2PNewPlugIn.getEndpointService().isReachable(peerID, false)) {
				final String advIDString = peerAdvertisement.getID().toString();
				newIds.put(advIDString, peerAdvertisement.getName());
				if (oldIDs.containsKey(advIDString)) {
					oldIDs.remove(advIDString);
				} else {
					fireNewPeerEvent(advIDString);
				}
			}
		}

		firePeerLostEvents(oldIDs);

		synchronized (knownPeerIDs) {
			knownPeerIDs = newIds;
		}

	}
}
