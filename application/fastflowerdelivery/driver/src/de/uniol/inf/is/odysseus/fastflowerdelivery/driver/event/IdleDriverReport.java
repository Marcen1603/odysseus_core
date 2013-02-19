package de.uniol.inf.is.odysseus.fastflowerdelivery.driver.event;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.fastflowerdelivery.driver.dao.LiveEventRegistry;
import de.uniol.inf.is.odysseus.fastflowerdelivery.event.AbstractEvent;
import de.uniol.inf.is.odysseus.fastflowerdelivery.io.ISinkEventHandler;

/**
 * Represents a certain Driver Report event, the idle driver
 *
 * @author Weert Stamm
 * @version 1.0
 */
public class IdleDriverReport extends AbstractEvent implements ISinkEventHandler {

	private String driverReference;
	
	public IdleDriverReport() {}
	
	@SuppressWarnings("rawtypes")
	public IdleDriverReport(Tuple tuple) {
		this.driverReference = (String) tuple.getAttributes()[0];
		this.occurrenceTime = (long) tuple.getAttributes()[1];
	}

	@Override
	public List<String> getSchema() {
		ArrayList<String> result = new ArrayList<String>();
		result.add("String");
		result.add("StartTimestamp");
		result.add("EndTimestamp");
		return result;
	}

	@Override
	public void processTuple(@SuppressWarnings("rawtypes") Tuple tuple) {
		IdleDriverReport report = new IdleDriverReport(tuple);
		LiveEventRegistry.getInstance().register(report);
	}


	public String getDriverReference() {
		return driverReference;
	}

	public void setDriverReference(String driverReference) {
		this.driverReference = driverReference;
	}
}
