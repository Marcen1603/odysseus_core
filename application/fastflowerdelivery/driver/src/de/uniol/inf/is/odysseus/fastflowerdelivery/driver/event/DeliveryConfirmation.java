package de.uniol.inf.is.odysseus.fastflowerdelivery.driver.event;

import de.uniol.inf.is.odysseus.fastflowerdelivery.event.AbstractEvent;
import de.uniol.inf.is.odysseus.fastflowerdelivery.io.ISourceEventHandler;
import de.uniol.inf.is.odysseus.generator.DataTuple;

/**
 * Represents the Delivery Confirmation event.
 *
 * @author Weert Stamm
 * @version 1.0
 */
public class DeliveryConfirmation extends AbstractEvent implements ISourceEventHandler {

	private String driverReference;
	
	public DeliveryConfirmation() {}
	
	public DeliveryConfirmation(int requestId, String driverReference) {
		this.requestId = requestId;
		this.driverReference = driverReference;
	}
	
	@Override
	public DataTuple getTuple() {
		DataTuple result = new de.uniol.inf.is.odysseus.generator.DataTuple();
		result.addAttribute(new Long(occurrenceTime)); // Occurrence time (header)
		result.addAttribute(new Integer(requestId)); // Request id
		result.addAttribute(new String(driverReference)); // Store reference
		return result;
	}

	public String getDriverReference() {
		return driverReference;
	}

	public void setDriverReference(String driverReference) {
		this.driverReference = driverReference;
	}

}
