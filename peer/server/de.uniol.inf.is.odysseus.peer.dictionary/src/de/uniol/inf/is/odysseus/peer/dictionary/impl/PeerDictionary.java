package de.uniol.inf.is.odysseus.peer.dictionary.impl;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.jxta.document.Advertisement;
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

import de.uniol.inf.is.odysseus.peer.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.peer.dictionary.IPeerDictionaryListener;
import de.uniol.inf.is.odysseus.peer.jxta.IJxtaServicesProvider;
import de.uniol.inf.is.odysseus.peer.network.IAdvertisementDiscovererListener;
import de.uniol.inf.is.odysseus.peer.network.IP2PNetworkManager;

public class PeerDictionary implements IPeerDictionary, IAdvertisementDiscovererListener {

	private static final Logger LOG = LoggerFactory.getLogger(PeerDictionary.class);
	private static final String UNKNOWN_PEER_NAME = "<unknown>";
	private static final Map<PeerAdvertisement, Long> toFlushMap = Maps.newConcurrentMap();

	private static IP2PNetworkManager networkManager;
	private static IJxtaServicesProvider jxtaServicesProvider;
	
	private final List<IPeerDictionaryListener> listeners = Lists.newArrayList();
	private final Map<PeerID, String> remotePeerNameMap = Maps.newHashMap();
	private final Map<PeerID, String> remotePeerAddressMap = Maps.newHashMap();

	private Collection<PeerID> currentPeerIDs = Lists.newArrayList();

	// called by OSGi-DS
	public static void bindP2PNetworkManager(IP2PNetworkManager manager ) {
		networkManager = manager;
	}
	
	// called by OSGi-DS
	public static void unbindP2PNetworkManager( IP2PNetworkManager manager ) {
		if( networkManager == manager ) {
			networkManager = null;
		}
	}

	// called by OSGi-DS
	public static void bindJxtaServicesProvider(IJxtaServicesProvider serv) {
		jxtaServicesProvider = serv;
	}

	// called by OSGi-DS
	public static void unbindJxtaServicesProvider(IJxtaServicesProvider serv) {
		if (jxtaServicesProvider == serv) {
			jxtaServicesProvider = null;
		}
	}
	
	private Collection<PeerID> toValidPeerIDs(Collection<PeerAdvertisement> peerAdvs) {
		// TODO: Die Methode macht mehr als der Name aussagt
		Collection<PeerID> ids = Lists.newLinkedList();
		PeerID localPeerID = networkManager.getLocalPeerID();

		LOG.debug("Determining peers out of {} potencial peers", peerAdvs.size());
		for (PeerAdvertisement adv : peerAdvs) {
			if (!localPeerID.equals(adv.getPeerID())) {
				if (jxtaServicesProvider.isReachable(adv.getPeerID())) {
					ids.add(adv.getPeerID());
					LOG.debug("Peer " + adv.getName() + " is reachable");
					toFlushMap.remove(adv);
				} else {
					LOG.debug("Peer " + adv.getName() + " is NOT reachable anymore");
					remotePeerNameMap.remove(adv.getPeerID());
					remotePeerAddressMap.remove(adv.getPeerID());
					tryFlushAdvertisement(adv);
				}
			}
		}

		return ids;

	}

	private static void tryFlushAdvertisement(Advertisement advertisement) {
		try {
			jxtaServicesProvider.flushAdvertisement(advertisement);
			LOG.debug("Flushed advertisement {}", advertisement.getClass().getName());
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
				while( !networkManager.isStarted() ) {
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
					}
				}
				
				networkManager.addAdvertisementListener(PeerDictionary.this);
			}
		});

		waitingThread.setDaemon(true);
		waitingThread.setName("Waiting for p2p network instance");
		waitingThread.start();

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
		return ImmutableList.copyOf(currentPeerIDs);
	}

	@Override
	public String getRemotePeerName(PeerID peerID) {
		Preconditions.checkNotNull(peerID, "PeerID to get the name from must not be null!");

		if (peerID.equals(networkManager.getLocalPeerID())) {
			return networkManager.getLocalPeerName();
		}
		if (remotePeerNameMap.containsKey(peerID)) {
			return remotePeerNameMap.get(peerID);
		}

		Collection<PeerAdvertisement> peerAdvs = jxtaServicesProvider.getPeerAdvertisements();
		for (PeerAdvertisement peerAdv : peerAdvs) {
			if (peerAdv.getPeerID().equals(peerID)) {
				remotePeerNameMap.put(peerID, peerAdv.getName());
				return peerAdv.getName();
			}
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
		if (peerID.equals(networkManager.getLocalPeerID())) {
			return Optional.of("127.0.0.1");
		}

		String address = remotePeerAddressMap.get(peerID);
		if (Strings.isNullOrEmpty(address)) {
			Optional<String> newAddress = jxtaServicesProvider.getRemotePeerAddress(peerID);
			if (newAddress.isPresent() && !newAddress.get().startsWith("0.0.0.0")) {
				remotePeerAddressMap.put(peerID, newAddress.get());
				return newAddress;
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
		Collection<PeerAdvertisement> peerAdvertisements = jxtaServicesProvider.getPeerAdvertisements();
		Collection<PeerID> newPeers = toValidPeerIDs(peerAdvertisements);
		
		synchronized (currentPeerIDs) {
			Collection<PeerID> oldPeers = Lists.newArrayList(currentPeerIDs);
			currentPeerIDs = newPeers;
			
			Collection<PeerID> addedPeers = Lists.newLinkedList();
			for (PeerID newPeer : newPeers) {
				if (!oldPeers.contains(newPeer)) {
					addedPeers.add(newPeer);
				} else {
					oldPeers.remove(newPeer);
				}
			}

			for (PeerID addedPeer : addedPeers) {
				firePeerAddedEvent(addedPeer);
			}

			for (PeerID oldPeer : oldPeers) {
				firePeerRemovedEvent(oldPeer);
				
				// to avoid multiple calls for same lost peerID
				tryFlushPeerAdvertisement(oldPeer, peerAdvertisements);
			}
		}
	}

	private static void tryFlushPeerAdvertisement(PeerID oldPeer, Collection<PeerAdvertisement> peerAdvertisements) {
		if( oldPeer == null ) {
			return;
		}
		if( peerAdvertisements == null || peerAdvertisements.isEmpty() ) {
			return;
		}
		
		for( PeerAdvertisement peerAdvertisement : peerAdvertisements ) {
			if( peerAdvertisement.getPeerID().equals(oldPeer)) {
				try {
					jxtaServicesProvider.flushAdvertisement(peerAdvertisement);
				} catch (IOException ignore) {
				} 
			}
		}
	}

}