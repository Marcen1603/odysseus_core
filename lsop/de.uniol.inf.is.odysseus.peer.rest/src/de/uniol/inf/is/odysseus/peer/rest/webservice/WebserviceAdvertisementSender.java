package de.uniol.inf.is.odysseus.peer.rest.webservice;


import java.io.IOException;

import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;
import net.jxta.peergroup.PeerGroupID;
import de.uniol.inf.is.odysseus.peer.jxta.IJxtaServicesProvider;
import de.uniol.inf.is.odysseus.peer.util.RepeatingJobThread;
import de.uniol.inf.is.odysseus.rest.service.RestService;


/**
 * Helper class to publish advertisements with information to the rest service
 * @author Thore Stratmann
 *
 */
public class WebserviceAdvertisementSender {

	private static final long ADVLIFETIME = 180000;
	private static final long ADVEXPIRATIONTIME = 180000;
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
		startRepeatedPublish(provider, adv);
	}
	
	private void startRepeatedPublish(final IJxtaServicesProvider provider, final WebserviceAdvertisement adv) {
		
		RepeatingJobThread refresher = new RepeatingJobThread(ADVEXPIRATIONTIME, "Repeatedly send out WebserviceAdvertisement") {
			@Override
			public void doJob() {
				try {
					waitForJxtaServicesProvider(provider);
					provider.publish(adv,ADVLIFETIME,ADVEXPIRATIONTIME);
					provider.remotePublish(adv,ADVEXPIRATIONTIME);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		refresher.start();
		
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
