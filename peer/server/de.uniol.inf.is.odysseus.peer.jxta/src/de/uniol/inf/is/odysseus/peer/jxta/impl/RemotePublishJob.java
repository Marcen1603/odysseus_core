package de.uniol.inf.is.odysseus.peer.jxta.impl;

import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;

public class RemotePublishJob implements IJxtaJob {

	private final DiscoveryService service;
	private final Advertisement adv;
	private final long expiration;
	
	public RemotePublishJob( DiscoveryService service, Advertisement adv, long expiration ) {
		this.service = service;
		this.adv = adv;
		this.expiration = expiration;
	}
	
	@Override
	public void execute() throws Exception {
		service.remotePublish(adv, expiration);
	}
}
