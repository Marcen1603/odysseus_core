package de.uniol.inf.is.odysseus.webservice.client;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	@Override
	public void start(BundleContext bundleContext)  {
		Activator.context = bundleContext;
	}

	@Override
	public void stop(BundleContext bundleContext)  {
		Activator.context = null;
	}

}
