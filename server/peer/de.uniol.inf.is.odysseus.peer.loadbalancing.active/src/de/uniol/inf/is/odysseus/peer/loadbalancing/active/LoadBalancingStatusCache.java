package de.uniol.inf.is.odysseus.peer.loadbalancing.active;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;

/**
 * Store for Stati used in LoadBalancing. Each QueryPart has a id, which can be used to identify a loadBalancingProcess.
 * Implemented as Singleton.
 * @author Carsten Cordes
 *
 */
public class LoadBalancingStatusCache {

	/**
	 * The logger instance for this class.
	 */
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(LoadBalancingStatusCache.class);
	
	/**
	 * HashMap used to store the QueryParts with a particular id.
	 */
	private HashMap<Integer,LoadBalancingStatus> store;

	/**
	 * Instance variable
	 */
	private static LoadBalancingStatusCache instance;
	
	/** 
	 * Current Process counter.
	 */
	private int counter;

	/**
	 * Returns Instance
	 * @return instance of Cache
	 */
	public static LoadBalancingStatusCache getInstance() {
		if(instance==null) {
			instance = new LoadBalancingStatusCache();
			instance.store = new HashMap<Integer,LoadBalancingStatus>();
			instance.counter = 0;
		}
		return instance;
	}
	
	/**
	 * private Constructor -> use getInstance().
	 */
	private LoadBalancingStatusCache() {
	}

	/**
	 * Adds new Status for QueryPart and generates new Id.
	 * @param part QueryPart to add.
	 * @return LoadBalancingProcessId.
	 */
	public synchronized int createNewProcess(ILogicalQueryPart part) {
		
		LoadBalancingStatus status = new LoadBalancingStatus();
		status.setOriginalPart(part);
		
		store.put(counter, status);
		int loadBalancingProcessId = counter;
		counter++;
		return loadBalancingProcessId;
	}
	
	/**
	 * Gets Query Part
	 * @param loadBalancingProcessId LoadBalancingProcessId
	 * @return QueryPart
	 */
	public synchronized ILogicalQueryPart getOriginalQueryPart(int loadBalancingProcessId) {
		return store.get(loadBalancingProcessId).getOriginalPart();
	}
	
	public synchronized ILogicalQueryPart getModifiedQueryPart(int loadBalancingProcessId) {
		return store.get(loadBalancingProcessId).getModifiedPart();
	}
	
	public synchronized LoadBalancingStatus getStatus(int loadBalancingProcessId) {
		return store.get(loadBalancingProcessId);
	}
	
	public synchronized LoadBalancingStatus.LB_PHASES getPhase(int loadBalancingProcessId) {
		return store.get(loadBalancingProcessId).getPhase();
	}
	
	public synchronized void setPhase(int loadBalancingProcessId, LoadBalancingStatus.LB_PHASES phase) {
		store.get(loadBalancingProcessId).setPhase(phase);
	}

	/**
	 * Removes a Status for a particular Id.
	 * @param loadBalancingProcessId
	 */
	public void delete(int loadBalancingProcessId) {
		if(store.containsKey(loadBalancingProcessId))
			store.remove(loadBalancingProcessId);
	}
	
	
	

}