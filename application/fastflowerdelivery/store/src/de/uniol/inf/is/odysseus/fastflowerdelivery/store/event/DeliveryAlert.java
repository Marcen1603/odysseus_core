package de.uniol.inf.is.odysseus.fastflowerdelivery.store.event;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.fastflowerdelivery.event.AbstractEvent;
import de.uniol.inf.is.odysseus.fastflowerdelivery.io.ISinkEventHandler;
import de.uniol.inf.is.odysseus.fastflowerdelivery.store.dao.LiveEventRegistry;

/**
 * Represents a Delivery Alert event
 *
 * @author Weert Stamm
 * @version 1.0
 */
public class DeliveryAlert extends AbstractEvent implements ISinkEventHandler {
	
	private String driverReference = "";
	
	public DeliveryAlert() {}
	
	@SuppressWarnings("rawtypes")
	public DeliveryAlert(Tuple tuple) {
		Object[] attributes = tuple.getAttributes();
		this.occurrenceTime = (long) attributes[2];
		this.requestId = (int) attributes[0];
		this.setDriverReference((String) attributes[1]);
	}
	
	@Override
	public List<String> getSchema() {
		ArrayList<String> result = new ArrayList<String>();
		result.add("Integer");
		result.add("String");
		result.add("StartTimestamp");
		result.add("EndTimestamp");
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void processTuple(Tuple tuple) {
		// create alert from tuple
		DeliveryAlert alert = new DeliveryAlert(tuple);
		
		// register alert for website
		LiveEventRegistry.getInstance().register(alert);
	}

	public String getDriverReference() {
		return driverReference;
	}

	public void setDriverReference(String driverReference) {
		this.driverReference = driverReference;
	}
}
