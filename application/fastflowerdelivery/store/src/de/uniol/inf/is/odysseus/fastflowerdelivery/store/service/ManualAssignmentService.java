package de.uniol.inf.is.odysseus.fastflowerdelivery.store.service;


import de.uniol.inf.is.odysseus.fastflowerdelivery.service.AbstractWebService;
import de.uniol.inf.is.odysseus.fastflowerdelivery.service.ServiceRequest;
import de.uniol.inf.is.odysseus.fastflowerdelivery.service.WebServiceException;
import de.uniol.inf.is.odysseus.fastflowerdelivery.store.dao.DeliveryRequestDAO;
import de.uniol.inf.is.odysseus.fastflowerdelivery.io.EventSenderRegistry;
import de.uniol.inf.is.odysseus.fastflowerdelivery.store.event.DeliveryRequest;
import de.uniol.inf.is.odysseus.fastflowerdelivery.store.event.Driver;
import de.uniol.inf.is.odysseus.fastflowerdelivery.store.event.ManualAssignment;
import de.uniol.inf.is.odysseus.fastflowerdelivery.json.JsonData;
import de.uniol.inf.is.odysseus.fastflowerdelivery.json.JsonSuccess;

/**
 * A webservice to create and send a Manual Assignment event
 *
 * @author Weert Stamm
 * @version 1.0
 */
public class ManualAssignmentService extends AbstractWebService {

	private static final long serialVersionUID = 7563840847770049696L;

	public ManualAssignmentService() {
		setWebPath("/assignment");
	}
	
	@Override
	protected JsonData serve(ServiceRequest request) {
		// Fetch request parameters
		int requestId = request.getIntegerParameter("requestId");
		String driverReference = request.getStringParameter("driverReference");
		
		// Update the corresponding delivery request, that a driver has been assigned
		DeliveryRequest deliveryRequest = DeliveryRequestDAO.getInstance().findById(requestId);
		boolean assigned = false;
		for(Driver driver : deliveryRequest.getDrivers())
			if(driver.getReference().equals(driverReference) && driver.getRequestId() == requestId) {
				driver.setAssigned(true);
				assigned = true;
			}
		
		if(!assigned)
			throw new WebServiceException("Driver " + driverReference + " was not found.");
		
		// Create ManualAssignment Event and send it to odysseus
		ManualAssignment manualAssignment = new ManualAssignment(deliveryRequest, driverReference);
		EventSenderRegistry.getInstance().get(ManualAssignment.class).accept(manualAssignment);
		
		return new JsonSuccess();
	}

}
