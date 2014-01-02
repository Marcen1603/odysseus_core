package de.uniol.inf.is.odysseus.peer.resource.impl;

import java.io.IOException;
import java.util.Map;

import net.jxta.document.AdvertisementFactory;
import net.jxta.peer.PeerID;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.p2p_new.util.RepeatingJobThread;
import de.uniol.inf.is.odysseus.peer.resource.IResourceUsage;
import de.uniol.inf.is.odysseus.peer.resource.service.JxtaServicesProviderService;
import de.uniol.inf.is.odysseus.peer.resource.service.P2PNetworkManagerService;
import de.uniol.inf.is.odysseus.peer.resource.service.ServerExecutorService;

public class ResourceUsageCheckThread extends RepeatingJobThread {

	private static final double DEFAULT_BANDWIDTH_KB = 1024.0;
	private static final Logger LOG = LoggerFactory.getLogger(ResourceUsageCheckThread.class);
	private static final int REFRESH_INTERVAL_MILLIS = 10 *1000;
	private static final Runtime RUNTIME = Runtime.getRuntime();
	
	private final Sigar sigar = new Sigar();
	private long previousInputTotal = 0;
	private long previousOutputTotal = 0;
	
	private final PeerResourceUsageManager manager;
	private final Pinger pinger;

	public ResourceUsageCheckThread(PeerResourceUsageManager manager, Pinger pinger) {
		super(REFRESH_INTERVAL_MILLIS, "Resource usage checker");
		
		Preconditions.checkNotNull(manager, "Peer resource manager must not be null!");
		Preconditions.checkNotNull(pinger, "Pinger must not be null!");
		
		this.manager = manager;
		this.pinger = pinger;
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
				
				String interfaceName = sigar.getNetInterfaceConfig(null).getName();
				long rawSpeed = sigar.getNetInterfaceStat(interfaceName).getSpeed(); // rawSpeed = -1 if os is not supported (yet)
				double bandwidthInKBs = rawSpeed >= 0 ? rawSpeed / 1024.0 : DEFAULT_BANDWIDTH_KB;
				NetInterfaceStat net = sigar.getNetInterfaceStat(interfaceName);
				
				long inputTotal = net.getRxBytes();
				long outputTotal = net.getTxBytes();
				
				double netInputRate = ( inputTotal - previousInputTotal ) / 1024;
				double netOutputRate = ( outputTotal - previousOutputTotal ) / 1024;
				previousInputTotal = inputTotal;
				previousOutputTotal = outputTotal;
				
				int runningQueries = 0;
				int stoppedQueries = 0;
				for( IPhysicalQuery physicalQuery : ServerExecutorService.get().getExecutionPlan().getQueries() ) {
					if( physicalQuery.isOpened() ) {
						runningQueries++;
					} else {
						stoppedQueries++;
					}
				}
				
				Map<PeerID, Long> pingMap = pinger.getPingMap();
				
				IResourceUsage usage = new ResourceUsage(P2PNetworkManagerService.get().getLocalPeerID(), 
						freeMemory, maxMemory, cpuFree, cpuMax, System.currentTimeMillis(), 
						runningQueries, stoppedQueries, bandwidthInKBs, netOutputRate, netInputRate, pingMap);
				
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
