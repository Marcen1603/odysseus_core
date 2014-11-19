package de.uniol.inf.is.odysseus.peer.rest;

import org.restlet.routing.Router;

import de.uniol.inf.is.odysseus.peer.rest.serverresources.PingMapServerResource;
import de.uniol.inf.is.odysseus.rest.provider.IRestProvider;

public class PeerRestProvider implements IRestProvider {

	@Override
	public String getPath() {
		return "/peer";
	}

	@Override
	public void attachServerResources(Router router) {
		router.attach("/"+PingMapServerResource.PATH, PingMapServerResource.class);
	}

}
