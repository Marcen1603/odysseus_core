package de.uniol.inf.is.odysseus.generator.smarthome;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;


/**
 * The activator class controls the plug-in life cycle
 */
public class Activator implements BundleActivator {

	// The plug-in ID
	public static final String PLUGIN_ID = "de.uniol.inf.is.odysseus.generator.smarthome"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	private static BundleContext currentcontext;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
    public void start(BundleContext context) throws Exception {		
		plugin = this;
		currentcontext = context;		
		String[] identifiers = {"M", "L", "D", "I", "P", "T", "E"};
		DataReader dr = new DataReader(50000, 0, identifiers);
		dr.start();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
    public void stop(BundleContext context) throws Exception {
		plugin = null;		
		currentcontext = context;
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	public static BundleContext getContext() {
		return currentcontext;
	}

}
