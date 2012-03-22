package de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.WebserviceServer;
import de.uniol.inf.is.odysseus.slf4j.LoggingConfiguration;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
    public void start(BundleContext bundleContext) throws Exception {
		LoggingConfiguration.load();
		Activator.context = bundleContext;
		WebserviceServer.startServer();
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
    public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
