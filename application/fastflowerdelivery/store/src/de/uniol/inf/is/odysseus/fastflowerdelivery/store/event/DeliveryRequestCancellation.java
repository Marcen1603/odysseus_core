package de.uniol.inf.is.odysseus.fastflowerdelivery.store.event;

import de.uniol.inf.is.odysseus.fastflowerdelivery.event.AbstractEvent;
import de.uniol.inf.is.odysseus.fastflowerdelivery.io.ISourceEventHandler;
import de.uniol.inf.is.odysseus.fastflowerdelivery.service.ServiceRequest;
import de.uniol.inf.is.odysseus.generator.DataTuple;

/**
 * Represents the Delivery Request Cancellation event. Used to cancel delivieries
 *
 * @author Weert Stamm
 * @version 1.0
 */
public class DeliveryRequestCancellation extends AbstractEvent implements ISourceEventHandler {

	public DeliveryRequestCancellation() {}

	public DeliveryRequestCancellation(ServiceRequest request) {
		this.requestId = request.getIntegerParameter("requestId");
	}

	@Override
	public DataTuple getTuple() {
		DataTuple result = new DataTuple();
		result.addAttribute(new Long(occurrenceTime)); // Occurrence time (header)
		result.addAttribute(new Integer(requestId)); // Request id
		return result;
	}
	
}
