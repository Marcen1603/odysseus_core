package de.uniol.inf.is.odysseus.fastflowerdelivery.driver.event;

import de.uniol.inf.is.odysseus.fastflowerdelivery.driver.dao.BidRequestDAO;
import de.uniol.inf.is.odysseus.fastflowerdelivery.event.AbstractEvent;
import de.uniol.inf.is.odysseus.fastflowerdelivery.io.ISourceEventHandler;
import de.uniol.inf.is.odysseus.generator.DataTuple;

/**
 * Represents the Delivery Bid event
 *
 * @author Weert Stamm
 * @version 1.0
 */
public class DeliveryBid extends AbstractEvent implements ISourceEventHandler {

	private String storeReference = "";
	private String driverReference = "";
	private long requiredPickupTime = 0;
	private long requiredDeliveryTime = 0;
	
	public DeliveryBid() {}

	public DeliveryBid(int requestId, String driver) {
		this.driverReference = driver;
		this.requestId = requestId;
		BidRequest request = BidRequestDAO.getInstance().findByIdAndDriver(requestId, driver);
		this.storeReference = request.getStoreReference();
		this.requiredPickupTime = request.getRequiredPickupTime();
		this.requiredDeliveryTime = request.getRequiredDeliveryTime();
	}

	@Override
	public DataTuple getTuple() {
		DataTuple result = new de.uniol.inf.is.odysseus.generator.DataTuple();
		result.addAttribute(new Long(occurrenceTime)); // Occurrence time (header)
		result.addAttribute(new Integer(requestId)); // Request id
		result.addAttribute(new String(driverReference)); // Store reference
		result.addAttribute(new String(storeReference)); // Addressee location
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
