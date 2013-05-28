package de.uniol.inf.is.odysseus.p2p_new;

import net.jxta.content.ContentService;
import net.jxta.discovery.DiscoveryService;
import net.jxta.endpoint.EndpointService;
import net.jxta.pipe.PipeService;

public interface IJxtaServicesProvider {

	ContentService getContentService();
	DiscoveryService getDiscoveryService();
	EndpointService getEndpointService();
	PipeService getPipeService();
	
}
