package de.uniol.inf.is.odysseus.core.streamconnection;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class RCPStreamPlugIn implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		RCPStreamPlugIn.context = bundleContext;
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		RCPStreamPlugIn.context = null;
	}

}
