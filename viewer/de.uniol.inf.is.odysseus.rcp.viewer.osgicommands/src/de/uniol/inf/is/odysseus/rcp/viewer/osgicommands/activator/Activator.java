package de.uniol.inf.is.odysseus.rcp.viewer.osgicommands.activator;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;

public class Activator implements BundleActivator {

	private static IAdvancedExecutor executor = null;
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(final BundleContext context) throws Exception {
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
	}

	public static IAdvancedExecutor getExecutor() {
		return executor;
	}
	
	public void bindExecutor( IAdvancedExecutor ex ) {
		executor = ex;
	}
	
	public void unbindExecutor( IAdvancedExecutor ex ) {
		executor = null;
	}
}
