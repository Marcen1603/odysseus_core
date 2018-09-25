package de.uniol.inf.is.odysseus.rest.service;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import de.uniol.inf.is.odysseus.rest.provider.IRestProvider;


public class RestApplication extends Application {
	private IRestProvider provider;
	
	public RestApplication (IRestProvider provider) {
		this.provider = provider;
	}
	
	
	@Override
	public Restlet createInboundRoot() {
		Router router = new Router(getContext());
		provider.attachServerResources(router);
		return router;
	}

}
