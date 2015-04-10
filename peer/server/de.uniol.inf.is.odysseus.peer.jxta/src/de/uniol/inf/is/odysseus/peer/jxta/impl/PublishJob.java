package de.uniol.inf.is.odysseus.peer.jxta.impl;

import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;

public class PublishJob implements IJxtaJob {

	private final DiscoveryService service;
	private final Advertisement adv;
	private final long lifetime;
	private final long expiration;
	
	public PublishJob( DiscoveryService service, Advertisement adv, long lifetime, long expiration ) {
		this.service = service;
		this.adv = adv;
		this.lifetime = lifetime;
		this.expiration = expiration;
	}
	
	@Override
	public void execute() throws Exception {
		try {
			service.publish(adv, lifetime, expiration);
		} catch( Throwable t ) {
			throw new Exception("Could not publish advertisement " + adv.getClass().getSimpleName(), t);
		}
	}
}
