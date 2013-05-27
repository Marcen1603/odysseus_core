package de.uniol.inf.is.odysseus.p2p_new.dictionary.peers;

import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import net.jxta.discovery.DiscoveryEvent;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.peer.PeerID;
import net.jxta.protocol.DiscoveryResponseMsg;
import net.jxta.protocol.PeerAdvertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.p2p_new.P2PNewPlugIn;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.impl.P2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.util.RepeatingJobThread;

public class PeerManager implements DiscoveryListener {

	private static final Logger LOG = LoggerFactory.getLogger(PeerManager.class);

	private static final String PEER_DISCOVERY_THREAD_NAME = "Peer discovery thread";
	private static final int PEER_DISCOVERY_INTERVAL_MILLIS = 5000;

	private RepeatingJobThread peerDiscoveryThread;

	// called by OSGi-DS
	public final void activate() {
		peerDiscoveryThread = new RepeatingJobThread(PEER_DISCOVERY_INTERVAL_MILLIS, PEER_DISCOVERY_THREAD_NAME) {
			@Override
			public void doJob() {
				P2PNewPlugIn.getDiscoveryService().getRemoteAdvertisements(null, DiscoveryService.PEER, null, null, 0, PeerManager.this);
			}
		};
		peerDiscoveryThread.start();

		LOG.debug("Peer manager activated");
	}
	
	// called bei JXTA
	@Override
	public void discoveryEvent(DiscoveryEvent event) {
		final DiscoveryResponseMsg response = event.getResponse();
		final Enumeration<Advertisement> advs = response.getAdvertisements();
		
		List<PeerAdvertisement> peerAdvs = Lists.newArrayList();
		while (advs.hasMoreElements()) {
			Advertisement adv = advs.nextElement();
			if( adv instanceof PeerAdvertisement ) {
				PeerAdvertisement peerAdv = (PeerAdvertisement)adv;
				peerAdvs.add(peerAdv);
			}
		}
		processPeerAdvertisements(peerAdvs);
	}

	// called by OSGi-DS
	public final void deactivate() {
		peerDiscoveryThread.stopRunning();

		LOG.debug("Peer manager deactivated");
	}

	private void processPeerAdvertisements(List<PeerAdvertisement> advertisements) {
		if( !P2PDictionary.isActivated() ) {
			return;
		}
		
		final Map<PeerID, String> newIds = Maps.newHashMap();
		final Map<PeerID, String> oldIDs = Maps.newHashMap(P2PDictionary.getInstance().getKnownPeersMap());
		final PeerID localPeerID = P2PDictionary.getInstance().getLocalPeerID();

		for( PeerAdvertisement peerAdvertisement : advertisements ) {
			final PeerID peerID = peerAdvertisement.getPeerID();

			if (!peerID.equals(localPeerID) && P2PNewPlugIn.getEndpointService().isReachable(peerID, false)) {
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
