package de.uniol.inf.is.odysseus.net.discovery.broadcast;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class BroadcastDiscoveryPlugIn implements BundleActivator {

	public static final int BROADCAST_PORT = 60100;
	public static final String BROADCAST_REQUEST_CONTENT = "BROADCAST_REQUEST";
	
	@Override
	public void start(BundleContext bundleContext) throws Exception {
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
	}

}
