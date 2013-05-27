package de.uniol.inf.is.odysseus.p2p_new.peers;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.peer.PeerID;
import net.jxta.protocol.PeerAdvertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.p2p_new.P2PNewPlugIn;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.impl.P2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.util.RepeatingJobThread;

public class PeerManager {

	private static final Logger LOG = LoggerFactory.getLogger(PeerManager.class);

	private static final String PEER_DISCOVERY_THREAD_NAME = "Peer discovery thread";
	private static final int PEER_DISCOVERY_INTERVAL_MILLIS = 5000;

	private RepeatingJobThread peerDiscoveryThread;

	// called by OSGi-DS
	public final void activate() {
		peerDiscoveryThread = new RepeatingJobThread(PEER_DISCOVERY_INTERVAL_MILLIS, PEER_DISCOVERY_THREAD_NAME) {
			@Override
			public void doJob() {
				try {
					P2PNewPlugIn.getDiscoveryService().getRemoteAdvertisements(null, DiscoveryService.PEER, null, null, 0, null);
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

	public void checkNewPeers() {
		peerDiscoveryThread.doJob();
	}

	// called by OSGi-DS
	public final void deactivate() {
		peerDiscoveryThread.stopRunning();

		LOG.debug("Peer manager deactivated");
	}

	private void processPeerAdvertisements(Enumeration<Advertisement> advertisements) {
		if( !P2PDictionary.isActivated() ) {
			return;
		}
		
		final Map<PeerID, String> newIds = Maps.newHashMap();
		
		Map<PeerID, String> oldIDs = null;
		oldIDs = Maps.newHashMap(P2PDictionary.getInstance().getKnownPeersMap());

		while (advertisements.hasMoreElements()) {
			final PeerAdvertisement peerAdvertisement = (PeerAdvertisement) advertisements.nextElement();
			final PeerID peerID = peerAdvertisement.getPeerID();

			if (!peerID.equals(P2PDictionary.getInstance().getLocalPeerID()) && P2PNewPlugIn.getEndpointService().isReachable(peerID, false)) {
				final String peerName = peerAdvertisement.getName();
				
				newIds.put(peerID, peerName);
				if (oldIDs.containsKey(peerID)) {
					oldIDs.remove(peerID);
				} else {
					P2PDictionary.getInstance().addPeer(peerID, peerName);
				}
			}
		}

		for( PeerID lostPeerID : oldIDs.keySet() ) {
			P2PDictionary.getInstance().removePeer(lostPeerID);
		}
	}
}
