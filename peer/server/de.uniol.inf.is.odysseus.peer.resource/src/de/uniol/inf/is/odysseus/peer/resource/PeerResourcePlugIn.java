package de.uniol.inf.is.odysseus.peer.resource;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class PeerResourcePlugIn implements BundleActivator {

	private static Bundle bundle;
	
	@Override
	public void start(BundleContext context) throws Exception {
		bundle = context.getBundle();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		bundle = null;
	}
	
	public static Bundle getBundle() {
		return bundle;
	}
}
