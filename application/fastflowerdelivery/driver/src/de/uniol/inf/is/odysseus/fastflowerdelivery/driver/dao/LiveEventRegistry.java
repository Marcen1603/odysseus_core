package de.uniol.inf.is.odysseus.fastflowerdelivery.driver.dao;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.fastflowerdelivery.driver.event.BidRequest;
import de.uniol.inf.is.odysseus.fastflowerdelivery.event.AbstractEvent;

/**
 * This registry keeps track of all incoming events.
 * It is used by a webservice to deliver any type of event to the client.
 *
 * @author Weert Stamm
 * @version 1.0
 */
public class LiveEventRegistry {
	
	private static LiveEventRegistry instance = null;
	synchronized public static LiveEventRegistry getInstance() {
		if(instance == null)
			instance = new LiveEventRegistry();
		return instance;
	}
	
	ArrayList<AbstractEvent> eventPool = new ArrayList<AbstractEvent>();
	
	private LiveEventRegistry() {}
	
	/**
	 * Registers a new event to be delivered to the client.
	 * @param event
	 * 			the event to deliver to the client
	 */
	synchronized public void register(AbstractEvent event) {
		eventPool.add(event);
	}
	
	/**
	 * Returns a list of all events that match a certain driverReference
	 * @param driverReference
	 * 			the driver reference to match against
	 * @return a list of events
	 */
	synchronized public List<AbstractEvent> getEventsByDriver(String driverReference) {
		ArrayList<AbstractEvent> result = new ArrayList<AbstractEvent>();
		
		if(eventPool.size() > 0) {
			List<AbstractEvent> requests = BidRequestDAO.getInstance().findRequestsByDriver(driverReference);
			for(AbstractEvent event : eventPool)
				// Only choose events that belong to any request matching the driver reference
				if(belongsToAnyRequest(requests, event, driverReference))
					result.add(event);
			
			// Remove results from the event pool
			for(AbstractEvent event : result)
				eventPool.remove(event);
		}
		return result;
	}

	private boolean belongsToAnyRequest(List<AbstractEvent> requests, AbstractEvent event, String driverReference) {
		for(AbstractEvent request : requests)
			if(request.getRequestId() == event.getRequestId() && ((BidRequest)request).getDriverReference().equals(driverReference))
				return true;
		return false;
	}

}
