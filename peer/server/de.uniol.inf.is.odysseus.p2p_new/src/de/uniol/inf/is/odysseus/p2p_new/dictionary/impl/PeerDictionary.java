package de.uniol.inf.is.odysseus.p2p_new.dictionary.impl;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.jxta.document.Advertisement;
import net.jxta.document.AdvertisementFactory;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;
import net.jxta.protocol.PeerAdvertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementDiscovererListener;
import de.uniol.inf.is.odysseus.p2p_new.IPeerReachabilityListener;
import de.uniol.inf.is.odysseus.p2p_new.PeerReachabilityInfo;
import de.uniol.inf.is.odysseus.p2p_new.broadcast.PeerReachabilityService;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IPeerDictionaryListener;
import de.uniol.inf.is.odysseus.p2p_new.network.P2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.provider.JxtaServicesProvider;

public class PeerDictionary implements IPeerDictionary, IAdvertisementDiscovererListener, IPeerReachabilityListener {

	private static final Logger LOG = LoggerFactory.getLogger(PeerDictionary.class);
	private static final String UNKNOWN_PEER_NAME = "<unknown>";
//	private static final Map<PeerAdvertisement, Long> toFlushMap = Maps.newConcurrentMap();

	private static PeerDictionary instance;

	private final List<IPeerDictionaryListener> listeners = Lists.newArrayList();
	private final Map<PeerID, String> remotePeerNameMap = Maps.newHashMap();
	private final Map<PeerID, String> remotePeerAddressMap = Maps.newHashMap();

	private Map<PeerID, PeerAdvertisement> currentPeerIDs = Maps.newHashMap();
	private Map<PeerID, PeerReachabilityInfo> peerInfoMap = Maps.newHashMap();

	public static PeerDictionary getInstance() {
		return instance;
	}

	public static boolean isActivated() {
		return instance != null;
	}

//	private Collection<PeerID> toValidPeerIDs(Collection<PeerAdvertisement> peerAdvs) {
//		// TODO: Die Methode macht mehr als der Name aussagt
//		Collection<PeerID> ids = Lists.newLinkedList();
//		PeerID localPeerID = P2PNetworkManager.getInstance().getLocalPeerID();
//
//		LOG.debug("Determining peers");
//		for (PeerAdvertisement adv : peerAdvs) {
//			if (!localPeerID.equals(adv.getPeerID())) {
//				if (JxtaServicesProvider.getInstance().isReachable(adv.getPeerID()) || PeerReachabilityService.getInstance().isPeerReachable(adv.getPeerID())) {
//					ids.add(adv.getPeerID());
//					LOG.debug("Peer " + adv.getName() + " is reachable");
//					toFlushMap.remove(adv);
//				} else {
//					LOG.debug("Peer " + adv.getName() + " is NOT reachable anymore");
//					remotePeerNameMap.remove(adv.getPeerID());
//					remotePeerAddressMap.remove(adv.getPeerID());
//					tryFlushAdvertisement(adv);
//				}
//			}
//		}
//
//		return ids;
//
//	}

	private static void tryFlushAdvertisement(Advertisement advertisement) {
		try {
			if (JxtaServicesProvider.isActivated()) {
				JxtaServicesProvider.getInstance().flushAdvertisement(advertisement);
				LOG.debug("Flushed advertisement {}", advertisement.getClass().getName());
			}
		} catch (IOException e) {
			LOG.error("Could not flush advertisement {}", advertisement, e);
		}
	}

	public void bindListener(IPeerDictionaryListener serv) {
		addListener(serv);
	}

	public void unbindListener(IPeerDictionaryListener serv) {
		removeListener(serv);
	}

	private void firePeerAddedEvent(PeerID peer) {
		LOG.debug("Peer added : " + peer);

		for (IPeerDictionaryListener listener : this.listeners) {
			try {
				listener.peerAdded(peer);
			} catch (Throwable t) {
				LOG.error("Listener {} got an error", listener, t);
			}
		}
	}

	private void firePeerRemovedEvent(PeerID peer) {
		LOG.debug("Peer removed : " + peer);

		for (IPeerDictionaryListener listener : this.listeners) {
			try {
				listener.peerRemoved(peer);
			} catch (Throwable t) {
				LOG.error("Listener {} got an error", listener, t);
			}
		}
	}

	public void activate() {
		Thread waitingThread = new Thread(new Runnable() {
			@Override
			public void run() {
				P2PNetworkManager.waitFor();
				P2PNetworkManager.getInstance().addAdvertisementListener(PeerDictionary.this);
				
				PeerReachabilityService.waitFor();
				PeerReachabilityService.getInstance().addListener(PeerDictionary.this);

				instance = PeerDictionary.this;
			}
		});

		waitingThread.setDaemon(true);
		waitingThread.setName("Waiting for p2p network instance");
		waitingThread.start();

	}

	public void deactivate() {
//		P2PNetworkManager.getInstance().removeAdvertisementListener(PeerDictionary.this);
//		PeerReachabilityService.getInstance().removeListener(PeerDictionary.this);
		instance = null;
	}

