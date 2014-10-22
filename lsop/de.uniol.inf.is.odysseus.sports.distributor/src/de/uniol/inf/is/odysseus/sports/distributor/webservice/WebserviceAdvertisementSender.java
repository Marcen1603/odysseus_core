package de.uniol.inf.is.odysseus.sports.distributor.webservice;


import java.io.IOException;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;





import de.uniol.inf.is.odysseus.p2p_new.IJxtaServicesProvider;
import de.uniol.inf.is.odysseus.rest.service.RestService;


public class WebserviceAdvertisementSender {

	private static final Logger LOG = LoggerFactory
			.getLogger(WebserviceAdvertisementSender.class);
	private static final long WAIT_TIME_MILLIS = 21 * 1000;




	public void publishWebserviceAdvertisement(IJxtaServicesProvider provider, PeerID peerId) {
		WebserviceAdvertisement adv = new WebserviceAdvertisement();
		adv.setRestPort(RestService.getPort());
		adv.setPeerID(peerId);
		if (adv != null) {
			try {
				waitForJxtaServicesProvider(provider);
				provider.publish(adv, WAIT_TIME_MILLIS, WAIT_TIME_MILLIS);
				provider.remotePublish(adv, WAIT_TIME_MILLIS);
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
