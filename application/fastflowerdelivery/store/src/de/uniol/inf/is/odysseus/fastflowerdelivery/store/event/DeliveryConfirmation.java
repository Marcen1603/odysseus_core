package de.uniol.inf.is.odysseus.fastflowerdelivery.store.event;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.fastflowerdelivery.event.AbstractEvent;
import de.uniol.inf.is.odysseus.fastflowerdelivery.store.dao.DeliveryRequestDAO;
import de.uniol.inf.is.odysseus.fastflowerdelivery.store.dao.LiveEventRegistry;
import de.uniol.inf.is.odysseus.fastflowerdelivery.io.ISinkEventHandler;

/**
 * Represents a Delivery Confirmation event
 *
 * @author Weert Stamm
 * @version 1.0
 */
public class DeliveryConfirmation extends AbstractEvent implements ISinkEventHandler {

	private String driverReference;
	
	public DeliveryConfirmation() { }
	
	@SuppressWarnings("rawtypes")
	public DeliveryConfirmation(Tuple tuple) {
		Object[] attributes = tuple.getAttributes();
		this.occurrenceTime = (long) attributes[0];
		this.requestId = (int) attributes[1];
		this.setDriverReference((String) attributes[2]);
	}
	
	@Override
	public List<String> getSchema() {
		ArrayList<String> result = new ArrayList<String>();
		result.add("StartTimestamp");
		result.add("Integer");
		result.add("String");
		result.add("StartTimestamp");
		result.add("EndTimestamp");
		return result;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void processTuple(Tuple tuple) {
		// create event from tuple
		DeliveryConfirmation confirmation = new DeliveryConfirmation(tuple);
		
		// set the corresponding delivery to done
		DeliveryRequestDAO.getInstance().findById(confirmation.getRequestId()).setDeliveryDone(true);
		
		// register event for website
		LiveEventRegistry.getInstance().register(confirmation);
	}

	public String getDriverReference() {
		return driverReference;
	}

	public void setDriverReference(String driverReference) {
		this.driverReference = driverReference;
	}
	
}
