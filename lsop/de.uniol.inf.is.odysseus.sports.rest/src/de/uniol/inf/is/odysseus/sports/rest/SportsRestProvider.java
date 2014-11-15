package de.uniol.inf.is.odysseus.sports.rest;

import org.restlet.routing.Router;

import de.uniol.inf.is.odysseus.rest.provider.IRestProvider;
import de.uniol.inf.is.odysseus.sports.rest.serverresources.DistributedSportsQLSocketServerResource;
import de.uniol.inf.is.odysseus.sports.rest.serverresources.ExecuteSportsQLServerResource;
import de.uniol.inf.is.odysseus.sports.rest.serverresources.MetadataServerResource;

/**
 * This class gets instantiated only one time so here are the API´s routes defined 
 * @author Thomas, Thore
 *
 */
public class SportsRestProvider implements IRestProvider{

	@Override
	public String getPath() {
		return "/sports";
	}

	@Override
	public void attachServerResources(Router router) {
		System.setProperty("java.net.preferIPv4Stack", "true");

		router.attach("/"+ExecuteSportsQLServerResource.PATH, ExecuteSportsQLServerResource.class);

		router.attach("/"+DistributedSportsQLSocketServerResource.PATH, DistributedSportsQLSocketServerResource.class);
		
		router.attach("/"+MetadataServerResource.PATH, MetadataServerResource.class);
		
	}
}
