package de.uniol.inf.is.odysseus.p2p_new;

import net.jxta.content.ContentService;
import net.jxta.discovery.DiscoveryService;
import net.jxta.endpoint.EndpointService;
import net.jxta.peer.PeerInfoService;
import net.jxta.pipe.PipeService;

public interface IJxtaServicesProvider {

	public ContentService getContentService();
	public DiscoveryService getDiscoveryService();
	public EndpointService getEndpointService();
	public PipeService getPipeService();
	public PeerInfoService getPeerInfoService();
	
}
