package de.uniol.inf.is.odysseus.rest2.server;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.wso2.msf4j.MicroservicesRunner;

import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;

public class Application implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		int port = OdysseusConfiguration.instance.getInt("rest2.port", 8888);
		new MicroservicesRunner(port).addGlobalRequestInterceptor(new SecurityAuthInterceptor())
				.deploy(new StockQuoteService()).deployWebSocketEndpoint(new ChatAppEndpoint()).start();
	}

	@Override
	public void stop(BundleContext context) throws Exception {

	}

}
