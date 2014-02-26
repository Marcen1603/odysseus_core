package de.uniol.inf.is.odysseus.p2p_new.dictionary.peers;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import net.jxta.document.Advertisement;
import net.jxta.peer.PeerID;
import net.jxta.protocol.PeerAdvertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.p2p_new.dictionary.impl.P2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.network.P2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.provider.JxtaServicesProvider;
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
				if( JxtaServicesProvider.isActivated() ) {
					try {
						JxtaServicesProvider.getInstance().getRemotePeerAdvertisements();
					
						Enumeration<Advertisement> advs = JxtaServicesProvider.getInstance().getPeerAdvertisements();
						List<PeerAdvertisement> peerAdvs = Lists.newArrayList();
						while (advs.hasMoreElements()) {
							Advertisement adv = advs.nextElement();
							if( adv instanceof PeerAdvertisement ) {
								PeerAdvertisement peerAdv = (PeerAdvertisement)adv;
								peerAdvs.add(peerAdv);
							}
						}
						processPeerAdvertisements(peerAdvs);
						
					} catch (IOException e) {
						LOG.error("Could not get local advertisements");
					}
				}
			}
		};
		peerDiscoveryThread.start();

		LOG.debug("Peer manager activated");
	}
	
	// called by OSGi-DS
	public final void deactivate() {
		peerDiscoveryThread.stopRunning();

		LOG.debug("Peer manager deactivated");
	}

	private static void processPeerAdvertisements(List<PeerAdvertisement> advertisements) {
		if( !P2PDictionary.isActivated() ) {
			return;
		}
		
		final Map<PeerID, String> newIds = Maps.newHashMap();
		final Map<PeerID, String> oldIDs = Maps.newHashMap(P2PDictionary.getInstance().getKnownPeersMap());
		final PeerID localPeerID = P2PNetworkManager.getInstance().getLocalPeerID();

		for( PeerAdvertisement peerAdvertisement : advertisements ) {
			final PeerID peerID = peerAdvertisement.getPeerID();

			if (!peerID.equals(localPeerID) && JxtaServicesProvider.getInstance().isReachable(peerID)) {
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
