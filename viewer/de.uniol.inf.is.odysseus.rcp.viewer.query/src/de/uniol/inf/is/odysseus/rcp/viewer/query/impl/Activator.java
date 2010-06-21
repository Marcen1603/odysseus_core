package de.uniol.inf.is.odysseus.rcp.viewer.query.impl;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends Plugin {

	public static final String PLUGIN_ID = "de.uniol.inf.is.odysseus.rcp.viewer.query";
	private static Activator plugin;
	private static IAdvancedExecutor executor = null;

	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		Thread t = new Thread( new Runnable() {

			@Override
			public void run() {
				ServiceTracker execTracker = new ServiceTracker(context, IAdvancedExecutor.class.getName(), null);
				execTracker.open();
				IAdvancedExecutor e;
				try {
					e = (IAdvancedExecutor) execTracker.waitForService(0);
					if (e != null) {
						executor = e;
					} else {
					}
					execTracker.close();
				} catch (InterruptedException ex) {} 			
			}
			
		});
		
		t.start();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}
	
	public static IAdvancedExecutor getExecutor() {
		return executor;
	}

}
