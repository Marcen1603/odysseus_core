package de.uniol.inf.is.odysseus.rest2.server;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.wso2.msf4j.MicroservicesRunner;

public class Application implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		new MicroservicesRunner().deploy(new StockQuoteService()).deployWebSocketEndpoint(new ChatAppEndpoint())
				.start();
	}

	@Override
	public void stop(BundleContext context) throws Exception {

	}

}
