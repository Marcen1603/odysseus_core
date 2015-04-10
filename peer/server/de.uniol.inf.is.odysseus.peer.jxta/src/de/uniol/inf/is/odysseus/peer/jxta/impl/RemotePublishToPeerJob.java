package de.uniol.inf.is.odysseus.peer.jxta.impl;

import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.peer.PeerID;

public class RemotePublishToPeerJob implements IJxtaJob {

	private final DiscoveryService service;
	private final PeerID peerID;
	private final Advertisement adv;
	private final long expiration;
	
	public RemotePublishToPeerJob( DiscoveryService service, Advertisement adv, PeerID peerID, long expiration ) {
		this.service = service;
		this.adv = adv;
		this.expiration = expiration;
		this.peerID = peerID;
	}
	
	@Override
	public void execute() throws Exception {
		service.remotePublish(peerID.toString(), adv, expiration);
	}
}
