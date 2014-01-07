package de.uniol.inf.is.odysseus.peer.resource.impl;

import java.io.IOException;

import net.jxta.document.AdvertisementFactory;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.p2p_new.IJxtaServicesProvider;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.util.RepeatingJobThread;
import de.uniol.inf.is.odysseus.peer.resource.IPeerResourceUsageManager;
import de.uniol.inf.is.odysseus.peer.resource.IResourceUsage;

public class ResourceUsageCheckThread extends RepeatingJobThread {

	private static final double DEFAULT_BANDWIDTH_KB = 1024.0;
	private static final Logger LOG = LoggerFactory.getLogger(ResourceUsageCheckThread.class);
	private static final int REFRESH_INTERVAL_MILLIS = 10 *1000;
	private static final Runtime RUNTIME = Runtime.getRuntime();
	
	private static ResourceUsageCheckThread instance;
	private static PeerResourceUsageManager resourceUsageManager;
	private static IJxtaServicesProvider jxtaServicesProvider;
	private static IP2PNetworkManager p2pNetworkManager;
	private static IServerExecutor serverExecutor;
	
	private final Sigar sigar = new Sigar();
	private long previousInputTotal = 0;
	private long previousOutputTotal = 0;
	
	public ResourceUsageCheckThread() {
		super(REFRESH_INTERVAL_MILLIS, "Resource usage checker");
	}
	
	// called by OSGi-DS
	public static void bindPeerResourceUsageManager(IPeerResourceUsageManager serv) {
		resourceUsageManager = (PeerResourceUsageManager)serv;
	}

	// called by OSGi-DS
	public static void unbindPeerResourceUsageManager(IPeerResourceUsageManager serv) {
		if (resourceUsageManager == serv) {
			resourceUsageManager = null;
		}
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

	// called by OSGi-DS
	public static void bindP2PNetworkManager(IP2PNetworkManager serv) {
		p2pNetworkManager = serv;
	}

	// called by OSGi-DS
	public static void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		if (p2pNetworkManager == serv) {
			p2pNetworkManager = null;
		}
	}
	
	// called by OSGi-DS
	public static void bindExecutor(IExecutor serv) {
		serverExecutor = (IServerExecutor)serv;
	}

	// called by OSGi-DS
	public static void unbindExecutor(IExecutor serv) {
		if (serverExecutor == serv) {
			serverExecutor = null;
		}
	}

	// called by OSGi-DS
	public void activate() {
		instance = this;
		
		start();
	}

	// called by OSGi-DS
	public void deactivate() {
		instance = null;
		
		stopRunning();
	}

	public static ResourceUsageCheckThread getInstance() {
		return instance;
	}
	
	@Override
	public void doJob() {
		try {
			if( p2pNetworkManager.isStarted() && serverExecutor != null ) {
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
				
				if( serverExecutor == null ) {
					return;
				}
				for( IPhysicalQuery physicalQuery : serverExecutor.getExecutionPlan().getQueries() ) {
					if( physicalQuery.isOpened() ) {
						runningQueries++;
					} else {
						stoppedQueries++;
					}
				}
				
				IResourceUsage usage = new ResourceUsage(p2pNetworkManager.getLocalPeerID(), 
						freeMemory, maxMemory, cpuFree, cpuMax, System.currentTimeMillis(), 
						runningQueries, stoppedQueries, bandwidthInKBs, netOutputRate, netInputRate);
				
				if( !resourceUsageManager.getLocalResourceUsage().isPresent() || !ResourceUsage.areSimilar(resourceUsageManager.getLocalResourceUsage().get(), usage)) {
					LOG.debug("Set new current local resource usage: {}", usage);
					resourceUsageManager.setLocalResourceUsage(usage);
					
					ResourceUsageAdvertisement adv = (ResourceUsageAdvertisement)AdvertisementFactory.newAdvertisement(ResourceUsageAdvertisement.getAdvertisementType());
					adv.setResourceUsage(usage);
					
					try {
						jxtaServicesProvider.getDiscoveryService().remotePublish(adv, REFRESH_INTERVAL_MILLIS);
						jxtaServicesProvider.getDiscoveryService().publish(adv, REFRESH_INTERVAL_MILLIS, REFRESH_INTERVAL_MILLIS);
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
