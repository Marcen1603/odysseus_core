package de.uniol.inf.is.odysseus.p2p_new.network;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementDiscovererListener;

public class AdvertisementDiscovererListenerUpdater extends Thread {

	private static final Logger LOG = LoggerFactory.getLogger(AdvertisementDiscovererListenerUpdater.class);

	private final Collection<IAdvertisementDiscovererListener> listenersToCall = Lists.newArrayList();

	public AdvertisementDiscovererListenerUpdater(Collection<IAdvertisementDiscovererListener> listenersToCall) {
		this.listenersToCall.addAll(listenersToCall);

		setName("Advertisement discoverer listener updater");
		setDaemon(true);
	}

	@Override
	public void run() {
		fireAdvertisementListeners();
	}

	private void fireAdvertisementListeners() {
		for (IAdvertisementDiscovererListener listener : listenersToCall) {
			try {
				listener.updateAdvertisements();
			} catch (Throwable t) {
				LOG.error("Exception in advertisement discoverer listener", t);
			}
		}
	}
}
