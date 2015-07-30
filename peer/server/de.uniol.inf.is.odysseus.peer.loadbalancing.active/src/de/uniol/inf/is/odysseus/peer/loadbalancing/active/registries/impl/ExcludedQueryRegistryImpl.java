package de.uniol.inf.is.odysseus.peer.loadbalancing.active.registries.impl;

import java.util.ArrayList;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.peer.loadbalancing.active.IExcludedQueryRegistryListener;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.registries.interfaces.IExcludedQueriesRegistry;

public class ExcludedQueryRegistryImpl implements IExcludedQueriesRegistry{

	private ArrayList<Integer> excludedQueryIDs = Lists.newArrayList();

	private ArrayList<IExcludedQueryRegistryListener> listeners = Lists.newArrayList();
	

	public synchronized void excludeQueryIdFromLoadBalancing(int queryID) {
		if(!excludedQueryIDs.contains(queryID)) {
			excludedQueryIDs.add(queryID);
		}
		notifyExcludedQueriesChanged();
	}
	
	public synchronized void removeExcludedQueryID(int queryID) {
		if(excludedQueryIDs.contains(queryID)) {
			excludedQueryIDs.remove(new Integer(queryID));
		}
		notifyExcludedQueriesChanged();
	}
	

	
	private void notifyExcludedQueriesChanged() {
		for(IExcludedQueryRegistryListener listener : listeners) {
			listener.notifyExcludedQueriesChanged(excludedQueryIDs);
		}
	}

	@Override
	public boolean isQueryIDExcludedFromLoadBalancing(int queryID) {
		return excludedQueryIDs.contains(queryID);
	}

	@Override
	public void addListener(IExcludedQueryRegistryListener listener) {
		if(!this.listeners.contains(listener)) {
			listeners.add(listener);
		}
		
	}

	@Override
	public void removeListener(IExcludedQueryRegistryListener listener) {
		if(this.listeners.contains(listener)) {
			listeners.remove(listener);
		}
	}
}
