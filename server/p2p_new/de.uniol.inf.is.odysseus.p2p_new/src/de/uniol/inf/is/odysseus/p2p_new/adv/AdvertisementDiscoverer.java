package de.uniol.inf.is.odysseus.p2p_new.adv;

import java.io.IOException;
import java.util.Enumeration;

import net.jxta.discovery.DiscoveryEvent;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.document.Advertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.provider.JxtaServicesProvider;
import de.uniol.inf.is.odysseus.p2p_new.util.RepeatingJobThread;

abstract class AdvertisementDiscoverer extends RepeatingJobThread implements DiscoveryListener {

	private static final Logger LOG = LoggerFactory.getLogger(AdvertisementDiscoverer.class);
	
	private static final long DISCOVERY_INTERVAL_MILLIS = 4 * 1000;
	private static final int MAX_DISCOVERY_WAIT_MILLIS = 10 * 1000;
	
	private Long discoverTimestamp = 0L;

	public AdvertisementDiscoverer() {
		super(DISCOVERY_INTERVAL_MILLIS);
	}
	
	@Override
	public void doJob() {
		if (JxtaServicesProvider.isActivated() ) {
			
			synchronized( discoverTimestamp ) {
				if( System.currentTimeMillis() - discoverTimestamp > MAX_DISCOVERY_WAIT_MILLIS ) {
					LOG.debug("Discovering advertisements started");
					discoverTimestamp = System.currentTimeMillis();
					JxtaServicesProvider.getInstance().getRemoteAdvertisements(this);
				}
			}
			
			try {
				Enumeration<Advertisement> localAdvertisements = JxtaServicesProvider.getInstance().getLocalAdvertisements();
				process(localAdvertisements);
				
			} catch (IOException e) {
				LOG.error("Could not get local advertisements", e);
			}
		}
	}
	
	public abstract void process(Enumeration<Advertisement> advertisements);
	
	@Override
	public void discoveryEvent(DiscoveryEvent event) {
		synchronized( discoverTimestamp ) {
			discoverTimestamp = 0L;
			LOG.debug("Discovering advertisements finished!");
		}
	}
}
