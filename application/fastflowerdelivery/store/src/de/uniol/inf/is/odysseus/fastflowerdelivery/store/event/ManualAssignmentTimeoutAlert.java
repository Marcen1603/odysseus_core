package de.uniol.inf.is.odysseus.fastflowerdelivery.store.event;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.fastflowerdelivery.event.AbstractEvent;
import de.uniol.inf.is.odysseus.fastflowerdelivery.io.ISinkEventHandler;
import de.uniol.inf.is.odysseus.fastflowerdelivery.store.dao.LiveEventRegistry;

/**
 * Represents a Manual Assignment Timeout Alert event
 *
 * @author Weert Stamm
 * @version 1.0
 */
public class ManualAssignmentTimeoutAlert extends AbstractEvent implements ISinkEventHandler {

	private String storeReference = "";
	
	public ManualAssignmentTimeoutAlert() {}
	
	@SuppressWarnings("rawtypes")
	public ManualAssignmentTimeoutAlert(Tuple tuple) {
		Object[] attributes = tuple.getAttributes();
		this.occurrenceTime = (long) attributes[2];
		this.requestId = (int) attributes[0];
		this.storeReference = (String) attributes[1];
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
		ManualAssignmentTimeoutAlert alert = new ManualAssignmentTimeoutAlert(tuple);
		
		// register alert for website
		LiveEventRegistry.getInstance().register(alert);
	}

	public String getStoreReference() {
		return storeReference;
	}

	public void setStoreReference(String storeReference) {
		this.storeReference = storeReference;
	}

}