	@Override
	public void addListener(IPeerDictionaryListener listener) {
		Preconditions.checkNotNull(listener, "Listener to add must not be null!");
		synchronized (listeners) {
			listeners.add(listener);
		}
	}

	@Override
	public void removeListener(IPeerDictionaryListener listener) {
		Preconditions.checkNotNull(listener, "Listener to remove must not be null!");
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}

	@Override
	public ImmutableCollection<PeerID> getRemotePeerIDs() {
		return ImmutableList.copyOf(currentPeerIDs.keySet());
	}

	@Override
	public String getRemotePeerName(PeerID peerID) {
		Preconditions.checkNotNull(peerID, "PeerID to get the name from must not be null!");

		if (peerID.equals(P2PNetworkManager.getInstance().getLocalPeerID())) {
			return P2PNetworkManager.getInstance().getLocalPeerName();
		}
		if (remotePeerNameMap.containsKey(peerID)) {
			return remotePeerNameMap.get(peerID);
		}

		PeerReachabilityInfo peerInfo = peerInfoMap.get(peerID);
		if( peerInfo != null ) {
			remotePeerNameMap.put(peerID, peerInfo.getPeerName());
			return peerInfo.getPeerName();
		}

		return UNKNOWN_PEER_NAME;
	}

	@Override
	public String getRemotePeerName(String peerIDString) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(peerIDString), "PeerIDString must not be null or empty!");
		return getRemotePeerName(toPeerID(peerIDString));
	}

	private static PeerID toPeerID(String text) {
		try {
			final URI id = new URI(text);
			return (PeerID) IDFactory.fromURI(id);
		} catch (URISyntaxException | ClassCastException ex) {
			LOG.error("Could not set id", ex);
			return null;
		}
	}

	@Override
	public Optional<String> getRemotePeerAddress(PeerID peerID) {
		Preconditions.checkNotNull(peerID, "PeerID to get the address from must not be null!");
		if (peerID.equals(P2PNetworkManager.getInstance().getLocalPeerID())) {
			return Optional.of("127.0.0.1");
		}

		String address = remotePeerAddressMap.get(peerID);
		if (Strings.isNullOrEmpty(address)) {
			PeerReachabilityInfo info = peerInfoMap.get(peerID);
			if( info != null ) {
				String addressStr = info.getAddress().getHostAddress();
				remotePeerAddressMap.put(peerID, addressStr);
			
				return Optional.of( addressStr );
			} 
			
			return Optional.absent();
		}

		return Optional.of(address);
	}

	@Override
	public void advertisementDiscovered(Advertisement adv) {
		// Nothing to do.
	}

	@Override
	public void updateAdvertisements() {
//		Collection<PeerID> newPeers = toValidPeerIDs(JxtaServicesProvider.getInstance().getPeerAdvertisements());
//		
//		synchronized (currentPeerIDs) {
//			Collection<PeerID> oldPeers = Lists.newArrayList(currentPeerIDs);
//			currentPeerIDs = newPeers;
//			
//			Collection<PeerID> addedPeers = Lists.newLinkedList();
//			for (PeerID newPeer : newPeers) {
//				if (!oldPeers.contains(newPeer)) {
//					addedPeers.add(newPeer);
//				} else {
//					oldPeers.remove(newPeer);
//				}
//			}
//
//			for (PeerID addedPeer : addedPeers) {
//				firePeerAddedEvent(addedPeer);
//			}
//
//			for (PeerID oldPeer : oldPeers) {
//				firePeerRemovedEvent(oldPeer);
//			}
//		}
	}
	
	@Override
	public void peerReachable(PeerReachabilityInfo info) {
		PeerAdvertisement adv = (PeerAdvertisement)AdvertisementFactory.newAdvertisement(PeerAdvertisement.getAdvertisementType());
		adv.setName(info.getPeerName());
		adv.setPeerID(info.getPeerID());
		adv.setPeerGroupID(info.getPeerGroupID());
		
		try {
			JxtaServicesProvider.getInstance().publish(adv);
			currentPeerIDs.put(info.getPeerID(), adv);
			peerInfoMap.put(info.getPeerID(), info);
			
			firePeerAddedEvent(info.getPeerID());
		} catch (IOException e) {
			LOG.error("Could not publish peer advertisement of peer {}", info.getPeerName(), e);
		}
	}
	
	@Override
	public void peerNotReachable(PeerReachabilityInfo info) {
		
		PeerAdvertisement adv = currentPeerIDs.get(info.getPeerID());
		tryFlushAdvertisement(adv);
		
		currentPeerIDs.remove(info.getPeerID());
		peerInfoMap.remove(info.getPeerID());
		remotePeerNameMap.remove(info.getPeerID());
		remotePeerAddressMap.remove(info.getPeerID());
		
		firePeerRemovedEvent(info.getPeerID());
		return;
	}
	
	@Override
	public Collection<PeerAdvertisement> getPeerAdvertisements() {
		return Lists.newArrayList(currentPeerIDs.values());
	}
}