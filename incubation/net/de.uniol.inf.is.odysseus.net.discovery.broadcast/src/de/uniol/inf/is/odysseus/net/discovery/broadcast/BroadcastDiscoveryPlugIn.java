package de.uniol.inf.is.odysseus.net.discovery.broadcast;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class BroadcastDiscoveryPlugIn implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		BroadcastDiscoveryPlugIn.context = bundleContext;
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		BroadcastDiscoveryPlugIn.context = null;
	}

}
