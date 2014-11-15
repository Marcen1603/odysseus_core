package de.uniol.inf.is.odysseus.rest.provider;

import org.restlet.routing.Router;

public interface IRestProvider {

	String getPath();
	
	void attachServerResources(Router router);
}
