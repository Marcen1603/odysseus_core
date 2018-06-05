package de.uniol.inf.is.odysseus.fastflowerdelivery.store.dao;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.fastflowerdelivery.event.AbstractEvent;
import de.uniol.inf.is.odysseus.fastflowerdelivery.store.event.DeliveryRequest;

/**
 * Keeps track of all Delivery Request event that are created
 *
 * @author Weert Stamm
 * @version 1.0
 */
public class DeliveryRequestDAO {
	
	private static int ID_COUNTER = 0;
	
	private List<DeliveryRequest> requests = new ArrayList<DeliveryRequest>();
	
	private DeliveryRequestDAO() { }
	private static DeliveryRequestDAO instance = null;
	public static DeliveryRequestDAO getInstance() {
		if(instance == null)
			instance = new DeliveryRequestDAO();
		return instance;
	}
	
	/**
	 * Adds a Delivery Request event
	 * @param request
	 * 			the event to add
	 */
	public synchronized void add(DeliveryRequest request) {
		request.setRequestId(++ID_COUNTER);
		requests.add(request);
	}
	
	/**
	 * Removes a Delivery Request event, typically used when the delivery is done
	 * @param request
	 * 			the event to remove
	 */
	public synchronized void remove(DeliveryRequest request) {
		requests.remove(request);
	}
	
	/**
	 * Returns a list of all Delivery Request events that match a certain store reference.
	 * @param storeReference
	 * 			the store reference to match against
	 * @return a list of Delivery Request events
	 */
	public synchronized List<AbstractEvent> findActiveDeliveriesByStore(String storeReference) {
		ArrayList<AbstractEvent> result = new ArrayList<AbstractEvent>();
		for(DeliveryRequest request : requests)
			// Only chooses requests that match the store reference and are not delivered
			if(!request.isDeliveryDone() && request.getStoreReference().equals(storeReference)) 
				result.add(request);
		
		return result;
	}
	
	/**
	 * Finds active Delivery Requests by matching against a store reference
	 * @param storeReference
	 * 			the store reference to match against
	 * @return a list of Delivery Request events
	 */
	public synchronized List<DeliveryRequest> findDeliveriesByStore(String storeReference) {
		ArrayList<DeliveryRequest> result = new ArrayList<DeliveryRequest>();
		for(DeliveryRequest request : requests)
			// Only chooses requests that match the store reference
			if(request.getStoreReference().equals(storeReference)) 
				result.add(request);
		
		return result;
	}
	
	/**
	 * Finds a certain Delivery Request event by matching against the request id.
	 * @param id
	 * 			the request id
	 * @return a certain Delivery Request event, or null if none is found
	 */
	public synchronized DeliveryRequest findById(int id) {
		for(DeliveryRequest request : requests)
			if(request.getRequestId() == id)
				return request;
		return null;
	}
}
