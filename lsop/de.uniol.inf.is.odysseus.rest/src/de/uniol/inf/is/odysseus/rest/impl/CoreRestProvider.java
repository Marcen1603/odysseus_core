package de.uniol.inf.is.odysseus.rest.impl;

import org.restlet.routing.Router;

import de.uniol.inf.is.odysseus.rest.provider.IRestProvider;
import de.uniol.inf.is.odysseus.rest.serverresources.AddQueryServerResource;
import de.uniol.inf.is.odysseus.rest.serverresources.CreateSocketServerResource;
import de.uniol.inf.is.odysseus.rest.serverresources.LoginServerResource;

public class CoreRestProvider implements IRestProvider{

	@Override
	public String getPath() {
		return "/core";
	}

	@Override
	public void attachServerResources(Router router) {
		router.attach("/"+LoginServerResource.PATH, LoginServerResource.class);
		router.attach("/"+AddQueryServerResource.PATH, AddQueryServerResource.class);
		router.attach("/"+CreateSocketServerResource.PATH, CreateSocketServerResource.class);

	}

}
