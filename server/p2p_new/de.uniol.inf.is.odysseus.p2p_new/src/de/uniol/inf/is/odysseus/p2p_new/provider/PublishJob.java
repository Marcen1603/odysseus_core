package de.uniol.inf.is.odysseus.p2p_new.provider;

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
		service.publish(adv, lifetime, expiration);
		System.err.println("Publish " + adv.getAdvType());
	}
}
