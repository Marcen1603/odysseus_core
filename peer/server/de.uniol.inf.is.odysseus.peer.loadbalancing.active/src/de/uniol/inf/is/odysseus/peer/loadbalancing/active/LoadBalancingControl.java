package de.uniol.inf.is.odysseus.peer.loadbalancing.active;

import java.util.ArrayList;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingStrategy.LoadBalancingException;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.registries.interfaces.ILoadBalancingAllocatorRegistry;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.registries.interfaces.ILoadBalancingStrategyRegistry;

public class LoadBalancingControl implements ILoadBalancingController {
	

	/***
	 * Logger
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(LoadBalancingControl.class);
	
	private ILoadBalancingStrategy currentStrategy;
	private ILoadBalancingStrategy chosenStrategy;
	private ILoadBalancingAllocator currentAllocator;
	private ILoadBalancingAllocator chosenAllocator;
	
	private ArrayList<ILoadBalancingControllerListener> listeners = Lists.newArrayList();
	
	
	private boolean isRunning = false;
	
	private ILoadBalancingAllocatorRegistry allocators;
	private ILoadBalancingStrategyRegistry strategies;
	
	
	
	public void bindLoadBalancingStrategyRegistry(ILoadBalancingStrategyRegistry serv) {
		this.strategies=serv;
	}
	

	
	public void unbindLoadBalancingStrategyRegistry(ILoadBalancingStrategyRegistry serv) {
		if(this.strategies==serv) {
			this.strategies=null;
		}
	}
	
	public void bindLoadBalancingAllocatorRegistry(ILoadBalancingAllocatorRegistry serv) {
		this.allocators=serv;
	}
	

	public void unbindLoadBalancingAllocatorRegistry(ILoadBalancingAllocatorRegistry serv) {
		if(this.allocators==serv) {
			this.allocators=null;
		}
	}
	
	@Override
	public synchronized boolean setLoadBalancingStrategy(String strategyName) {
		if(!strategies.isStrategyBound(strategyName)) {
			LOG.error("Strategy {} not found.",strategyName);
			return false;
		}
		chosenStrategy = strategies.getStrategy(strategyName);
		LOG.debug("Strategy {} chosen.",strategyName);
		return true;
	}

	@Override
	public synchronized boolean setLoadBalancingAllocator(String allocatorName) {
		if(!allocators.isAllocatorBound(allocatorName)) {
			LOG.error("Allocator {} not found.",allocatorName);
			return false;
		}
		chosenAllocator = allocators.getAllocator(allocatorName);
		LOG.debug("Allocator {} chosen.",allocatorName);
		return true;
	}

	@Override
	public synchronized boolean isLoadBalancingRunning() {
		return isRunning;
	}

	@Override
	public synchronized String getSelectedLoadBalancingStrategy() {
		if(this.currentStrategy==null)
			return "";
		
		return currentStrategy.getName();
	}

	@Override
	public synchronized String getSelectedLoadBalancingAllocator() {
		if(this.currentAllocator==null)
			return "";
		
		return currentAllocator.getName();
	}

	@Override
	public synchronized void stopLoadBalancing() {
		currentStrategy.stopMonitoring();
		this.isRunning = false;
		
		notifyListeners();
	}
	
	@Override
	public synchronized Set<String> getAvailableStrategies() {
		return strategies.getRegisteredStrategies();
	}
	

	@Override
	public synchronized Set<String> getAvailableAllocators() {
		return allocators.getRegisteredAllocators();
	}
	
	

	@Override
	public synchronized void startLoadBalancing() {
		if(isRunning) {
			stopLoadBalancing();
		}
		
		if(chosenStrategy == null) {
			LOG.error("No Strategy chosen.");
			return;
		}
		if(chosenAllocator == null) {
			LOG.error("No allocator chosen.");
			return;
		}
		
		currentAllocator=chosenAllocator;
		currentStrategy=chosenStrategy;
		
		currentStrategy.setAllocator(currentAllocator);
		
		try {
			currentStrategy.startMonitoring();
			isRunning=true;
		} catch (LoadBalancingException e) {
			LOG.error("LoadBalancing Strategy {} failed: {}",currentStrategy.getName(),e.getMessage());
			e.printStackTrace();
			stopLoadBalancing();
		}
		
		notifyListeners();
	}


	private synchronized void notifyListeners() {
		for (ILoadBalancingControllerListener listener : listeners) {
			listener.notifyLoadBalancingStatusChanged(isRunning);
		}
	}
	

	@Override
	public synchronized void addControllerListener(ILoadBalancingControllerListener listener) {
		if(!this.listeners.contains(listener))
			listeners.add(listener);
		
	}



	@Override
	public synchronized void removeControllerListener(
			ILoadBalancingControllerListener listener) {
		if(this.listeners.contains(listener))
			listeners.remove(listener);
		
		
	}

	@Override
	public void forceLoadBalancing() {
		
		if(currentStrategy!=null) {
			try {
				currentStrategy.forceLoadBalancing();
			} catch (LoadBalancingException e) {
				LOG.error("Error while forcing LoadBalancing: {}",e.getMessage());
				e.printStackTrace();
			}
		}
		
	}

}
