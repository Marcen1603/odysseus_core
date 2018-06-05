package de.uniol.inf.is.odysseus.fastflowerdelivery.store.service;

import de.uniol.inf.is.odysseus.fastflowerdelivery.io.EventReceiverRegistry;
import de.uniol.inf.is.odysseus.fastflowerdelivery.io.MysqlConnection;
import de.uniol.inf.is.odysseus.fastflowerdelivery.json.JsonData;
import de.uniol.inf.is.odysseus.fastflowerdelivery.json.JsonError;
import de.uniol.inf.is.odysseus.fastflowerdelivery.json.JsonSuccess;
import de.uniol.inf.is.odysseus.fastflowerdelivery.service.AbstractWebService;
import de.uniol.inf.is.odysseus.fastflowerdelivery.service.ServiceRequest;

public class LoginService extends AbstractWebService {

	private static final long serialVersionUID = -5844104208239794319L;

	private boolean create;
	
	public LoginService(boolean createUserIfNotExists) {
		this.create = createUserIfNotExists;
		setWebPath("/login");
	}

	@Override
	protected JsonData serve(ServiceRequest request) {
		String username = request.getStringParameter("username");
		boolean exists = MysqlConnection.getInstance().existence("SELECT StoreReference FROM store WHERE StoreReference='"+username+"';");
		// Create the user if it does not exist
		if(!exists && create)
			MysqlConnection.getInstance().create("INSERT INTO store (StoreReference, MinimumRanking) VALUES ('"+username+"','3');");
		else if(!exists)
			return new JsonError("The user '"+username+"' does not exist.");
		
		EventReceiverRegistry.getInstance().startAll();
		
		return new JsonSuccess();
	}
}
