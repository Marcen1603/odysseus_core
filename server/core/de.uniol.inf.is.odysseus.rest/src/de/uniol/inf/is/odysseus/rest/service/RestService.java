package de.uniol.inf.is.odysseus.rest.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.service.CorsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.rest.provider.IRestProvider;


public class RestService {

	public static Component component = new Component();


	private static final Logger LOGGER = LoggerFactory.getLogger(RestService.class);

	private static int port;

	private Map<IRestProvider, RestApplication> restApplications = Maps.newHashMap();


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void bindRestProvider(IRestProvider provider) {
		RestApplication app =  new RestApplication(provider);
		restApplications.put(provider, app);
		component.getDefaultHost().attach(provider.getPath(), app);

        CorsService corsService = new CorsService();
        corsService.setAllowedOrigins( new HashSet(Arrays.asList("*")));
        corsService.setAllowedCredentials(true);

        app.getServices().add(corsService);


	}

	public void unbindRestProvider(IRestProvider provider) {
		RestApplication app =  restApplications.get(provider);
		if (app != null) {
			component.getDefaultHost().detach(app);
		}
	}

	public static void start() throws Exception {
		int webServicePort = Integer.parseInt(OdysseusConfiguration.instance.get("WebService.Port"))+10;
		int maxPort = Integer.parseInt(OdysseusConfiguration.instance.get("WebService.MaxPort"));
		while (webServicePort <= maxPort ){
			try {
				component.getServers().clear();
				component.getServers().add(Protocol.HTTP, webServicePort);
				component.start();
				port = webServicePort;
				LOGGER.info("Restservice published at " + webServicePort);
				break;
			} catch (Exception e) {

			}
			webServicePort++;
		}
	}

	public static void stop() throws Exception {
		component.stop();
	}


	public static int getPort() {
		return port;
	}


}
