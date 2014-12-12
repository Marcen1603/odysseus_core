package de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.sender;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.IJxtaServicesProvider;
import de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.DistributedDataContainerAdvertisement;

/**
 * DDCAdvertisementSender publishes created DDCAdvertisements to other peers
 * 
 * @author ChrisToenjesDeye
 * 
 */
public class DistributedDataContainerAdvertisementSender {

	private static final Logger LOG = LoggerFactory
			.getLogger(DistributedDataContainerAdvertisementSender.class);
	private static final long WAIT_TIME_MILLIS = 21 * 1000;

	private static IJxtaServicesProvider jxtaServicesProvider;
	private static DistributedDataContainerAdvertisementSender instance;

	public final void activate() {
		instance = this;
	}

	public final void deactivate() {

		instance = null;
	}

	public static DistributedDataContainerAdvertisementSender getInstance() {
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

	/**
	 * Publishes the given DDCAdvertisement to all other peers in network
	 * 
	 * @param adv
	 *            - the DDCAdvertisement to publish
	 */
	public void publishDDCAdvertisement(
			final DistributedDataContainerAdvertisement adv) {
		if (adv != null) {
			try {
				waitForJxtaServicesProvider();
				jxtaServicesProvider.publish(adv, WAIT_TIME_MILLIS, WAIT_TIME_MILLIS);
				jxtaServicesProvider.remotePublish(adv, WAIT_TIME_MILLIS);
				LOG.debug("Published DDC advertisment.");
			} catch (IOException e) {
				LOG.error("Could not publish DDC advertisement", e);
			}
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
