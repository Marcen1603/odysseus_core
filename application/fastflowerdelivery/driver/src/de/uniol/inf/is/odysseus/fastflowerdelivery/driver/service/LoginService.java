package de.uniol.inf.is.odysseus.fastflowerdelivery.driver.service;

import de.uniol.inf.is.odysseus.fastflowerdelivery.io.EventReceiverRegistry;
import de.uniol.inf.is.odysseus.fastflowerdelivery.io.MysqlConnection;
import de.uniol.inf.is.odysseus.fastflowerdelivery.json.JsonData;
import de.uniol.inf.is.odysseus.fastflowerdelivery.json.JsonError;
import de.uniol.inf.is.odysseus.fastflowerdelivery.json.JsonSuccess;
import de.uniol.inf.is.odysseus.fastflowerdelivery.service.AbstractWebService;
import de.uniol.inf.is.odysseus.fastflowerdelivery.service.ServiceRequest;

/**
 * A webservice to manage the login for users.
 * Also starts the event receivers if the first user logs into the system.
 *
 * @author Weert Stamm
 * @version 1.0
 */
public class LoginService extends AbstractWebService {

	private static final long serialVersionUID = -5372974316357383804L;

	private boolean create;
	
	public LoginService(boolean createUserIfNotExists) {
		this.create = createUserIfNotExists;
		setWebPath("/login");
	}

	@Override
	protected JsonData serve(ServiceRequest request) {
		String username = request.getStringParameter("username");
		boolean exists = MysqlConnection.getInstance().existence("SELECT DriverReference FROM driver WHERE DriverReference='"+username+"';");
		
		if(!exists && create) // Create the user if it does not exist
			MysqlConnection.getInstance().create("INSERT INTO driver (DriverReference, Location, Ranking) VALUES ('"+username+"','Wechloy','7');");
		else if(!exists) // Dont let them login
			return new JsonError("The user '"+username+"' does not exist.");
		
		// Starts all event receivers (is executed only once)
		EventReceiverRegistry.getInstance().startAll();
		
		return new JsonSuccess();
	}

}
