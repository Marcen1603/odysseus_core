package de.uniol.inf.is.odysseus.p2p_new.network;

import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

import net.jxta.discovery.DiscoveryEvent;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.document.Advertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementDiscovererListener;
import de.uniol.inf.is.odysseus.p2p_new.provider.JxtaServicesProvider;
import de.uniol.inf.is.odysseus.p2p_new.util.RepeatingJobThread;

public class AdvertisementDiscoverer extends RepeatingJobThread implements DiscoveryListener {

	private static final Logger LOG = LoggerFactory.getLogger(AdvertisementDiscoverer.class);

	private static final long DISCOVERY_INTERVAL_MILLIS = 20 * 1000;
	private static final int MAX_DISCOVERY_WAIT_MILLIS = 30 * 1000;

	private Long discoverTimestamp = 0L;

	private final Collection<IAdvertisementDiscovererListener> listenerMap = Lists.newLinkedList();

	public AdvertisementDiscoverer() {
		super(DISCOVERY_INTERVAL_MILLIS, "Advertisement discoverer");
	}

	@Override
	public void doJob() {
		if (JxtaServicesProvider.isActivated()) {
			synchronized (discoverTimestamp) {
				if (System.currentTimeMillis() - discoverTimestamp > MAX_DISCOVERY_WAIT_MILLIS) {
					LOG.debug("Discovering advertisements started");
					JxtaServicesProvider.getInstance().getRemoteAdvertisements(this);
					JxtaServicesProvider.getInstance().getRemotePeerAdvertisements(this);

					discoverTimestamp = System.currentTimeMillis();
				}
			}
		}
	}

	@Override
	public void discoveryEvent(DiscoveryEvent event) {
		synchronized (discoverTimestamp) {
			discoverTimestamp = 0L;
			LOG.debug("Discovering advertisements finished!");
		}

		Collection<Advertisement> advertisements = toCollection(event.getResponse().getAdvertisements());
		for (Advertisement advertisement : advertisements) {
			fireAdvertisementListeners(advertisement);
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
