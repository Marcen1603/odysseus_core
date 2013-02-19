package de.uniol.inf.is.odysseus.fastflowerdelivery.driver.service;

import de.uniol.inf.is.odysseus.fastflowerdelivery.driver.dao.BidRequestDAO;
import de.uniol.inf.is.odysseus.fastflowerdelivery.driver.event.DeliveryBid;
import de.uniol.inf.is.odysseus.fastflowerdelivery.io.EventSenderRegistry;
import de.uniol.inf.is.odysseus.fastflowerdelivery.json.JsonData;
import de.uniol.inf.is.odysseus.fastflowerdelivery.json.JsonSuccess;
import de.uniol.inf.is.odysseus.fastflowerdelivery.service.AbstractWebService;
import de.uniol.inf.is.odysseus.fastflowerdelivery.service.ServiceRequest;

/**
 * A webservice used to create and send a Delivery Bid event.
 *
 * @author Weert Stamm
 * @version 1.0
 */
public class BidService extends AbstractWebService {

	private static final long serialVersionUID = -8000361014464313891L;
	
	public BidService() {
		setWebPath("/placeBid");
	}

	@Override
	protected JsonData serve(ServiceRequest request) {
		// Fetch request parameters
		int requestId = request.getIntegerParameter("requestId");
		String driverReference = request.getStringParameter("driver");
		
		// Update the Bid Request event
		BidRequestDAO.getInstance().findByIdAndDriver(requestId, driverReference).setBidPlaced(true);
		
		// Create and send the Delivery Bid event
		DeliveryBid bid = new DeliveryBid(requestId, driverReference);
		EventSenderRegistry.getInstance().get(DeliveryBid.class).accept(bid);
		
		return new JsonSuccess();
	}

}
