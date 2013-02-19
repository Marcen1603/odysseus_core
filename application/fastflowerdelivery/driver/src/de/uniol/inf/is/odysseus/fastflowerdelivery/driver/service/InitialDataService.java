package de.uniol.inf.is.odysseus.fastflowerdelivery.driver.service;


import java.util.List;

import de.uniol.inf.is.odysseus.fastflowerdelivery.driver.dao.BidRequestDAO;
import de.uniol.inf.is.odysseus.fastflowerdelivery.event.AbstractEvent;
import de.uniol.inf.is.odysseus.fastflowerdelivery.json.JsonData;
import de.uniol.inf.is.odysseus.fastflowerdelivery.json.JsonEvents;
import de.uniol.inf.is.odysseus.fastflowerdelivery.service.AbstractWebService;
import de.uniol.inf.is.odysseus.fastflowerdelivery.service.ServiceRequest;

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
		// Fetch the driver reference
		String driverReference = request.getStringParameter("driver");
		// Get all recent Bid Requests that match that driver reference
		List<AbstractEvent> bidRequests = BidRequestDAO.getInstance().findRequestsByDriver(driverReference);
		return new JsonEvents(bidRequests);
	}

}
