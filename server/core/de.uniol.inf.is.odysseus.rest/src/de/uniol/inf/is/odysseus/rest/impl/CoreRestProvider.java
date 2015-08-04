package de.uniol.inf.is.odysseus.rest.impl;

import org.restlet.routing.Router;

import de.uniol.inf.is.odysseus.rest.provider.IRestProvider;
import de.uniol.inf.is.odysseus.rest.serverresources.AddQueryServerResource;
import de.uniol.inf.is.odysseus.rest.serverresources.CreateMultipleSocketServerResource;
import de.uniol.inf.is.odysseus.rest.serverresources.CreateSocketServerResource;
import de.uniol.inf.is.odysseus.rest.serverresources.LoginServerResource;
import de.uniol.inf.is.odysseus.rest.serverresources.RemoveQueryServerResource;
import de.uniol.inf.is.odysseus.rest.serverresources.StartQueryServerResource;
import de.uniol.inf.is.odysseus.rest.serverresources.StopQueryServerResource;

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
		router.attach("/"+CreateMultipleSocketServerResource.PATH, CreateMultipleSocketServerResource.class);
		router.attach("/"+StartQueryServerResource.PATH, StartQueryServerResource.class);
		router.attach("/"+StopQueryServerResource.PATH, StopQueryServerResource.class);
		router.attach("/"+RemoveQueryServerResource.PATH, RemoveQueryServerResource.class);

	}

}
