package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.trigger.monitoring;


import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.peer.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.odyload.CostEstimationHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.odyload.OdyLoadConstants;
import de.uniol.inf.is.odysseus.peer.resource.IPeerResourceUsageManager;
import de.uniol.inf.is.odysseus.peer.resource.IResourceUsage;


public class MonitoringThread extends Thread {
	

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(MonitoringThread.class);
	
	private ArrayList<IMonitoringThreadListener> listenerList = new ArrayList<IMonitoringThreadListener>();
	
	private boolean isActive = true;
	
	
	public void setInactive() {
		isActive = false;
	}
	
	public MonitoringThread(IMonitoringThreadListener listener) {
		this.setName("LoadBalancing Monitoring Thread.");
		this.listenerList.add(listener);
	}
	
	public void addListener(IMonitoringThreadListener listener) {
		if(!listenerList.contains(listener))
			listenerList.add(listener);
	}
	
	public void removeListener(IMonitoringThreadListener listener) {
		if(listenerList.contains(listener))
			listenerList.remove(listener);
	}
	
	@Override
	public void run() {
		
		IPeerResourceUsageManager usageManager = OsgiServiceProvider.getUsageManager();
		IPeerDictionary peerDictionary = OsgiServiceProvider.getPeerDictionary();
		
		while(isActive) {
				
			
				LOG.debug("Looking up resource usage");
				

				
				//Wait for update interval.
				//Do This before we measure as this forces the peer to wait between failed LB attempts.
				try {
					Thread.sleep(OdyLoadConstants.UPDATE_INTERVAL);
				}
				catch (InterruptedException e) {
					LOG.error(e.getMessage());
					isActive = false;
				}

				if(peerDictionary.getRemotePeerIDs().size()>0) {
					IResourceUsage usage = usageManager.getLocalResourceUsage();
					LOG.debug("CPU: {} free of {}",usage.getCpuFree(),usage.getCpuMax());
					LOG.debug("MEM: {} free of {}",usage.getMemFreeBytes(),usage.getMemMaxBytes());
					LOG.debug("NET: {} (In)  of {} (Max)",usage.getNetInputRate(),usage.getNetBandwidthMax());
					LOG.debug("NET: {} (Out)  of {} (Max)",usage.getNetOutputRate(),usage.getNetBandwidthMax());
					
					double cpuUsage = 1-(usage.getCpuFree()/usage.getCpuMax());
					//Multiplication with 1.0 to avoid long/long Division resulting in implicit typecast which returns 0.0
					double memUsage = 1-(1.0*usage.getMemFreeBytes()/usage.getMemMaxBytes());
					
					double netUsage = (usage.getNetOutputRate()+usage.getNetInputRate())/usage.getNetBandwidthMax();
					
					if(OdyLoadConstants.COUNT_JXTA_OPERATORS_FOR_NETWORK_COSTS) {
						netUsage = CostEstimationHelper.estimateNetUsedFromJxtaOperatorCount();
					}
					
					
					LOG.debug("CPU usage is {}",cpuUsage);
					LOG.debug("MEM usage is {}",memUsage);
					LOG.debug("NET usage is {}",netUsage);
					
					if(cpuUsage>=OdyLoadConstants.CPU_THRESHOLD || memUsage>=OdyLoadConstants.MEM_THRESHOLD || netUsage>=OdyLoadConstants.NET_THRESHOLD) {
						this.isActive = false;
						ArrayList<IMonitoringThreadListener> copyOfListenerList =  new ArrayList<IMonitoringThreadListener>(listenerList);
						for(IMonitoringThreadListener listener : copyOfListenerList) {
							listener.notifyLoadBalancingTriggered(cpuUsage,memUsage,netUsage);
						}
					}
				}
			
			
		}
		
		
	}

}
