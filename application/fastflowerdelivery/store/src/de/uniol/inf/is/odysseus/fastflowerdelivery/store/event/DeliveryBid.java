package de.uniol.inf.is.odysseus.fastflowerdelivery.store.event;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.fastflowerdelivery.store.dao.DeliveryRequestDAO;
import de.uniol.inf.is.odysseus.fastflowerdelivery.store.dao.LiveEventRegistry;
import de.uniol.inf.is.odysseus.fastflowerdelivery.event.AbstractEvent;
import de.uniol.inf.is.odysseus.fastflowerdelivery.io.ISinkEventHandler;

/**
 * Represents a Delivery Bid event
 *
 * @author Weert Stamm
 * @version 1.0
 */
public class DeliveryBid extends AbstractEvent implements ISinkEventHandler {

	private String storeReference = "";
	private String driverReference = "";
	private long requiredPickupTime = 0;
	private long requiredDeliveryTime = 0;
	
	public DeliveryBid() {}
	
	@SuppressWarnings("rawtypes")
	public DeliveryBid(Tuple tuple) {
		Object[] attributes = tuple.getAttributes();
		this.occurrenceTime = (long) attributes[0];
		this.requestId = (int) attributes[1];
		this.storeReference = (String) attributes[3];
		this.driverReference = (String) attributes[2];
		this.requiredPickupTime = (long) attributes[4];
		this.requiredDeliveryTime = (long) attributes[5];
	}
	
	@Override
	public List<String> getSchema() {
		ArrayList<String> result = new ArrayList<String>();
		result.add("StartTimestamp");
		result.add("Integer");
		result.add("String");
		result.add("String");
		result.add("Long");
		result.add("Long");
		result.add("StartTimestamp");
		result.add("EndTimestamp");
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void processTuple(Tuple tuple) {
		// create bid from tuple
		DeliveryBid bid = new DeliveryBid(tuple);
		
		// Add driver to request
		Driver driver = new Driver();
		driver.setReference(bid.getDriverReference());
		DeliveryRequest request = DeliveryRequestDAO.getInstance().findById(bid.getRequestId());
		if(request != null)
			request.addDriver(driver);
		
		// register driver for website
		LiveEventRegistry.getInstance().register(driver);
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
