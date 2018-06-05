package de.uniol.inf.is.odysseus.fastflowerdelivery.store.service;


import java.util.List;

import de.uniol.inf.is.odysseus.fastflowerdelivery.event.AbstractEvent;
import de.uniol.inf.is.odysseus.fastflowerdelivery.service.AbstractWebService;
import de.uniol.inf.is.odysseus.fastflowerdelivery.service.ServiceRequest;
import de.uniol.inf.is.odysseus.fastflowerdelivery.store.dao.DeliveryRequestDAO;
import de.uniol.inf.is.odysseus.fastflowerdelivery.json.JsonData;
import de.uniol.inf.is.odysseus.fastflowerdelivery.json.JsonEvents;

/**
 * A webservice that is used to fetch any data that has already been in use.
 * Necessary if the user refreshes the page, to repopulate it.
 *
 * @author Weert Stamm
 * @version 1.0
 */
public class InitialDataService extends AbstractWebService {

	private static final long serialVersionUID = 5507204145688984809L;

	public InitialDataService() {
		setWebPath("/initial");
	}
	
	@Override
	protected JsonData serve(ServiceRequest request) {
		// Fetch the store reference
		String storeReference = request.getStringParameter("store");
		// Get all recent events that match the store reference
		List<AbstractEvent> deliveryRequests = DeliveryRequestDAO.getInstance().findActiveDeliveriesByStore(storeReference);
		return new JsonEvents(deliveryRequests);
	}

}
