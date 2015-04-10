package de.uniol.inf.is.odysseus.peer.recovery.advertisement;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jxta.document.Advertisement;



import net.jxta.document.AdvertisementFactory;
import de.uniol.inf.is.odysseus.peer.network.IAdvertisementDiscovererListener;
import de.uniol.inf.is.odysseus.peer.network.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryAllocator;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryStrategy;
import de.uniol.inf.is.odysseus.peer.recovery.registry.RecoveryAllocatorRegistry;
import de.uniol.inf.is.odysseus.peer.recovery.registry.RecoveryStrategyRegistry;

public class RecoveryAllocatorAdvertisementListener implements IAdvertisementDiscovererListener {

	private IP2PNetworkManager p2pNetworkManager;
	
	private static final Logger LOG = LoggerFactory.getLogger(RecoveryAllocatorAdvertisementListener.class);

	
	@Override
	public void advertisementDiscovered(Advertisement advertisement) {
		if( advertisement instanceof RecoveryAllocatorAdvertisement ) {
			RecoveryAllocatorAdvertisement adv = (RecoveryAllocatorAdvertisement)advertisement;			
			IRecoveryAllocator allocator = RecoveryAllocatorRegistry.getInstance().get(adv.getAllocatorName());
			if (allocator != null) {
				LOG.info("Discovered advertisement with recovery allocator "+allocator.getName());
				for (IRecoveryStrategy strategy : RecoveryStrategyRegistry.getInstance().getInterfaceContributions()) {
					strategy.setAllocator(allocator);
				}
			} else {
				LOG.info("Discovered advertisement with recovery allocator "+adv.getAllocatorName() +" is not available");
			}			
		}
	}

	@Override
	public void updateAdvertisements() {
	}	
	
	// called by OSGi-DS
	public void bindP2PNetworkManager(IP2PNetworkManager serv) {
		p2pNetworkManager = serv;
		AdvertisementFactory.registerAdvertisementInstance(RecoveryAllocatorAdvertisement.getAdvertisementType(),new RecoveryAllocatorAdvertisementInstantiator());
		p2pNetworkManager.addAdvertisementListener(this);
	}
	
	public void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		if (p2pNetworkManager == serv) {
			p2pNetworkManager = null;
		}
	}
}