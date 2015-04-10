package de.uniol.inf.is.odysseus.p2p_new.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.peer.jxta.IJxtaServicesProvider;

public class JxtaServicesProviderService {
	
	private static final Logger LOG = LoggerFactory.getLogger(JxtaServicesProviderService.class);

	private static IJxtaServicesProvider jxtaServicesProvider;

	// called by OSGi
	public static void bindJxtaServicesProvider(IJxtaServicesProvider serv) {
		jxtaServicesProvider = serv;
		LOG.debug("Bound jxtaServicesProvider {}", serv.getClass().getSimpleName());
	}

	// called by OSGi
	public static void unbindJxtaServicesProvider(IJxtaServicesProvider serv) {
		if (serv == jxtaServicesProvider ) {
			LOG.debug("Unbound jxtaServicesProvider {}", serv.getClass().getSimpleName());
			
			jxtaServicesProvider = null;
		}
	}
	
	public static boolean isBound() {
		return jxtaServicesProvider != null;
	}
	
	public static IJxtaServicesProvider getInstance() {
		return jxtaServicesProvider;
	}
}
