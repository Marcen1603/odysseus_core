package de.uniol.inf.is.odysseus.p2p_new.adv;

import java.io.IOException;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import de.uniol.inf.is.odysseus.p2p_new.provider.JxtaServicesProvider;
import de.uniol.inf.is.odysseus.p2p_new.util.RepeatingJobThread;

abstract class AdvertisementDiscoverer extends RepeatingJobThread {

	private static final Logger LOG = LoggerFactory.getLogger(AdvertisementDiscoverer.class);
	
	private static final int REMOTE_DISCOVERY_COUNT = 2;
	private static final long DISCOVERY_INTERVAL_MILLIS = 2 * 1000;

	private int remoteCounter = 0;

	public AdvertisementDiscoverer() {
		super(DISCOVERY_INTERVAL_MILLIS);
	}
	
	@Override
	public void doJob() {
		if (JxtaServicesProvider.isActivated() && JxtaServicesProvider.getInstance().getDiscoveryService() != null) {
			
			if( remoteCounter == 0 ) {
				JxtaServicesProvider.getInstance().getDiscoveryService().getRemoteAdvertisements(null, DiscoveryService.ADV, null, null, 99);
			}
			remoteCounter = (remoteCounter + 1 ) % REMOTE_DISCOVERY_COUNT;
			
			try {
				Enumeration<Advertisement> localAdvertisements = JxtaServicesProvider.getInstance().getDiscoveryService().getLocalAdvertisements(DiscoveryService.ADV, null, null);
				process(localAdvertisements);
				
			} catch (IOException e) {
				LOG.error("Could not get local advertisements", e);
			}
		}
	}
	
	public abstract void process(Enumeration<Advertisement> advertisements);
}
