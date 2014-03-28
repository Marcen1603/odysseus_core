package de.uniol.inf.is.odysseus.p2p_new.adv;

import java.io.IOException;
import java.util.Enumeration;

import net.jxta.document.Advertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.provider.JxtaServicesProvider;
import de.uniol.inf.is.odysseus.p2p_new.util.RepeatingJobThread;

abstract class AdvertisementDiscoverer extends RepeatingJobThread {

	private static final Logger LOG = LoggerFactory.getLogger(AdvertisementDiscoverer.class);
	
	private static final long DISCOVERY_INTERVAL_MILLIS = 4 * 1000;

	public AdvertisementDiscoverer() {
		super(DISCOVERY_INTERVAL_MILLIS);
	}
	
	@Override
	public void doJob() {
		if (JxtaServicesProvider.isActivated() ) {
			
			JxtaServicesProvider.getInstance().getRemoteAdvertisements();
			
			try {
				Enumeration<Advertisement> localAdvertisements = JxtaServicesProvider.getInstance().getLocalAdvertisements();
				process(localAdvertisements);
				
			} catch (IOException e) {
				LOG.error("Could not get local advertisements", e);
			}
		}
	}
	
	public abstract void process(Enumeration<Advertisement> advertisements);
}
