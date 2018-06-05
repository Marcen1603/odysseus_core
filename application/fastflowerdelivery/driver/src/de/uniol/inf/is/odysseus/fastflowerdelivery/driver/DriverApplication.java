package de.uniol.inf.is.odysseus.fastflowerdelivery.driver;

import de.uniol.inf.is.odysseus.fastflowerdelivery.EventWebApplication;
import de.uniol.inf.is.odysseus.fastflowerdelivery.io.EventReceiver;
import de.uniol.inf.is.odysseus.fastflowerdelivery.io.EventSender;
import de.uniol.inf.is.odysseus.fastflowerdelivery.driver.event.Assignment;
import de.uniol.inf.is.odysseus.fastflowerdelivery.driver.event.BidRequest;
import de.uniol.inf.is.odysseus.fastflowerdelivery.driver.event.ConsistentStrongDriverReport;
import de.uniol.inf.is.odysseus.fastflowerdelivery.driver.event.ConsistentWeakDriverReport;
import de.uniol.inf.is.odysseus.fastflowerdelivery.driver.event.DeliveryBid;
import de.uniol.inf.is.odysseus.fastflowerdelivery.driver.event.DeliveryConfirmation;
import de.uniol.inf.is.odysseus.fastflowerdelivery.driver.event.DeliveryRequestCancellation;
import de.uniol.inf.is.odysseus.fastflowerdelivery.driver.event.IdleDriverReport;
import de.uniol.inf.is.odysseus.fastflowerdelivery.driver.event.ImprovingDriverReport;
import de.uniol.inf.is.odysseus.fastflowerdelivery.driver.event.ImprovingNote;
import de.uniol.inf.is.odysseus.fastflowerdelivery.driver.event.PermanentWeakDriverReport;
import de.uniol.inf.is.odysseus.fastflowerdelivery.driver.event.PickupConfirmation;
import de.uniol.inf.is.odysseus.fastflowerdelivery.driver.service.BidService;
import de.uniol.inf.is.odysseus.fastflowerdelivery.driver.service.DeliveryConfirmationService;
import de.uniol.inf.is.odysseus.fastflowerdelivery.driver.service.InitialDataService;
import de.uniol.inf.is.odysseus.fastflowerdelivery.driver.service.LoginService;
import de.uniol.inf.is.odysseus.fastflowerdelivery.driver.service.StatusUpdateService;

/**
 * Main class to start up the webapplication for the drivers.
 *
 * @author Weert Stamm
 * @version 1.0
 */
public class DriverApplication extends EventWebApplication {

	public static void main(String[] args) {
		
		// Load config file
		DriverConfiguration config = new DriverConfiguration().load("config.json");
		
		// Start the application
		new DriverApplication(config).start(config.getWebsitePort());
	}
	
	/**
	 * Constructor to initialize the application
	 * @param config
	 * 			the driver configuration object
	 */
	public DriverApplication(DriverConfiguration config) {

		// Register event senders
		register(new EventSender(config.getOutgoingPortDeliveryBid(), new DeliveryBid()));
		register(new EventSender(config.getOutgoingPortDeliveryConfirmation(), new DeliveryConfirmation()));
		
		// Register event receivers
		register(new EventReceiver(config.getOdysseusAddress(), config.getIncomingPortBidRequest(), new BidRequest()));
		register(new EventReceiver(config.getOdysseusAddress(), config.getIncomingPortAssignment(), new Assignment()));
		register(new EventReceiver(config.getOdysseusAddress(), config.getIncomingPortPickupConfirmation(), new PickupConfirmation()));
		register(new EventReceiver(config.getOdysseusAddress(), config.getIncomingPortIdleDriverReport(), new IdleDriverReport()));
		register(new EventReceiver(config.getOdysseusAddress(), config.getIncomingPortImprovingDriverReport(), new ImprovingDriverReport()));
		register(new EventReceiver(config.getOdysseusAddress(), config.getIncomingPortPermanentWeakDriverReport(), new PermanentWeakDriverReport()));
		register(new EventReceiver(config.getOdysseusAddress(), config.getIncomingPortConsistentWeakDriverReport(), new ConsistentWeakDriverReport()));
		register(new EventReceiver(config.getOdysseusAddress(), config.getIncomingPortConsistentStrongDriverReport(), new ConsistentStrongDriverReport()));
		register(new EventReceiver(config.getOdysseusAddress(), config.getIncomingPortImprovingNote(), new ImprovingNote()));
		register(new EventReceiver(config.getOdysseusAddress(), config.getIncomingPortDeliveryRequestCancellation(), new DeliveryRequestCancellation()));
		
		// Register services
		register(new StatusUpdateService());
		register(new BidService());
		register(new DeliveryConfirmationService());
		register(new InitialDataService());
		register(new LoginService(config.isCreateUserOnLogin()));
	}

}
