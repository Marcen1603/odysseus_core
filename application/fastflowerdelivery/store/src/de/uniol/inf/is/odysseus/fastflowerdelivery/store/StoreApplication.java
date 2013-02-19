package de.uniol.inf.is.odysseus.fastflowerdelivery.store;

import de.uniol.inf.is.odysseus.fastflowerdelivery.EventWebApplication;
import de.uniol.inf.is.odysseus.fastflowerdelivery.store.event.DeliveryAlert;
import de.uniol.inf.is.odysseus.fastflowerdelivery.store.event.DeliveryBid;
import de.uniol.inf.is.odysseus.fastflowerdelivery.store.event.DeliveryConfirmation;
import de.uniol.inf.is.odysseus.fastflowerdelivery.store.event.DeliveryRequest;
import de.uniol.inf.is.odysseus.fastflowerdelivery.store.event.DeliveryRequestCancellation;
import de.uniol.inf.is.odysseus.fastflowerdelivery.store.event.ManualAssignment;
import de.uniol.inf.is.odysseus.fastflowerdelivery.store.event.ManualAssignmentTimeoutAlert;
import de.uniol.inf.is.odysseus.fastflowerdelivery.store.event.PickupAlert;
import de.uniol.inf.is.odysseus.fastflowerdelivery.store.event.PickupConfirmation;
import de.uniol.inf.is.odysseus.fastflowerdelivery.store.service.CancellationService;
import de.uniol.inf.is.odysseus.fastflowerdelivery.store.service.DeliveryRequestService;
import de.uniol.inf.is.odysseus.fastflowerdelivery.store.service.InitialDataService;
import de.uniol.inf.is.odysseus.fastflowerdelivery.store.service.LoginService;
import de.uniol.inf.is.odysseus.fastflowerdelivery.store.service.ManualAssignmentService;
import de.uniol.inf.is.odysseus.fastflowerdelivery.store.service.PickupConfirmationService;
import de.uniol.inf.is.odysseus.fastflowerdelivery.store.service.StatusUpdateService;
import de.uniol.inf.is.odysseus.fastflowerdelivery.io.EventReceiver;
import de.uniol.inf.is.odysseus.fastflowerdelivery.io.EventSender;

/**
 * Main class to start up the webapplication for the stores.
 *
 * @author Weert Stamm
 * @version 1.0
 */
public class StoreApplication extends EventWebApplication {

	public static void main(String[] args) {
		
		// Load config file
		StoreConfiguration config = new StoreConfiguration().load("config.json");

		// Start the application		
		new StoreApplication(config).start(config.getWebsitePort());
	}
	
	/**
	 * Constructor to initialize the application
	 * @param config
	 * 			the store configuration object
	 */
	public StoreApplication(StoreConfiguration config) {		
		// Register event senders
		register(new EventSender(config.getOutgoingPortDeliveryRequest(), new DeliveryRequest()));
		register(new EventSender(config.getOutgoingPortManualAssignment(), new ManualAssignment()));
		register(new EventSender(config.getOutgoingPortPickupConfirmation(), new PickupConfirmation()));
		register(new EventSender(config.getOutgoingPortDeliveryRequestCancellation(), new DeliveryRequestCancellation()));
		
		// Register event receivers
		register(new EventReceiver(config.getOdysseusAddress(), config.getIncomingPortDeliveryBid(), new DeliveryBid()));
		register(new EventReceiver(config.getOdysseusAddress(), config.getIncomingPortDeliveryConfirmation(), new DeliveryConfirmation()));
		register(new EventReceiver(config.getOdysseusAddress(), config.getIncomingPortManualAssignmentTimeoutAlert(), new ManualAssignmentTimeoutAlert()));
		register(new EventReceiver(config.getOdysseusAddress(), config.getIncomingPortPickupAlert(), new PickupAlert()));
		register(new EventReceiver(config.getOdysseusAddress(), config.getIncomingPortDeliveryAlert(), new DeliveryAlert()));
		
		// Register services
		register(new DeliveryRequestService());
		register(new InitialDataService());
		register(new StatusUpdateService());
		register(new ManualAssignmentService());
		register(new PickupConfirmationService());
		register(new CancellationService());
		register(new LoginService(config.isCreateUserOnLogin()));
	}
}
