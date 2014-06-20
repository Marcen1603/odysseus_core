package de.uniol.inf.is.odysseus.sports.rest;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import de.uniol.inf.is.odysseus.rest.provider.IRestProvider;
import de.uniol.inf.is.odysseus.sports.rest.resources.GetExampleResource;
import de.uniol.inf.is.odysseus.sports.rest.resources.PostExampleResource;

public class SportsRestProvider extends Application implements IRestProvider{

	@Override
	public String getPath() {
		return "/sports";
	}

	@Override
	public Restlet createInboundRoot() {
		Router router = new Router(getContext());
		router.attach("/getexample/{name}", GetExampleResource.class);
		router.attach("/postexample", PostExampleResource.class);
		return router;
	}
}
