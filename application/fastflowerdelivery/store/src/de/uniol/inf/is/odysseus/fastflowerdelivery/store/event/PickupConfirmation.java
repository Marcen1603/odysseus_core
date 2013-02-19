package de.uniol.inf.is.odysseus.fastflowerdelivery.store.event;

import de.uniol.inf.is.odysseus.fastflowerdelivery.event.AbstractEvent;
import de.uniol.inf.is.odysseus.fastflowerdelivery.io.ISourceEventHandler;
import de.uniol.inf.is.odysseus.generator.DataTuple;

/**
 * Represents a Pickup Confirmation event
 *
 * @author Weert Stamm
 * @version 1.0
 */
public class PickupConfirmation extends AbstractEvent implements ISourceEventHandler{

	private String storeReference;
	private String driverReference;
	
	public PickupConfirmation() { }
	
	public PickupConfirmation(int requestId, String storeReference, String driverReference) {
		this.requestId = requestId;
		this.storeReference = storeReference;
		this.driverReference = driverReference;
	}
	
	@Override
	public DataTuple getTuple() {
		DataTuple result = new DataTuple();
		result.addAttribute(new Long(occurrenceTime)); // Occurrence time (header)
		result.addAttribute(new Integer(requestId)); // Request id
		result.addAttribute(new String(storeReference)); // Store reference
		result.addAttribute(new String(driverReference)); // Driver reference
		return result;
	}
	
	public String getStoreReference() {
		return storeReference;
	}

	public void setStoreReference(String storeReference) {
		this.storeReference = storeReference;
	}

	public String getDriverReference() {
		return driverReference;
	}

	public void setDriverReference(String driverReference) {
		this.driverReference = driverReference;
	}
	
}
