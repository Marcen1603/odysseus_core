package de.uniol.inf.is.odysseus.peer.ddc.distribute;

import de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.DistributedDataContainerDistributeThread;
import de.uniol.inf.is.odysseus.peer.ddc.file.IDDCFileHandlerListener;

public class DDCFileHandlerListener implements IDDCFileHandlerListener {

	@Override
	public void ddcFileLoaded() {
		
		// distribute data
		// start new thread to distribute DDC, because otherwise startup of
		// Odysseus
		// is blocked by waiting for p2p network
		DistributedDataContainerDistributeThread ddcDistributeThread = new DistributedDataContainerDistributeThread();
		ddcDistributeThread.setName("DDCDistributeThread");
		ddcDistributeThread.setDaemon(true); // set is as deamon
		ddcDistributeThread.start();
		
	}

}