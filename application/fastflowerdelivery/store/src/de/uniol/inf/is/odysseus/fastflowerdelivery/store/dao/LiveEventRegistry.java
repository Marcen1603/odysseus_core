package de.uniol.inf.is.odysseus.fastflowerdelivery.store.dao;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.fastflowerdelivery.event.AbstractEvent;
import de.uniol.inf.is.odysseus.fastflowerdelivery.store.event.DeliveryRequest;

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
	
	ArrayList<AbstractEvent> registeredData = new ArrayList<AbstractEvent>();
	
	private LiveEventRegistry() {}
	
	/**
	 * Registers a new event to be delivered to the client.
	 * @param event
	 * 			the event to deliver to the client
	 */
	synchronized public void register(AbstractEvent event) {
		registeredData.add(event);
	}
	
	/**
	 * Returns a list of all events that match a certain store reference
	 * @param storeReference
	 * 			the store reference to match against
	 * @return a list of events
	 */
	synchronized public List<AbstractEvent> getEventsByStore(String storeReference) {
		ArrayList<AbstractEvent> result = new ArrayList<AbstractEvent>();
		if(registeredData.size() > 0) {
			List<DeliveryRequest> requests = DeliveryRequestDAO.getInstance().findDeliveriesByStore(storeReference);
			for(AbstractEvent event : registeredData)
				if(belongsToAnyRequest(requests, event))
					result.add(event);
			for(AbstractEvent event : result)
				registeredData.remove(event);
		}
		return result;
	}

	private boolean belongsToAnyRequest(List<DeliveryRequest> requests, AbstractEvent event) {
		for(DeliveryRequest request : requests)
			if(request.getRequestId() == event.getRequestId())
				return true;
		return false;
	}

}
