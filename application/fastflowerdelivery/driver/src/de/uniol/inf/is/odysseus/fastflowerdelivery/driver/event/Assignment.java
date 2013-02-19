package de.uniol.inf.is.odysseus.fastflowerdelivery.driver.event;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.fastflowerdelivery.driver.dao.BidRequestDAO;
import de.uniol.inf.is.odysseus.fastflowerdelivery.driver.dao.LiveEventRegistry;
import de.uniol.inf.is.odysseus.fastflowerdelivery.event.AbstractEvent;
import de.uniol.inf.is.odysseus.fastflowerdelivery.io.ISinkEventHandler;

/**
 * Represents the Assignment event.
 *
 * @author Weert Stamm
 * @version 1.0
 */
public class Assignment extends AbstractEvent implements ISinkEventHandler {

	private String storeReference;
	private String driverReference;
	private String addresseeLocation;
	private long committedPickupTime;
	private long requiredDeliveryTime;

	public Assignment() {}
	
	@SuppressWarnings("rawtypes")
	public Assignment(Tuple tuple) {
		Object[] attributes = tuple.getAttributes();
		this.occurrenceTime = (long) attributes[0];
		this.requestId = (int) attributes[1];
		this.storeReference = (String) attributes[2];
		this.driverReference = (String) attributes[3];
		this.addresseeLocation = (String) attributes[4];
		this.committedPickupTime = (long) attributes[5];
		this.requiredDeliveryTime = (long) attributes[6];
	}

	@Override
	public List<String> getSchema() {
		ArrayList<String> result = new ArrayList<String>();
		result.add("StartTimestamp");
		result.add("Integer");
		result.add("String");
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
		Assignment event = new Assignment(tuple);
		// Change status of corresponding BidRequest
		BidRequest br = BidRequestDAO.getInstance().findByIdAndDriver(event.getRequestId(), event.getDriverReference());
		br.setAssigned(true);
		br.setAssignedTo(event.getDriverReference());
		
		LiveEventRegistry.getInstance().register(event);
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

	public long getCommittedPickupTime() {
		return committedPickupTime;
	}

	public void setCommittedPickupTime(long committedPickupTime) {
		this.committedPickupTime = committedPickupTime;
	}

	public long getRequiredDeliveryTime() {
		return requiredDeliveryTime;
	}

	public void setRequiredDeliveryTime(long requiredDeliveryTime) {
		this.requiredDeliveryTime = requiredDeliveryTime;
	}
}
