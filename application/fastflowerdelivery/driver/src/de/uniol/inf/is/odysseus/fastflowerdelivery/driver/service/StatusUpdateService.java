package de.uniol.inf.is.odysseus.fastflowerdelivery.driver.service;

import java.util.List;

import de.uniol.inf.is.odysseus.fastflowerdelivery.driver.dao.LiveEventRegistry;
import de.uniol.inf.is.odysseus.fastflowerdelivery.event.AbstractEvent;
import de.uniol.inf.is.odysseus.fastflowerdelivery.json.JsonData;
import de.uniol.inf.is.odysseus.fastflowerdelivery.json.JsonEvents;
import de.uniol.inf.is.odysseus.fastflowerdelivery.service.AbstractWebService;
import de.uniol.inf.is.odysseus.fastflowerdelivery.service.ServiceRequest;

/**
 * A webservice that is used to fetch incoming events in (nearly) real time.
 *
 * @author Weert Stamm
 * @version 1.0
 */
public class StatusUpdateService extends AbstractWebService {

	private static final long serialVersionUID = -9056038959213119274L;

	public StatusUpdateService() {
		setWebPath("/update");
	}
	
	@Override
	protected JsonData serve(ServiceRequest request) {
		// Get the driver reference
		String driverReference = request.getStringParameter("driver");
		// Get all recently incoming events that match the driver reference
		List<AbstractEvent> liveEvents = LiveEventRegistry.getInstance().getEventsByDriver(driverReference);
		return new JsonEvents(liveEvents);
	}

}
