package de.uniol.inf.is.odysseus.peer.config;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class PeerConfigurationActivator implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		PeerConfiguration.save();
	}

}
