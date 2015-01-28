package de.uniol.inf.is.odysseus.p2p_new.network;

import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

import net.jxta.discovery.DiscoveryEvent;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.document.Advertisement;
import net.jxta.protocol.PeerAdvertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementDiscovererListener;
import de.uniol.inf.is.odysseus.p2p_new.provider.JxtaServicesProvider;
import de.uniol.inf.is.odysseus.p2p_new.util.RepeatingJobThread;
import de.uniol.inf.is.odysseus.peer.config.PeerConfiguration;

public class AdvertisementDiscoverer extends RepeatingJobThread implements DiscoveryListener {

	private static final Logger LOG = LoggerFactory.getLogger(AdvertisementDiscoverer.class);

	private static final int DEFAULT_DISCOVERY_INTERVAL_MILLIS = 10 * 1000;
	private static final int DEFAULT_DISCOVERY_WITH_PEER_INTERVAL_MILLIS = 30000;

	private static final String DISCOVERY_INTERVAL_SYSPROPERTY = "peer.discovery.startinterval";
	private static final String DISCOVERY_INTERVAL_WITH_PEERS_SYSPROPERTY = "peer.discovery.interval";

	private final Collection<IAdvertisementDiscovererListener> listenerMap = Lists.newLinkedList();

	private boolean foundPeer = false;

	public AdvertisementDiscoverer() {
		super(determineDiscoveryInterval(), "Advertisement discoverer");
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
	public void beforeJob() {
		JxtaServicesProvider.waitFor();
	}

	@Override
	public void doJob() {
		LOG.debug("Discovering advertisements... (interval is {} ms)", getIntervalMillis());

		JxtaServicesProvider jxta = JxtaServicesProvider.getInstance();
		jxta.getRemoteAdvertisements(this);
		jxta.getRemotePeerAdvertisements(this);
		
		AdvertisementDiscovererListenerUpdater updater = null;
		synchronized( listenerMap ) {
			updater = new AdvertisementDiscovererListenerUpdater(listenerMap);
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
		synchronized( listenerMap ) {
			handler = new AdvertisementDiscovererListenerHandler(listenerMap, advertisements);
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

	public <T extends Advertisement> void addListener(IAdvertisementDiscovererListener listener) {
		synchronized (listenerMap) {
			listenerMap.add(listener);
		}
	}

	public <T extends Advertisement> void removeListener(IAdvertisementDiscovererListener listener) {
		synchronized (listenerMap) {
			listenerMap.remove(listener);
		}
	}
}
