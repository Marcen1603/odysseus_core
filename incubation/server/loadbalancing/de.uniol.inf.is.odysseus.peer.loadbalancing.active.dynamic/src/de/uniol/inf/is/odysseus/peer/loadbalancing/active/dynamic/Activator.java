package de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Activator implements BundleActivator {

	private static BundleContext context;
	/**
	 * Logger
	 */
	private static final Logger LOG = LoggerFactory.getLogger(Activator.class);

	
	
	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		LOG.info("Dynamic LoadBalancing Bundle loaded.");
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
		LOG.info("Dynamic LoadBalancing Bundle unloaded.");
	}

}
