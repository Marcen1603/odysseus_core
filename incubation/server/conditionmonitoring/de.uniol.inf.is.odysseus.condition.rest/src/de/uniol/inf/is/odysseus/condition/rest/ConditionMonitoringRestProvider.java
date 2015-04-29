package de.uniol.inf.is.odysseus.condition.rest;

import org.restlet.routing.Router;

import de.uniol.inf.is.odysseus.condition.rest.serverresources.ConditionQLServerResource;
import de.uniol.inf.is.odysseus.rest.provider.IRestProvider;

public class ConditionMonitoringRestProvider implements IRestProvider {

	@Override
	public String getPath() {
		return "/condition";
	}

	@Override
	public void attachServerResources(Router router) {
		System.setProperty("java.net.preferIPv4Stack", "true");

		router.attach("/" + ConditionQLServerResource.PATH, ConditionQLServerResource.class);

	}

}
