package de.uniol.inf.is.odysseus.fastflowerdelivery.driver.event;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.fastflowerdelivery.driver.dao.BidRequestDAO;
import de.uniol.inf.is.odysseus.fastflowerdelivery.driver.dao.LiveEventRegistry;
import de.uniol.inf.is.odysseus.fastflowerdelivery.event.AbstractEvent;
import de.uniol.inf.is.odysseus.fastflowerdelivery.io.ISinkEventHandler;

/**
 * Represents the Pickup Confirmation event
 *
 * @author Weert Stamm
 * @version 1.0
 */
public class PickupConfirmation extends AbstractEvent implements ISinkEventHandler {

	private String storeReference;
	private String driverReference;
	
	public PickupConfirmation() {}
	
	@SuppressWarnings("rawtypes")
	public PickupConfirmation(Tuple tuple) {
		Object[] attributes = tuple.getAttributes();
		this.occurrenceTime = (long) attributes[0];
		this.requestId = (int) attributes[1];
		this.setDriverReference((String) attributes[2]);
		this.setStoreReference((String) attributes[3]);
	}

	@Override
	public List<String> getSchema() {
		ArrayList<String> result = new ArrayList<String>();
		result.add("StartTimestamp");
		result.add("Integer");
		result.add("String");
		result.add("String");
		result.add("StartTimestamp");
		result.add("EndTimestamp");
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void processTuple(Tuple tuple) {
		PickupConfirmation event = new PickupConfirmation(tuple);
		BidRequestDAO.getInstance().findByIdAndDriver(event.getRequestId(), event.getDriverReference()).setPickedUp(true);
		LiveEventRegistry.getInstance().register(event);
	}

	public String getDriverReference() {
		return driverReference;
	}

	public void setDriverReference(String driverReference) {
		this.driverReference = driverReference;
	}

	public String getStoreReference() {
		return storeReference;
	}

	public void setStoreReference(String storeReference) {
		this.storeReference = storeReference;
	}

}
