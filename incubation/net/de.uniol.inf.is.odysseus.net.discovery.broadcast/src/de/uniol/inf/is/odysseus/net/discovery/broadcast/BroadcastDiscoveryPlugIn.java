package de.uniol.inf.is.odysseus.net.discovery.broadcast;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class BroadcastDiscoveryPlugIn implements BundleActivator {

	public static final int BROADCAST_REQUEST_PORT = 65212;
	public static final int BROADCAST_ANSWER_PORT = 65213;
	
	@Override
	public void start(BundleContext bundleContext) throws Exception {
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
	}

}
