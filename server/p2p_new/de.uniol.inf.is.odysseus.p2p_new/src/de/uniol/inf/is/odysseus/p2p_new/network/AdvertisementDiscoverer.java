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

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementDiscovererListener;
import de.uniol.inf.is.odysseus.p2p_new.provider.JxtaServicesProvider;
import de.uniol.inf.is.odysseus.p2p_new.util.RepeatingJobThread;

public class AdvertisementDiscoverer extends RepeatingJobThread implements DiscoveryListener {

	private static final Logger LOG = LoggerFactory.getLogger(AdvertisementDiscoverer.class);

	private static final long DISCOVERY_INTERVAL_MILLIS_WITHOUT_PEERS = 6 * 1000;
	private static final long DISCOVERY_INTERVAL_MILLIS = 30 * 1000;

	private final Collection<IAdvertisementDiscovererListener> listenerMap = Lists.newLinkedList();

	private boolean foundPeer = false;
	private Object discovering = new Object();

	public AdvertisementDiscoverer() {
		super(DISCOVERY_INTERVAL_MILLIS_WITHOUT_PEERS, "Advertisement discoverer");
	}

	@Override
	public void beforeJob() {
		JxtaServicesProvider.waitFor();
	}

	@Override
	public void doJob() {
		synchronized( discovering ) {
			LOG.debug("Discovering advertisements... (interval is {} ms)", getIntervalMillis());
			
			JxtaServicesProvider jxta = JxtaServicesProvider.getInstance();
			jxta.getRemoteAdvertisements(this);
			jxta.getRemotePeerAdvertisements(this);
			
			fireUpdateEvent();
		}
	}

	@Override
	public void discoveryEvent(DiscoveryEvent event) {
		synchronized( discovering ) {
			Collection<Advertisement> advertisements = toCollection(event.getResponse().getAdvertisements());
			for (Advertisement advertisement : advertisements) {
				if (!foundPeer) {
					checkIfPeerFound(advertisement);
				}
	
				fireAdvertisementListeners(advertisement);
			}
		}
	}

	private void checkIfPeerFound(Advertisement advertisement) {
		if (advertisement instanceof PeerAdvertisement) {
			PeerAdvertisement peerAdv = (PeerAdvertisement) advertisement;
			if (!peerAdv.getPeerID().equals(P2PNetworkManager.getInstance().getLocalPeerID())) {
				// found our first peer --> slow discovery down now
				LOG.debug("Found our first remote peer. Slow discovery down to {} ms", DISCOVERY_INTERVAL_MILLIS);
				setIntervalMillis(DISCOVERY_INTERVAL_MILLIS);
				foundPeer = true;
			}
		}
	}

	private void fireAdvertisementListeners(Advertisement advertisement) {
		synchronized (listenerMap) {
			for (IAdvertisementDiscovererListener listener : listenerMap) {
				try {
					listener.advertisementDiscovered(advertisement);
				} catch (Throwable t) {
					LOG.error("Exception in advertisement discoverer listener", t);
				}
			}
		}
	}

	private void fireUpdateEvent() {
		synchronized (listenerMap) {
			for (IAdvertisementDiscovererListener listener : listenerMap) {
				try {
					listener.updateAdvertisements();
				} catch (Throwable t) {
					LOG.error("Exception in advertisement discoverer listener", t);
				}
			}
		}
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
