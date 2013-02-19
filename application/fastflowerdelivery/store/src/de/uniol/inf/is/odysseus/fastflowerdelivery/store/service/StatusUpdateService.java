package de.uniol.inf.is.odysseus.fastflowerdelivery.store.service;

import java.util.List;

import de.uniol.inf.is.odysseus.fastflowerdelivery.event.AbstractEvent;
import de.uniol.inf.is.odysseus.fastflowerdelivery.service.AbstractWebService;
import de.uniol.inf.is.odysseus.fastflowerdelivery.service.ServiceRequest;
import de.uniol.inf.is.odysseus.fastflowerdelivery.store.dao.LiveEventRegistry;
import de.uniol.inf.is.odysseus.fastflowerdelivery.json.JsonData;
import de.uniol.inf.is.odysseus.fastflowerdelivery.json.JsonEvents;

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
		// Fetch the store reference
		String storeReference = request.getStringParameter("store");
		// Get all recently incoming events that match the store reference
		List<AbstractEvent> liveEvents = LiveEventRegistry.getInstance().getEventsByStore(storeReference);
		return new JsonEvents(liveEvents);
	}

}
