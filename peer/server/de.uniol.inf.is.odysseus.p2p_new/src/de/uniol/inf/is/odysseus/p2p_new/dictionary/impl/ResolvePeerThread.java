package de.uniol.inf.is.odysseus.p2p_new.dictionary.impl;

import java.io.IOException;

import net.jxta.protocol.PeerAdvertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.p2p_new.provider.JxtaServicesProvider;

public final class ResolvePeerThread extends Thread {

	private static final Logger LOG = LoggerFactory.getLogger(ResolvePeerThread.class);
	
	private final PeerAdvertisement peerAdvertisement;
	
	public ResolvePeerThread(PeerAdvertisement peerAdvertisement) {
		Preconditions.checkNotNull(peerAdvertisement, "Peer advertisement must not be null!");
		
		this.peerAdvertisement = peerAdvertisement;
		
		setName("Trying to connect to peer " + peerAdvertisement.getName());
		setDaemon(true);
	}
	
	public PeerAdvertisement getPeerAdvertisement() {
		return peerAdvertisement;
	}
	
	@Override
	public void run() {
		LOG.debug("Trying to connect to peer {}", peerAdvertisement.getName());
		if( !JxtaServicesProvider.getInstance().isReachable(peerAdvertisement.getPeerID(), true) ) {
			try {
				LOG.debug("Could not connect to peer {}", peerAdvertisement.getName());
				JxtaServicesProvider.getInstance().flushAdvertisement(peerAdvertisement);
				
			} catch (IOException e) {
				LOG.error("Could not flush peer advertisement", e);
			}
		} else {
			LOG.debug("Could connect to peer {}", peerAdvertisement.getName());
		}
	}
}
