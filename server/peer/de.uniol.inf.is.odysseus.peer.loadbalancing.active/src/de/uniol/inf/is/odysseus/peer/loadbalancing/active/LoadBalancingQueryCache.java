package de.uniol.inf.is.odysseus.peer.loadbalancing.active;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;

/**
 * Store for QueryParts used in LoadBalancing. Each QueryPart has a id, which can be used to identify a loadBalancingProcess.
 * Implemented as Singleton.
 * @author Carsten Cordes
 *
 */
public class LoadBalancingQueryCache {

	/**
	 * The logger instance for this class.
	 */
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(LoadBalancingQueryCache.class);
	
	/**
	 * HashMap used to store the QueryParts with a particular id.
	 */
	private HashMap<Integer,ILogicalQueryPart> store;

	/**
	 * Instance variable
	 */
	private static LoadBalancingQueryCache instance;
	
	/** 
	 * Current QueryPart counter.
	 */
	private int counter;

	/**
	 * Returns Instance
	 * @return instance of Cache
	 */
	public static LoadBalancingQueryCache getInstance() {
		if(instance==null) {
			instance = new LoadBalancingQueryCache();
			instance.store = new HashMap<Integer,ILogicalQueryPart>();
			instance.counter = 0;
		}
		return instance;
	}
	
	/**
	 * private Constructor -> use getInstance().
	 */
	private LoadBalancingQueryCache() {
	}

	/**
	 * Adds QueryPart to list and generates new Id.
	 * @param part QueryPart to add.
	 * @return LoadBalancingProcessId.
	 */
	public synchronized int addQueryPartCopy(ILogicalQueryPart part) {
		
		
		store.put(counter, part);
		int loadBalancingProcessId = counter;
		counter++;
		return loadBalancingProcessId;
	}
	
	/**
	 * Gets Query Part
	 * @param loadBalancingProcessId LoadBalancingProcessId
	 * @return QueryPart
	 */
	public synchronized ILogicalQueryPart getQueryPart(int loadBalancingProcessId) {
		return store.get(loadBalancingProcessId);
	}

	/**
	 * Removes a queryPart for a particular Id.
	 * @param loadBalancingProcessId
	 */
	public void delete(int loadBalancingProcessId) {
		if(store.containsKey(loadBalancingProcessId))
			store.remove(loadBalancingProcessId);
	}
	
	
	

}