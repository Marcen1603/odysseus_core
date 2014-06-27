package de.uniol.inf.is.odysseus.sports.rest;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import serverresources.GetExampleResource;
import serverresources.PostExampleResource;
import serverresources.UserDAOServerResource;
import de.uniol.inf.is.odysseus.rest.provider.IRestProvider;

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
		
		
		//Example route by Thore
		router.attach("/getexample/{name}", GetExampleResource.class);
		
		//Example route by Thore
		router.attach("/postexample", PostExampleResource.class);
		
		//Example route by Thomas
		router.attach("/user", UserDAOServerResource.class);
	
		return router;
	}
}
