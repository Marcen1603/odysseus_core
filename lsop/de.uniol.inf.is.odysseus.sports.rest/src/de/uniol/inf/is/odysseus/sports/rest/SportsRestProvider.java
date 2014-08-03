package de.uniol.inf.is.odysseus.sports.rest;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import de.uniol.inf.is.odysseus.rest.provider.IRestProvider;
import de.uniol.inf.is.odysseus.sports.rest.serverresources.LoginServerResource;
import de.uniol.inf.is.odysseus.sports.rest.serverresources.QueryServerResource;
import de.uniol.inf.is.odysseus.sports.rest.serverresources.QueryWithLoginServerResource;
import de.uniol.inf.is.odysseus.sports.rest.serverresources.SocketInfoServerResource;

/**
 * This class gets instantiated only one time so here are the API´s routes defined 
 * @author Thomas, Thore
 *
 */
public class SportsRestProvider extends Application implements IRestProvider{

	@Override
	public String getPath() {
		return "/sports";
	}

	@Override
	public Restlet createInboundRoot() {
		
		System.setProperty("java.net.preferIPv4Stack", "true");
		
		//Router must be defined in order to handle incoming requests
		//In this case only one router, perhaps we install another router for authentication in the future
		Router router = new Router(getContext());
		

		router.attach("/query", QueryServerResource.class);

		router.attach("/socket_info", SocketInfoServerResource.class);

		router.attach("/login", LoginServerResource.class);

		router.attach("/querywithlogin", QueryWithLoginServerResource.class);

		return router;
	}
}
