package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementManager;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.IServiceStatusListener;

public class AdvertisementManagerService {
	private static List<IServiceStatusListener> listeners = new ArrayList<IServiceStatusListener>();
	private static final Logger LOG = LoggerFactory.getLogger(AdvertisementManagerService.class);
	
	private static IAdvertisementManager manager;
	
	public void bindAdvertisementManager(IAdvertisementManager mng) {
		manager = mng;
		LOG.debug("AdvertisementManager bound");
		for(IServiceStatusListener l : listeners) {
			l.serviceBound(manager);
		}
	}
	
	public void unbindAdvertisementManager(IAdvertisementManager mng) {
		if(manager == mng) {
			manager = null;			
			LOG.debug("AdvertisementManager unbound");
		}
	}

	public static IAdvertisementManager getAdvertisementManager() {
		return manager;
	}

	public static boolean isBound() {
		return getAdvertisementManager() != null;	
	}
	
	public static void addListener(IServiceStatusListener l) {
		listeners.add(l);
	}
	
}