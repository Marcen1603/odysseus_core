package de.uniol.inf.is.odysseus.net.config;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class OdysseusNetConfigPlugIn implements BundleActivator {

	@Override
	public void start(BundleContext bundleContext) throws Exception {
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		OdysseusNetConfiguration.save();
	}

}
