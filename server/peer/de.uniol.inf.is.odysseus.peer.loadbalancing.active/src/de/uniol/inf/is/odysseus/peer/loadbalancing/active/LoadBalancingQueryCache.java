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
	
	private HashMap<Integer,ILogicalQueryPart> store;

	private static LoadBalancingQueryCache instance;
	
	private int counter;

	
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

	public synchronized int addQueryPartCopy(ILogicalQueryPart part) {
		
		
		store.put(counter, part);
		int queryID = counter;
		counter++;
		return queryID;
	}
	
	public synchronized ILogicalQueryPart getQueryPart(int queryId) {
		return store.get(queryId);
	}
	
	
	

}