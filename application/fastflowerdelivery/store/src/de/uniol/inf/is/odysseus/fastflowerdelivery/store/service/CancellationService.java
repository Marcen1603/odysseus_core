package de.uniol.inf.is.odysseus.fastflowerdelivery.store.service;

import de.uniol.inf.is.odysseus.fastflowerdelivery.io.EventSenderRegistry;
import de.uniol.inf.is.odysseus.fastflowerdelivery.json.JsonData;
import de.uniol.inf.is.odysseus.fastflowerdelivery.json.JsonEvent;
import de.uniol.inf.is.odysseus.fastflowerdelivery.service.AbstractWebService;
import de.uniol.inf.is.odysseus.fastflowerdelivery.service.ServiceRequest;
import de.uniol.inf.is.odysseus.fastflowerdelivery.store.dao.DeliveryRequestDAO;
import de.uniol.inf.is.odysseus.fastflowerdelivery.store.event.DeliveryRequest;
import de.uniol.inf.is.odysseus.fastflowerdelivery.store.event.DeliveryRequestCancellation;

/**
 * Cancels an active delivery
 *
 * @author Weert Stamm
 * @version 1.0
 */
public class CancellationService extends AbstractWebService {

	private static final long serialVersionUID = -6187167417918927812L;

	public CancellationService() {
		setWebPath("/cancel");
	}
	
	@Override
	protected JsonData serve(ServiceRequest request) {
		// Create DeliveryRequest Event and send it to odysseus
		DeliveryRequestCancellation cancelRequest = new DeliveryRequestCancellation(request);
		EventSenderRegistry.getInstance().get(DeliveryRequestCancellation.class).accept(cancelRequest);
		
		// Remove corresponding Delivery Request event from the system
		DeliveryRequest remove = DeliveryRequestDAO.getInstance().findById(cancelRequest.getRequestId());
		DeliveryRequestDAO.getInstance().remove(remove);
		
		// Return event to the website		
		return new JsonEvent(cancelRequest);
	}

}
