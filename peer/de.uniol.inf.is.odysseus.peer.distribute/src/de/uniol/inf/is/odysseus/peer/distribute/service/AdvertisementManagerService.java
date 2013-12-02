package de.uniol.inf.is.odysseus.peer.distribute.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementManager;

public class AdvertisementManagerService {

	private static final Logger LOG = LoggerFactory.getLogger(AdvertisementManagerService.class);
	
	private static IAdvertisementManager manager;
	
	public void bindAdvertisementManager(IAdvertisementManager mng) {
		manager = mng;		
		LOG.debug("AdvertisementManager bound {}", mng);
	}
	
	public void unbindAdvertisementManager(IAdvertisementManager mng) {
		if(manager == mng) {
			manager = null;			
			LOG.debug("AdvertisementManager unbound {}", mng);
		}
	}
	
	public static IAdvertisementManager get() {
		return manager;
	}
	
	public static boolean isBound() {
		return get() != null;
	}
}