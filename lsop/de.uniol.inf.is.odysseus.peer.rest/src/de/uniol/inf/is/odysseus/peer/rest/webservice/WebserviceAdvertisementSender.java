package de.uniol.inf.is.odysseus.peer.rest.webservice;


import java.io.IOException;

import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;
import net.jxta.peergroup.PeerGroupID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;







import de.uniol.inf.is.odysseus.p2p_new.IJxtaServicesProvider;
import de.uniol.inf.is.odysseus.rest.service.RestService;


/**
 * Helper class to publish advertisements with information to the rest service
 * @author Thore Stratmann
 *
 */
public class WebserviceAdvertisementSender {

	private static final Logger LOG = LoggerFactory
			.getLogger(WebserviceAdvertisementSender.class);
//	private static final long WAIT_TIME_MILLIS = 21 * 1000;

	/**
	 * Publishes an advertisement with information to the rest service
	 * @param provider
	 * @param peerId
	 */
	public void publishWebserviceAdvertisement(IJxtaServicesProvider provider, PeerID peerId, PeerGroupID peerGroupID) {
		WebserviceAdvertisement adv = new WebserviceAdvertisement();
		adv.setRestPort(RestService.getPort());
		adv.setPeerID(peerId);
		adv.setID(IDFactory.newPipeID(peerGroupID));
		if (adv != null) {
			try {
				waitForJxtaServicesProvider(provider);
				provider.publishInfinite(adv);
				provider.remotePublishInfinite(adv);
			} catch (IOException e) {
				LOG.error("Could not publish webservice advertisement", e);
			}
		}
	}
	
	private static void waitForJxtaServicesProvider(IJxtaServicesProvider provider) {
		while( !provider.isActive() ) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}
		}
	}
}