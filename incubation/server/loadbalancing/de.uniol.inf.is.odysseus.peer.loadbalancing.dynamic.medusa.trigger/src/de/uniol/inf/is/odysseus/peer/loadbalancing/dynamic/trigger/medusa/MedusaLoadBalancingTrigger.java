package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.trigger.medusa;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.ILoadBalancingTrigger;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.listeners.ILoadBalancingTriggerListener;

public class MedusaLoadBalancingTrigger implements ILoadBalancingTrigger, IMedusaTriggerListener {

	private List<ILoadBalancingTriggerListener> listeners = Lists.newArrayList();
	
	private Object threadManipulationLock = new Object();
	CheckMarginCostThread monitoringThread = null;
	private boolean isActive = false;
	
	
	
	@Override
	public String getName() {
		return "Medusa";
	}

	@Override
	public void addListener(ILoadBalancingTriggerListener listener) {
		if(!listeners.contains(listener)) {
			listeners.add(listener);
		}
		
	}

	@Override
	public void removeListener(ILoadBalancingTriggerListener listener) {
		if(listeners.contains(listener)) {
			listeners.remove(listener);
		}
	}

	@Override
	public void start() {
		synchronized(threadManipulationLock) {
			if(!isActive) {
				isActive = true;
				if(monitoringThread==null) {
					monitoringThread = new CheckMarginCostThread(this);
					monitoringThread.start();
				}
				
			}
		}
		
	}

	@Override
	public void setInactive() {
		synchronized(threadManipulationLock) {
			if(isActive) {
				isActive = false;
				if(monitoringThread!=null) {
					monitoringThread.setInactive();
					monitoringThread = null;
				}
			}
		}
	}

	@Override
	public void loadBalancingTriggered() {
		List<ILoadBalancingTriggerListener> listenersCopy = new ArrayList<ILoadBalancingTriggerListener>(listeners);
		for(ILoadBalancingTriggerListener listener : listenersCopy) {
			//We don't need Cpu Mem and Net Values here... But we will set some if someone uses it with different strategy.
			listener.triggerLoadBalancing(0.1, 0.1, 0.1);
		}
	}

}
