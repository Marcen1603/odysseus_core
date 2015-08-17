package de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.odyload.strategy.trigger;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.OdyLoadConstants;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.interfaces.IMonitoringThreadListener;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.odyload.strategy.heuristic.CostEstimationHelper;
import de.uniol.inf.is.odysseus.peer.resource.IPeerResourceUsageManager;
import de.uniol.inf.is.odysseus.peer.resource.IResourceUsage;


public class MonitoringThread extends Thread {
	

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(MonitoringThread.class);
	

	
	private ArrayList<IMonitoringThreadListener> listenerList = new ArrayList<IMonitoringThreadListener>();
	
	private boolean isActive = true;
	private IPeerResourceUsageManager usageManager;
	
	public void setInactive() {
		isActive = false;
	}
	
	public MonitoringThread(IPeerResourceUsageManager usageManager, IMonitoringThreadListener listener) {
		this.listenerList.add(listener);
		this.usageManager = usageManager;
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
		while(isActive) {
				LOG.info("Looking up resource usage");
			
			IResourceUsage usage = usageManager.getLocalResourceUsage();
			LOG.debug("CPU: {} free of {}",usage.getCpuFree(),usage.getCpuMax());
			LOG.info("MEM: {} free of {}",usage.getMemFreeBytes(),usage.getMemMaxBytes());
			LOG.debug("NET: {} (In)  of {} (Max)",usage.getNetInputRate(),usage.getNetBandwidthMax());
			LOG.debug("NET: {} (Out)  of {} (Max)",usage.getNetOutputRate(),usage.getNetBandwidthMax());
			
			double cpuUsage = 1-(usage.getCpuFree()/usage.getCpuMax());
			//Multiplication with 1.0 to avoid long/long Division resulting in implicit typecast which returns 0.0
			double memUsage = 1-(1.0*usage.getMemFreeBytes()/usage.getMemMaxBytes());
			
			double netUsage = (usage.getNetOutputRate()+usage.getNetInputRate())/usage.getNetBandwidthMax();
			
			if(OdyLoadConstants.COUNT_JXTA_OPERATORS_FOR_NETWORK_COSTS) {
				netUsage = CostEstimationHelper.estimateNetUsedFromJxtaOperatorCount();
			}
			
			
			LOG.info("CPU usage is {}",cpuUsage);
			LOG.info("MEM usage is {}",memUsage);
			LOG.info("NET usage is {}",netUsage);
			
			if(cpuUsage>=OdyLoadConstants.CPU_THRESHOLD || memUsage>=OdyLoadConstants.MEM_THRESHOLD || netUsage>=OdyLoadConstants.NET_THRESHOLD) {
				this.isActive = false;
				ArrayList<IMonitoringThreadListener> copyOfListenerList =  new ArrayList<IMonitoringThreadListener>(listenerList);
				for(IMonitoringThreadListener listener : copyOfListenerList) {
					listener.triggerLoadBalancing(cpuUsage,memUsage,netUsage);
				}
			}
			
				
			//Wait for update interval.
			try {
				Thread.sleep(OdyLoadConstants.UPDATE_INTERVAL);
			}
			catch (InterruptedException e) {
				LOG.error(e.getMessage());
				isActive = false;
			}
		}
		
		
	}

}
