package de.uniol.inf.is.odysseus.action.services;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private ClassLoader contextClassLoader;

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		//save classloader because dynamic client factory overrides it!
		this.contextClassLoader = Thread.currentThread().getContextClassLoader();
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		if (this.contextClassLoader != null){
			Thread.currentThread().setContextClassLoader(this.contextClassLoader);
		}
	}

}
