package de.uniol.inf.is.odysseus.peer.recovery.advertisement;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jxta.document.Advertisement;



import net.jxta.document.AdvertisementFactory;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementDiscovererListener;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryCommunicator;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryStrategyManager;
import de.uniol.inf.is.odysseus.peer.recovery.registry.RecoveryCommunicatorRegistry;
import de.uniol.inf.is.odysseus.peer.recovery.registry.RecoveryStrategyManagerRegistry;

public class RecoveryStrategyManagerAdvertisementListener implements IAdvertisementDiscovererListener {

	private IP2PNetworkManager p2pNetworkManager;
	
	private static final Logger LOG = LoggerFactory.getLogger(RecoveryStrategyManagerAdvertisementListener.class);


	@Override
	public void advertisementDiscovered(Advertisement advertisement) {
		if( advertisement instanceof RecoveryStrategyManagerAdvertisement ) {
			RecoveryStrategyManagerAdvertisement adv = (RecoveryStrategyManagerAdvertisement)advertisement;			
			IRecoveryStrategyManager strategyManager = RecoveryStrategyManagerRegistry.getInstance().get(adv.getStrategyManagerName());
			if (strategyManager != null) {
				LOG.info("Discovered advertisement with strategy manager "+adv.getStrategyManagerName());
				for (IRecoveryCommunicator communicator : RecoveryCommunicatorRegistry.getRecoveryCommunicators()) {
					communicator.setRecoveryStrategyManager(strategyManager);
				}
			} else {
				LOG.info("Discovered advertisement with strategy manager "+adv.getStrategyManagerName() +" is not available");
			}			
		}
	}

	@Override
	public void updateAdvertisements() {
	}	
	
	// called by OSGi-DS
	public void bindP2PNetworkManager(IP2PNetworkManager serv) {
		p2pNetworkManager = serv;
		AdvertisementFactory.registerAdvertisementInstance(RecoveryStrategyManagerAdvertisement.getAdvertisementType(),new RecoveryStrategyManagerAdvertisementInstantiator());
		p2pNetworkManager.addAdvertisementListener(this);
	}
	
	public void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		if (p2pNetworkManager == serv) {
			p2pNetworkManager = null;
		}
	}
}