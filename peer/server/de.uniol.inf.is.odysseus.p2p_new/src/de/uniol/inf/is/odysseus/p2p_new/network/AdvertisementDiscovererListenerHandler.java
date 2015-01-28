package de.uniol.inf.is.odysseus.p2p_new.network;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import net.jxta.document.Advertisement;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementDiscovererListener;

public class AdvertisementDiscovererListenerHandler extends Thread {

	private static final Logger LOG = LoggerFactory.getLogger(AdvertisementDiscovererListenerHandler.class);
	
	private final Collection<IAdvertisementDiscovererListener> listenersToCall = Lists.newArrayList();
	private final Collection<Advertisement> foundAdvs = Lists.newArrayList();
	
	public AdvertisementDiscovererListenerHandler(Collection<IAdvertisementDiscovererListener> listenersToCall, Collection<Advertisement> foundAdvertisements ) {
		this.listenersToCall.addAll(listenersToCall);
		this.foundAdvs.addAll(foundAdvertisements);
		
		setName("Advertisement discoverer listener handler");
		setDaemon(true);
	}
	
	@Override
	public void run() {
		fireAdvertisementListeners();
	}
	
	private void fireAdvertisementListeners() {
		for( Advertisement advertisement : foundAdvs ) {
			for (IAdvertisementDiscovererListener listener : listenersToCall) {
				try {
					listener.advertisementDiscovered(advertisement);
				} catch (Throwable t) {
					LOG.error("Exception in advertisement discoverer listener", t);
				}
			}
		}
	}
}
