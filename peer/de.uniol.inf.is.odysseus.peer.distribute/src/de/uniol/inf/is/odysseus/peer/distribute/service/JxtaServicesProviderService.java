package de.uniol.inf.is.odysseus.peer.distribute.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.IJxtaServicesProvider;

public class JxtaServicesProviderService {

	private static final Logger LOG = LoggerFactory.getLogger(JxtaServicesProviderService.class);
	
	private static IJxtaServicesProvider provider;
	
	public void bindJxtaServicesProvider(IJxtaServicesProvider prov) {		
		provider = prov;		
		LOG.debug("JxtaServicesProvider bound {}", prov);		
	}
	
	public void unbindJxtaServicesProvider(IJxtaServicesProvider prov) {		
		if(provider == prov) {			
			provider = null;		
			LOG.debug("JxtaServicesProvider unbound {}", prov);			
		}		
	}
	
	public static boolean isBound() {		
		return get() != null;		
	}
	
	public static IJxtaServicesProvider get() {		
		return provider;	
	}
	
}