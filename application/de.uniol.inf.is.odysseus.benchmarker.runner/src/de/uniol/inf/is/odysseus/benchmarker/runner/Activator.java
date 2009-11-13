package de.uniol.inf.is.odysseus.benchmarker.runner;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "de.uniol.inf.is.odysseus.benchmarker.runner";

	// The shared instance
	private static Activator plugin;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		Thread t = new Thread(new Runnable() {
			public void run() {
				for (Bundle bundle : context.getBundles()) {
					boolean isFragment = bundle.getHeaders().get(
							Constants.FRAGMENT_HOST) != null;
					if (bundle != context.getBundle() && !isFragment
							&& bundle.getState() == Bundle.RESOLVED) {
						try {
							bundle.start();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
		t.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
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

}
