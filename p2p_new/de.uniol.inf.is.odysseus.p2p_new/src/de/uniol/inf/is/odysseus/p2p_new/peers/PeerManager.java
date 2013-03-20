package de.uniol.inf.is.odysseus.p2p_new.peers;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.peer.PeerID;
import net.jxta.protocol.PeerAdvertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

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
	private List<String> knownPeerIDs = Lists.newArrayList();

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

	private void firePeerLostEvents(List<String> oldIDs) {
		for (final String lostPeer : oldIDs) {
			firePeerLostEvent(lostPeer);

			try {
				P2PNewPlugIn.getDiscoveryService().flushAdvertisements(lostPeer, DiscoveryService.PEER);
			} catch (final IOException ex) {
				LOG.error("Could not flush advertisement with id {}", lostPeer, ex);
			}
		}
	}

	private void processPeerAdvertisements(Enumeration<Advertisement> advertisements) {
		final List<String> newIds = Lists.newArrayList();
		List<String> oldIDs = null;
		synchronized (knownPeerIDs) {
			oldIDs = Lists.newArrayList(knownPeerIDs);
		}

		while (advertisements.hasMoreElements()) {
			final PeerAdvertisement peerAdvertisement = (PeerAdvertisement) advertisements.nextElement();
			final PeerID peerID = peerAdvertisement.getPeerID();

			if (!peerID.equals(P2PNewPlugIn.getOwnPeerID()) && P2PNewPlugIn.getEndpointService().isReachable(peerID, false)) {
				final String advIDString = peerAdvertisement.getID().toString();
				newIds.add(advIDString);
				if (oldIDs.contains(advIDString)) {
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
