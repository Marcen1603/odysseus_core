package de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.sender;

import java.io.IOException;

import net.jxta.document.Advertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.IJxtaServicesProvider;
import de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.DistributedDataContainerAdvertisement;
import de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.DistributedDataContainerChangeAdvertisement;
import de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.DistributedDataContainerRequestAdvertisement;

/**
 * DDCAdvertisementSender publishes created DDCAdvertisements to other peers
 * 
 * @author ChrisToenjesDeye, Michael Brand
 * 
 */
public class DistributedDataContainerAdvertisementSender {

	private static final Logger LOG = LoggerFactory
			.getLogger(DistributedDataContainerAdvertisementSender.class);

	private static final long WAIT_TIME_MILLIS = 60 * 1000;
	private static final long WAIT_TIME_MILLIS_REQUEST = 30 * 1000;	// JXTA checks for advertisement every 30 seconds

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

	private void publishDDCAdvertisement_internal(Advertisement adv, long waitTime) {
		if (adv != null) {
			try {
				waitForJxtaServicesProvider();
				jxtaServicesProvider.publish(adv, waitTime,
						waitTime);
				jxtaServicesProvider.remotePublish(adv, waitTime);
			} catch (IOException e) {
				LOG.error("Could not publish DDC advertisement", e);
			}
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
		publishDDCAdvertisement_internal(adv, WAIT_TIME_MILLIS);
		LOG.debug("Published DDC initial advertisment.");
	}

	/**
	 * Publishes the given DDCChangeAdvertisement to all other peers in network
	 * 
	 * @param adv
	 *            - the DDCChangeAdvertisement to publish
	 */
	public void publishDDCChangeAdvertisement(
			final DistributedDataContainerChangeAdvertisement adv) {
		publishDDCAdvertisement_internal(adv, WAIT_TIME_MILLIS);
		LOG.debug("Published DDC change advertisment.");
	}

	/**
	 * Publishes the given DDCRequestAdvertisement to request other DDCs
	 * 
	 * @param adv
	 *            - the DDCRequestAdvertisement to publish
	 */
	public void publishDDCRequestAdvertisement(
			final DistributedDataContainerRequestAdvertisement adv) {
		publishDDCAdvertisement_internal(adv, WAIT_TIME_MILLIS_REQUEST);
		LOG.debug("Published DDC request advertisment.");
	}

	private static void waitForJxtaServicesProvider() {
		while (!jxtaServicesProvider.isActive()) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}
		}
	}
}
