package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.trigger.monitoring;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.ILoadBalancingTrigger;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.listeners.ILoadBalancingTriggerListener;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.odyload.OdyLoadConstants;

public class MonitoringTriggerImpl implements ILoadBalancingTrigger, IMonitoringThreadListener {

	private static final String TRIGGER_NAME = "Monitoring";
	

	private static final Logger LOG = LoggerFactory
			.getLogger(MonitoringTriggerImpl.class);
	

	private Object threadManipulationLock = new Object();
	private MonitoringThread monitoringThread = null;
	
	private boolean isRunning = false;

	private ArrayList<ILoadBalancingTriggerListener> listenerList = new ArrayList<ILoadBalancingTriggerListener>();
	

	public void addListener(ILoadBalancingTriggerListener listener) {
		if(!listenerList.contains(listener))
			listenerList.add(listener);
	}
	
	public void removeListener(ILoadBalancingTriggerListener listener) {
		if(listenerList.contains(listener))
			listenerList.remove(listener);
	}
	
	@Override
	public String getName() {
		return TRIGGER_NAME;
				
	}

	@Override
	public void start() {
			isRunning = true;
			startNewMonitoringThread();
	}

	@Override
	public void setInactive() {
		isRunning = false;
		synchronized (threadManipulationLock) {
			if (monitoringThread != null) {
				LOG.info("Stopping monitoring Peer.");
				monitoringThread.removeListener(this);
				monitoringThread.setInactive();
				monitoringThread = null;
			} else {
				LOG.info("No monitoring Thread running.");
			}
		}
		
	}

	@Override
	public void notifyLoadBalancingTriggered(double cpuUsage, double memUsage,
			double netUsage) {
		
		double cpuLoadToRemove = Math.max(0.0, cpuUsage
				- OdyLoadConstants.CPU_THRESHOLD);
		double memLoadToRemove = Math.max(0.0, memUsage
				- OdyLoadConstants.MEM_THRESHOLD);
		double netLoadToRemove = Math.max(0.0, netUsage
				- OdyLoadConstants.NET_THRESHOLD);
		
		ArrayList<ILoadBalancingTriggerListener> listenersCopy = new ArrayList<ILoadBalancingTriggerListener>(listenerList);
		for (ILoadBalancingTriggerListener listener : listenersCopy) {
			listener.triggerLoadBalancing(cpuLoadToRemove, memLoadToRemove, netLoadToRemove);
		}
		
	}
	

	private void startNewMonitoringThread() {
		//Only start new Thread if strategy is still running! 
		if(!isRunning) {
			LOG.info("Not restarting Monitoring Thread, as Trigger is stopped.");
			return;
		}
		
		synchronized (threadManipulationLock) {
			if (monitoringThread == null) {
				LOG.info("Starting to monitor Peer.");
				monitoringThread = new MonitoringThread(this);
				monitoringThread.start();
			} else {
				LOG.info("Monitoring Thread already running.");
			}
		}
	}

}
