package de.uniol.inf.is.odysseus.fastflowerdelivery.store.service;


import de.uniol.inf.is.odysseus.fastflowerdelivery.service.AbstractWebService;
import de.uniol.inf.is.odysseus.fastflowerdelivery.service.ServiceRequest;
import de.uniol.inf.is.odysseus.fastflowerdelivery.store.dao.DeliveryRequestDAO;
import de.uniol.inf.is.odysseus.fastflowerdelivery.io.EventSenderRegistry;
import de.uniol.inf.is.odysseus.fastflowerdelivery.store.event.PickupConfirmation;
import de.uniol.inf.is.odysseus.fastflowerdelivery.json.JsonData;
import de.uniol.inf.is.odysseus.fastflowerdelivery.json.JsonSuccess;

/**
 * A webservice to create and send the Pickup Confirmation event
 *
 * @author Weert Stamm
 * @version 1.0
 */
public class PickupConfirmationService extends AbstractWebService {

	private static final long serialVersionUID = -3799397355922607544L;

	public PickupConfirmationService() {
		setWebPath("/pickup");
	}
	
	@Override
	protected JsonData serve(ServiceRequest request) {
		// Fetch request parameters
		int requestId = request.getIntegerParameter("requestId");		
		String driverReference = request.getStringParameter("driverReference");
		String storeReference = request.getStringParameter("storeReference");
		
		// Update corresponding DeliveryRequest
		DeliveryRequestDAO.getInstance().findById(requestId).setPickupDone(true);
		
		// Create Pickup Confirmation Event and send it to odysseus
		PickupConfirmation pickupConfirmation = new PickupConfirmation(requestId, driverReference, storeReference);
		EventSenderRegistry.getInstance().get(PickupConfirmation.class).accept(pickupConfirmation);
		
		return new JsonSuccess();
	}

}
