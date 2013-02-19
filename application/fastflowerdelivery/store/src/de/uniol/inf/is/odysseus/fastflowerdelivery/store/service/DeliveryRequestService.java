package de.uniol.inf.is.odysseus.fastflowerdelivery.store.service;


import de.uniol.inf.is.odysseus.fastflowerdelivery.service.AbstractWebService;
import de.uniol.inf.is.odysseus.fastflowerdelivery.service.ServiceRequest;
import de.uniol.inf.is.odysseus.fastflowerdelivery.io.EventSenderRegistry;
import de.uniol.inf.is.odysseus.fastflowerdelivery.store.event.DeliveryRequest;
import de.uniol.inf.is.odysseus.fastflowerdelivery.json.JsonData;
import de.uniol.inf.is.odysseus.fastflowerdelivery.json.JsonEvent;

/**
 * Accepts POST requests from the website, regarding creating a new Delivery Request Event
 *
 * @author Weert Stamm
 * @version 1.0
 */
public class DeliveryRequestService extends AbstractWebService {

	private static final long serialVersionUID = 5871009476949260296L;
	
	public DeliveryRequestService() {
		setWebPath("/deliveryRequest");
	}

	@Override
	protected JsonData serve(ServiceRequest request) {
		// Create DeliveryRequest Event and send it to odysseus
		DeliveryRequest deliveryRequest = new DeliveryRequest(request);
		EventSenderRegistry.getInstance().get(DeliveryRequest.class).accept(deliveryRequest);
		
		// Return event to the website		
		return new JsonEvent(deliveryRequest);
	}
}
