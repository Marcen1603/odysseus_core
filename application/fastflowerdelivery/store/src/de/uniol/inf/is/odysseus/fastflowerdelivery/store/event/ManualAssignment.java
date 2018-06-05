package de.uniol.inf.is.odysseus.fastflowerdelivery.store.event;

import de.uniol.inf.is.odysseus.fastflowerdelivery.event.AbstractEvent;
import de.uniol.inf.is.odysseus.fastflowerdelivery.io.ISourceEventHandler;
import de.uniol.inf.is.odysseus.generator.DataTuple;

/**
 * Represents a Manual Assignment event
 *
 * @author Weert Stamm
 * @version 1.0
 */
public class ManualAssignment extends AbstractEvent implements ISourceEventHandler{

	private String storeReference;
	private String driverReference;
	private String addresseeLocation;
	private long requiredPickupTime;
	private long requiredDeliveryTime;
	
	public ManualAssignment() { }
	
	public ManualAssignment(DeliveryRequest request, String driverReference) {
		this.requestId = request.getRequestId();
		this.storeReference = request.getStoreReference();
		this.driverReference = driverReference;
		this.addresseeLocation = request.getAdresseeLocation();
		this.requiredPickupTime = request.getRequiredPickupTime();
		this.requiredDeliveryTime = request.getRequiredDeliveryTime();
	}
	
	@Override
	public DataTuple getTuple() {
		DataTuple result = new DataTuple();
		result.addAttribute(new Long(occurrenceTime)); // Occurrence time (header)
		result.addAttribute(new Integer(requestId)); // Request id
		result.addAttribute(new String(storeReference)); // Store reference
		result.addAttribute(new String(driverReference)); // Driver reference
		result.addAttribute(new String(addresseeLocation)); // Addressee location
		result.addAttribute(new Long(requiredPickupTime)); // Required pickup time +10min
		result.addAttribute(new Long(requiredDeliveryTime)); // Required delivery time +20min
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

	public String getAddresseeLocation() {
		return addresseeLocation;
	}

	public void setAddresseeLocation(String addresseeLocation) {
		this.addresseeLocation = addresseeLocation;
	}

	public long getRequiredPickupTime() {
		return requiredPickupTime;
	}

	public void setRequiredPickupTime(long requiredPickupTime) {
		this.requiredPickupTime = requiredPickupTime;
	}

	public long getRequiredDeliveryTime() {
		return requiredDeliveryTime;
	}

	public void setRequiredDeliveryTime(long requiredDeliveryTime) {
		this.requiredDeliveryTime = requiredDeliveryTime;
	}
	
}
