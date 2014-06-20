package de.uniol.inf.is.odysseus.rest;

import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.rest.provider.IRestProvider;

public class RestProviderServiceBinding {
	
	public static Component component = new Component();
	
	private static final int PORT = 8182;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RestProviderServiceBinding.class);
	

	public void bindRestProvider(IRestProvider provider) {
		component.getDefaultHost().attach(provider.getPath(), (Restlet) provider);
	}
	
	public void unbindRestProvider(IRestProvider provider) {
		component.getDefaultHost().detach((Restlet) provider);
	}
	
	public static void start() throws Exception {
		component.getServers().add(Protocol.HTTP, PORT);
		component.start();
		LOGGER.info("Restservice published at " + PORT);
	}
	
	public static void stop() throws Exception {
		component.stop();
	}

}
