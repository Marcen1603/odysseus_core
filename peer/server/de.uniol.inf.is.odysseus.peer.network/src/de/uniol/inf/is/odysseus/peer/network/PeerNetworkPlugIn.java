package de.uniol.inf.is.odysseus.peer.network;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class PeerNetworkPlugIn implements BundleActivator {

	private static Bundle bundle;
	
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		bundle = bundleContext.getBundle();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		bundle = null;
	}

	public static Bundle getBundle() {
		return bundle;
	}

}
