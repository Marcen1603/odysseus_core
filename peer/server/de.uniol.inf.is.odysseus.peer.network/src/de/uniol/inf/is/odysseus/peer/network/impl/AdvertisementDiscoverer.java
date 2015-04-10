package de.uniol.inf.is.odysseus.peer.network.impl;

import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

import net.jxta.discovery.DiscoveryEvent;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.protocol.PeerAdvertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.peer.config.PeerConfiguration;
import de.uniol.inf.is.odysseus.peer.network.IAdvertisementDiscovererListener;
import de.uniol.inf.is.odysseus.peer.util.RepeatingJobThread;

public class AdvertisementDiscoverer extends RepeatingJobThread implements DiscoveryListener {

	private static final Logger LOG = LoggerFactory.getLogger(AdvertisementDiscoverer.class);

	private static final int DEFAULT_DISCOVERY_INTERVAL_MILLIS = 10 * 1000;
	private static final int DEFAULT_DISCOVERY_WITH_PEER_INTERVAL_MILLIS = 30000;

	private static final String DISCOVERY_INTERVAL_SYSPROPERTY = "peer.discovery.startinterval";
	private static final String DISCOVERY_INTERVAL_WITH_PEERS_SYSPROPERTY = "peer.discovery.interval";

	private static final Collection<IAdvertisementDiscovererListener> LISTENER_MAP = Lists.newLinkedList();
	private final DiscoveryService discoveryService;

	private boolean foundPeer = false;

	public AdvertisementDiscoverer(DiscoveryService discoveryService) {
		super(determineDiscoveryInterval(), "Advertisement discoverer");
		
		Preconditions.checkNotNull(discoveryService, "discoveryService must not be null!");
		
		this.discoveryService = discoveryService;
	}

	private static long determineDiscoveryInterval() {
		Optional<String> discoverIntervalString = PeerConfiguration.get(DISCOVERY_INTERVAL_SYSPROPERTY);
		try {
			if (discoverIntervalString.isPresent()) {
				return Integer.valueOf(discoverIntervalString.get());
			}
		} catch (Throwable t) {
		}
		return DEFAULT_DISCOVERY_INTERVAL_MILLIS;
	}

	@Override
	public void doJob() {
		LOG.debug("Discovering advertisements... (interval is {} ms)", getIntervalMillis());

		discoveryService.getRemoteAdvertisements(null, DiscoveryService.ADV, null, null, 99, this);
		discoveryService.getRemoteAdvertisements(null, DiscoveryService.PEER, null, null, 0, this);
		
		AdvertisementDiscovererListenerUpdater updater = null;
		synchronized( LISTENER_MAP ) {
			updater = new AdvertisementDiscovererListenerUpdater(LISTENER_MAP);
		}
		
		updater.start(); // calls listener async
	}

	@Override
	public void discoveryEvent(DiscoveryEvent event) {
		Collection<Advertisement> advertisements = toCollection(event.getResponse().getAdvertisements());
		LOG.debug("Found {} advertisements", advertisements.size());

		for (Advertisement advertisement : advertisements) {
			if (!foundPeer) {
				checkIfPeerFound(advertisement);
			} else {
				break;
			}
		}
		
		AdvertisementDiscovererListenerHandler handler = null;
		synchronized( LISTENER_MAP ) {
			handler = new AdvertisementDiscovererListenerHandler(LISTENER_MAP, advertisements);
		}
		
		handler.start(); // calls listener async
	}

	private void checkIfPeerFound(Advertisement advertisement) {
		if (advertisement instanceof PeerAdvertisement) {
			PeerAdvertisement peerAdv = (PeerAdvertisement) advertisement;
			if (!peerAdv.getPeerID().equals(P2PNetworkManager.getInstance().getLocalPeerID())) {
				// found our first peer --> slow discovery down now
				setIntervalMillis(determineSlowDiscoveryInterval());
				LOG.debug("Found our first remote peer. Slow discovery down to {} ms", getIntervalMillis());
				foundPeer = true;
			}
		}
	}

	private static long determineSlowDiscoveryInterval() {
		Optional<String> discoverIntervalString = PeerConfiguration.get(DISCOVERY_INTERVAL_WITH_PEERS_SYSPROPERTY);
		try {
			if (discoverIntervalString.isPresent()) {
				return Integer.valueOf(discoverIntervalString.get());
			}
		} catch (Throwable t) {
		}
		return DEFAULT_DISCOVERY_WITH_PEER_INTERVAL_MILLIS;
	}

	private static Collection<Advertisement> toCollection(Enumeration<Advertisement> advs) {
		List<Advertisement> list = Lists.newLinkedList();
		while (advs.hasMoreElements()) {
			list.add(advs.nextElement());
		}
		return list;
	}

	public static void addListener(IAdvertisementDiscovererListener listener) {
		synchronized (LISTENER_MAP) {
			LISTENER_MAP.add(listener);
		}
	}

	public static void removeListener(IAdvertisementDiscovererListener listener) {
		synchronized (LISTENER_MAP) {
			LISTENER_MAP.remove(listener);
		}
	}
}
