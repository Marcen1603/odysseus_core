package de.uniol.inf.is.odysseus.fastflowerdelivery.driver.service;

import de.uniol.inf.is.odysseus.fastflowerdelivery.driver.dao.BidRequestDAO;
import de.uniol.inf.is.odysseus.fastflowerdelivery.driver.event.DeliveryConfirmation;
import de.uniol.inf.is.odysseus.fastflowerdelivery.io.EventSenderRegistry;
import de.uniol.inf.is.odysseus.fastflowerdelivery.json.JsonData;
import de.uniol.inf.is.odysseus.fastflowerdelivery.json.JsonSuccess;
import de.uniol.inf.is.odysseus.fastflowerdelivery.service.AbstractWebService;
import de.uniol.inf.is.odysseus.fastflowerdelivery.service.ServiceRequest;

/**
 * A webservice used to create and send a Delivery Confirmation event.
 *
 * @author Weert Stamm
 * @version 1.0
 */
public class DeliveryConfirmationService extends AbstractWebService {

	private static final long serialVersionUID = -4449088430364946155L;

	public DeliveryConfirmationService() {
		setWebPath("/delivered");
	}
	
	@Override
	protected JsonData serve(ServiceRequest request) {
		// Fetch request parameters
		int requestId = request.getIntegerParameter("requestId");
		String driverReference = request.getStringParameter("driver");
		boolean internal = Boolean.parseBoolean(request.getStringParameter("internal"));
		
		// Update the corresponding Bid Request event
		BidRequestDAO.getInstance().findByIdAndDriver(requestId, driverReference).setDelivered(true);
		
		// Create and send the Delivery Confirmation event
		if(!internal) {
			DeliveryConfirmation confirmation = new DeliveryConfirmation(requestId, driverReference);
			EventSenderRegistry.getInstance().get(DeliveryConfirmation.class).accept(confirmation);
		}
		
		return new JsonSuccess();
	}

}
