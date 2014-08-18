package de.uniol.inf.is.odysseus.rest;

import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.rest.provider.IRestProvider;


/**
 * 
 * Service binding for @link IRestProvider.  
 * 
 * For this service binding, each implementation of @link IRestProvider must extend from @link org.restlet.Restlet.
 * 
 * @author Thore Stratmann
 *
 */
public class RestProviderServiceBinding {
	
	public static Component component = new Component();
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RestProviderServiceBinding.class);
	

	public void bindRestProvider(IRestProvider provider) {
		component.getDefaultHost().attach(provider.getPath(), (Restlet) provider);
	}
	
	public void unbindRestProvider(IRestProvider provider) {
		component.getDefaultHost().detach((Restlet) provider);
	}
	
	public static void start() throws Exception {
		int port = Integer.parseInt(OdysseusConfiguration.get("WebService.Port"))+10;
		int maxPort = Integer.parseInt(OdysseusConfiguration.get("WebService.MaxPort"));
		while (port <= maxPort ){
			try {
				component.getServers().clear();
				component.getServers().add(Protocol.HTTP, port);
				component.start();
				LOGGER.info("Restservice published at " + port);
				break;
			} catch (Exception e) {

			}
			port++;
		}
	}
	
	public static void stop() throws Exception {
		component.stop();
	}
	
	

}
