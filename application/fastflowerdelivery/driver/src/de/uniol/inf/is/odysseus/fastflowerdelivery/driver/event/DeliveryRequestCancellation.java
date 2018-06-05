package de.uniol.inf.is.odysseus.fastflowerdelivery.driver.event;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.fastflowerdelivery.driver.dao.LiveEventRegistry;
import de.uniol.inf.is.odysseus.fastflowerdelivery.event.AbstractEvent;
import de.uniol.inf.is.odysseus.fastflowerdelivery.io.ISinkEventHandler;

/**
 * Notifies the system that a particular Delivery Request has been cancelled.
 *
 * @author Weert Stamm
 * @version 1.0
 */
public class DeliveryRequestCancellation extends AbstractEvent implements ISinkEventHandler {

	public DeliveryRequestCancellation() {}
	
	@SuppressWarnings("rawtypes")
	public DeliveryRequestCancellation(Tuple tuple) {
		this.requestId = (int) tuple.getAttributes()[1];
		this.occurrenceTime = (long) tuple.getAttributes()[0];
	}

	@Override
	public List<String> getSchema() {
		ArrayList<String> result = new ArrayList<String>();
		result.add("StartTimestamp");
		result.add("Integer");
		result.add("StartTimestamp");
		result.add("EndTimestamp");
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void processTuple(Tuple tuple) {
		DeliveryRequestCancellation cancel = new DeliveryRequestCancellation(tuple);		
		LiveEventRegistry.getInstance().register(cancel);
	}
	
}
