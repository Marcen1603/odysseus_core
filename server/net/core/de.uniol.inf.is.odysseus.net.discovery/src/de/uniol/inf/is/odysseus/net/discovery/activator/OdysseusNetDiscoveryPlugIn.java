package de.uniol.inf.is.odysseus.net.discovery.activator;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class OdysseusNetDiscoveryPlugIn implements BundleActivator {

	public static final String DISCOVERER_NAME_CONFIG_KEY = "net.discoverer.name";
	public static final String DEFAULT_DISCOVERER_NAME = "BroadcastOdysseusNodeDiscoverer";

	@Override
	public void start(BundleContext context) throws Exception {
	}

	@Override
	public void stop(BundleContext context) throws Exception {
	}

}
