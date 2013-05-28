package de.uniol.inf.is.odysseus.p2p_new.lb.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.IJxtaServicesProvider;

public class JxtaServicesProviderService {

	private static final Logger LOG = LoggerFactory.getLogger(JxtaServicesProviderService.class);
	private static IJxtaServicesProvider provider;
	
	// called by OSGi
	public void bindJxtaServicesProvider( IJxtaServicesProvider prov ) {
		provider = prov;
		
		LOG.debug("JxtaServicesProvider bound {}", prov);
	}
	
	// called by OSGi
	public void unbindJxtaServicesProvider( IJxtaServicesProvider prov ) {
		if( provider == prov ) {
			provider = null;
			
			LOG.debug("JxtaServicesProvider unbound {}", prov);
		}
	}
	
	public static boolean isBound() {
		return provider != null;
	}
	
	public static IJxtaServicesProvider get() {
		return provider;
	}
}
