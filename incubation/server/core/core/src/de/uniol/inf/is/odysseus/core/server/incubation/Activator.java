package de.uniol.inf.is.odysseus.core.server.incubation;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
/**
 * Activator of the Bundle
 * @author Dennis Nowak
 *
 */
public class Activator implements BundleActivator {

	private static BundleContext context;

	/**
	 * Returns the context of the bundle
	 * @return the context of the bundle
	 */
	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
