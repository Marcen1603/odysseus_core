package de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.sender;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.IJxtaServicesProvider;
import de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.DDCAdvertisement;

public class DDCAdvertisementSender {

	private static final Logger LOG = LoggerFactory.getLogger(DDCAdvertisementSender.class);
	private static IJxtaServicesProvider jxtaServicesProvider;
	private static final long WAIT_TIME_MILLIS = 21 * 1000;
	private static DDCAdvertisementSender instance;

	public final void activate() {
		instance = this;
	}

	public final void deactivate() {

		instance = null;
	}

	public static DDCAdvertisementSender getInstance() {
		return instance;
	}
	
	// called by OSGi-DS
	public static void bindJxtaServicesProvider(IJxtaServicesProvider serv) {
		jxtaServicesProvider = serv;
	}

	// called by OSGi-DS
	public static void unbindJxtaServicesProvider(IJxtaServicesProvider serv) {
		if (jxtaServicesProvider == serv) {
			jxtaServicesProvider = null;
		}
	}

	public void publishDDCAdvertisement(final DDCAdvertisement adv) {
		try {
			waitForJxtaServicesProvider();
			jxtaServicesProvider.publish(adv, WAIT_TIME_MILLIS,
					WAIT_TIME_MILLIS);
			jxtaServicesProvider.remotePublish(adv, WAIT_TIME_MILLIS);
		} catch (IOException e) {
			LOG.error("Could not publish ddc advertisement", e);
		}
	}

	private static void waitForJxtaServicesProvider() {
		while( !jxtaServicesProvider.isActive() ) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}
		}
	}
}
