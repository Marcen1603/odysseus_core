package de.uniol.inf.is.odysseus.fastflowerdelivery.driver.dao;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.fastflowerdelivery.driver.event.BidRequest;
import de.uniol.inf.is.odysseus.fastflowerdelivery.event.AbstractEvent;

/**
 * Keeps track of all bid request events received.
 *
 * @author Weert Stamm
 * @version 1.0
 */
public class BidRequestDAO {
	
	private List<BidRequest> requests = new ArrayList<BidRequest>();
	
	private BidRequestDAO() { }
	private static BidRequestDAO instance = null;
	public static BidRequestDAO getInstance() {
		if(instance == null)
			instance = new BidRequestDAO();
		return instance;
	}
	
	/**
	 * Adds a bid request event
	 * @param request
	 * 			the request to add
	 */
	public synchronized void add(BidRequest request) {
		requests.add(request);
	}
	
	/**
	 * removes a bid request
	 * @param request
	 * 			the request to be removed
	 */
	public synchronized void remove(BidRequest request) {
		requests.remove(request);
	}
	
	/**
	 * Finds all bid request events that match a certain driver reference
	 * @param driverReference
	 * 			the driverReference to match against
	 * @return a list of all found bid request events
	 */
	public synchronized List<AbstractEvent> findRequestsByDriver(String driverReference) {
		ArrayList<AbstractEvent> result = new ArrayList<AbstractEvent>();
		for(BidRequest request : requests)
			// Only chooses requests that match the driver reference
			if(request.getDriverReference().equals(driverReference) && !request.isDelivered())
				result.add(request);
		
		return result;
	}
	
	/**
	 * Finds a certain bid request event by matching against an id and a driver reference.
	 * @param requestId
	 * 			the request id to match against
	 * @param driverReference
	 * 			the driver reference to match against
	 * @return the bid request event
	 */
	public synchronized BidRequest findByIdAndDriver(int requestId, String driverReference) {
		for(BidRequest request : requests)
			if(request.getRequestId() == requestId && request.getDriverReference().equals(driverReference))
				return request;
		return null;
	}
}
