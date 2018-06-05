package de.uniol.inf.is.odysseus.incubation.crypt;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * This is the Activator of this bundle.
 * 
 * @author MarkMilster
 *
 */
public class Activator implements BundleActivator {

	private static BundleContext context;

	/**
	 * Returns the BundleContext of this bundle.
	 * 
	 * @return The BundleContext of this bundle.
	 */
	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.
	 * BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
