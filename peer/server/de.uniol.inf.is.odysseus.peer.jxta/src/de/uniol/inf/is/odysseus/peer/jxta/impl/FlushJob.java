package de.uniol.inf.is.odysseus.peer.jxta.impl;

import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;

public class FlushJob implements IJxtaJob {

	private final DiscoveryService service;
	private final Advertisement adv;
	
	public FlushJob( DiscoveryService service, Advertisement adv ) {
		this.service = service;
		this.adv = adv;
	}
	
	@Override
	public void execute() throws Exception {
		service.flushAdvertisement(adv);
	}
}
