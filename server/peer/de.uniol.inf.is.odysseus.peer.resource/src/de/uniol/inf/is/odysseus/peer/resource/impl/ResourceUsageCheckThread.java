package de.uniol.inf.is.odysseus.peer.resource.impl;

import java.io.IOException;

import net.jxta.document.AdvertisementFactory;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.p2p_new.util.RepeatingJobThread;
import de.uniol.inf.is.odysseus.peer.resource.IResourceUsage;
import de.uniol.inf.is.odysseus.peer.resource.service.JxtaServicesProviderService;
import de.uniol.inf.is.odysseus.peer.resource.service.P2PNetworkManagerService;

public class ResourceUsageCheckThread extends RepeatingJobThread {

	private static final Logger LOG = LoggerFactory.getLogger(ResourceUsageCheckThread.class);
	private static final int REFRESH_INTERVAL_MILLIS = 10 *1000;
	private static final Runtime RUNTIME = Runtime.getRuntime();
	
	private final Sigar sigar = new Sigar();
	
	private final PeerResourceUsageManager manager;

	public ResourceUsageCheckThread(PeerResourceUsageManager manager) {
		super(REFRESH_INTERVAL_MILLIS, "Resource usage checker");
		
		Preconditions.checkNotNull("Peer resource manager must not be null!");
		this.manager = manager;
	}
	
	@Override
	public void doJob() {
		try {
			if( P2PNetworkManagerService.isBound() && P2PNetworkManagerService.get().isStarted()) {
				int cpuMax = sigar.getCpuPercList().length;
				
				CpuPerc perc = sigar.getCpuPerc();
				double cpuFree = cpuMax - (perc.getUser()) * cpuMax;
				
				long maxMemory = RUNTIME.totalMemory();
				long freeMemory = RUNTIME.freeMemory();
				
				IResourceUsage usage = new ResourceUsage(P2PNetworkManagerService.get().getLocalPeerID(), freeMemory, maxMemory, cpuFree, cpuMax, System.currentTimeMillis());
				
				if( !manager.getLocalResourceUsage().isPresent() || !ResourceUsage.areSimilar(manager.getLocalResourceUsage().get(), usage)) {
					LOG.debug("Set new current local resource usage: {}", usage);
					manager.setLocalResourceUsage(usage);
					
					ResourceUsageAdvertisement adv = (ResourceUsageAdvertisement)AdvertisementFactory.newAdvertisement(ResourceUsageAdvertisement.getAdvertisementType());
					adv.setResourceUsage(usage);
					
					try {
						JxtaServicesProviderService.get().getDiscoveryService().remotePublish(adv, REFRESH_INTERVAL_MILLIS);
						JxtaServicesProviderService.get().getDiscoveryService().publish(adv, REFRESH_INTERVAL_MILLIS, REFRESH_INTERVAL_MILLIS);
					} catch (IOException e) {
						LOG.error("Could not publish resource usage advertisement", e);
					}
				}
			}
		} catch (SigarException e) {
			LOG.error("Cannot measure local resource usage", e);
		}
	}
}
