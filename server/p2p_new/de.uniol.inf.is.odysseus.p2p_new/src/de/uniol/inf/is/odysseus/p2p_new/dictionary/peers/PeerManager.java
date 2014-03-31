package de.uniol.inf.is.odysseus.p2p_new.dictionary.peers;

import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import net.jxta.discovery.DiscoveryEvent;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.document.Advertisement;
import net.jxta.peer.PeerID;
import net.jxta.protocol.PeerAdvertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.p2p_new.communication.PeerCloseMessage;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.impl.P2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.network.P2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.provider.JxtaServicesProvider;
import de.uniol.inf.is.odysseus.p2p_new.util.RepeatingJobThread;

public class PeerManager implements IPeerCommunicatorListener, DiscoveryListener {

	private static final Logger LOG = LoggerFactory.getLogger(PeerManager.class);

	private static final String PEER_DISCOVERY_THREAD_NAME = "Peer discovery";
	private static final int PEER_DISCOVERY_INTERVAL_MILLIS = 2000;
	private static final int PEER_LOSS_THREASHOLD = 3;
	
	private static IPeerCommunicator peerCommunicator;

	private final Map<PeerID, Integer> potencialPeerLossMap = Maps.newHashMap();
	private final Collection<PeerID> gracefullyDisconnectedPeers = Lists.newArrayList();
	private final Object dictionaryObject = new Object();
	
	private RepeatingJobThread peerDiscoveryThread;
	private Boolean onDiscovering = false;

	// called by OSGi-DS
	public void bindPeerCommunicator(IPeerCommunicator serv) {
		peerCommunicator = serv;
		
		peerCommunicator.addListener(this, PeerCloseMessage.class);
	}

	// called by OSGi-DS
	public void unbindPeerCommunicator(IPeerCommunicator serv) {
		if (peerCommunicator == serv) {
			peerCommunicator.removeListener(this, PeerCloseMessage.class);
			
			peerCommunicator = null;
		}
	}
	
	// called by OSGi-DS
	public final void activate() {
		peerDiscoveryThread = new RepeatingJobThread(PEER_DISCOVERY_INTERVAL_MILLIS, PEER_DISCOVERY_THREAD_NAME) {
			@Override
			public void doJob() {
				if( JxtaServicesProvider.isActivated() ) {
					try {
						synchronized( onDiscovering ) {
							if( !onDiscovering ) {
								onDiscovering = true;
								JxtaServicesProvider.getInstance().getRemotePeerAdvertisements(PeerManager.this);
							}
						}
					
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

	@Override
	public void discoveryEvent(DiscoveryEvent event) {
		synchronized( onDiscovering ) {
			onDiscovering = false;
		}
	}
	
	// called by OSGi-DS
	public final void deactivate() {
		peerDiscoveryThread.stopRunning();

		LOG.debug("Peer manager deactivated");
	}

	private void processPeerAdvertisements(List<PeerAdvertisement> advertisements) {
		if( !P2PDictionary.isActivated() || !JxtaServicesProvider.isActivated() ) {
			return;
		}
		
		final Map<PeerID, String> newIds = Maps.newHashMap();
		final Map<PeerID, String> oldIDs = Maps.newHashMap();
		synchronized(dictionaryObject) {
			 oldIDs.putAll(P2PDictionary.getInstance().getKnownPeersMap());
		}
		
		final PeerID localPeerID = P2PNetworkManager.getInstance().getLocalPeerID();

		for( PeerAdvertisement peerAdvertisement : advertisements ) {
			final PeerID peerID = peerAdvertisement.getPeerID();

			if (!peerID.equals(localPeerID) && JxtaServicesProvider.getInstance().isReachable(peerID) && !gracefullyDisconnectedPeers.contains(peerID)) {
				final String peerName = peerAdvertisement.getName();
				
				newIds.put(peerID, peerName);
				if (oldIDs.containsKey(peerID)) {
					oldIDs.remove(peerID);
				} else {
					synchronized( dictionaryObject ) {
						P2PDictionary.getInstance().addPeer(peerID, peerName);
					}
				}
			}
		}

		for( PeerID lostPeerID : oldIDs.keySet() ) {
			
			synchronized( potencialPeerLossMap ) {
				Integer count = potencialPeerLossMap.get(lostPeerID);
				if( count == null ) {
					potencialPeerLossMap.put(lostPeerID, 1);
				} else {
					count++;
					if( count >= PEER_LOSS_THREASHOLD ) {
						
						synchronized( dictionaryObject ) {
							P2PDictionary.getInstance().removePeer(lostPeerID);
						}
						
						synchronized( gracefullyDisconnectedPeers ) {
							gracefullyDisconnectedPeers.remove(lostPeerID);
						}
						
						potencialPeerLossMap.remove(lostPeerID);
					} else {
						potencialPeerLossMap.put(lostPeerID, count);
					}
				}
			}
		}
	}

	@Override
	public void receivedMessage(IPeerCommunicator communicator, PeerID senderPeer, IMessage message) {
		synchronized(potencialPeerLossMap ) {
			potencialPeerLossMap.remove(senderPeer);
		}
		
		Collection<PeerID> currentlyKnownPeerIDs = null;
		synchronized( dictionaryObject ) {
			currentlyKnownPeerIDs = P2PDictionary.getInstance().getRemotePeerIDs();
		}
		
		if( currentlyKnownPeerIDs.contains(senderPeer)) {
			// we are waiting for the peerAdvertisement to disappear
			synchronized( gracefullyDisconnectedPeers ) {
				gracefullyDisconnectedPeers.add(senderPeer);
			}
		}
		
		synchronized( dictionaryObject ) {
			P2PDictionary.getInstance().removePeer(senderPeer);
		}
	}

}
