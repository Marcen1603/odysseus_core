package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.IJxtaServicesProvider;

/**
 * Class for binding the JxtaServicesProvider-Service to be used within the package,
 * same as {@link de.uniol.inf.is.odysseus.p2p_new.distribute.user.service.JxtaServicesProviderService}
 */
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
