package de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement;

import de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.sender.DistributedDataContainerAdvertisementGenerator;
import de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.sender.DistributedDataContainerAdvertisementSender;

/**
 * Thread for initial distribution on peer-startup. The extra thread is needed,
 * because otherwise the activation of this bundle blocks the whole Odysseus
 * startup.
 * 
 * @author ChrisToenjesDeye
 * 
 */
public class DistributedDataContainerDistributeThread extends Thread {

	@Override
	public void run() {
		// Generate DDCAdvertisement from DDCEntries
		DistributedDataContainerAdvertisement advertisement = DistributedDataContainerAdvertisementGenerator
				.getInstance().generate();
		// Publish Advertisement to other peers in network
		DistributedDataContainerAdvertisementSender.getInstance()
				.publishDDCAdvertisement(advertisement);
	}
}
