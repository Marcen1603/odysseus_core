package de.uniol.inf.is.odysseus.rcp.dashboard;

import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class DashboardPlugIn extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "de.uniol.inf.is.odysseus.rcp.dashboard";
	public static final String EXTENSION_POINT_ID = "de.uniol.inf.is.odysseus.rcp.DashboardPart";
	
	private static DashboardPlugIn plugin;
	
	/**
	 * The constructor
	 */
	public DashboardPlugIn() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
		DashboardPartExtensionPointResolver.execute(Platform.getExtensionRegistry());
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
		
		DashboardPartRegistry.unregisterAll();
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static DashboardPlugIn getDefault() {
		return plugin;
	}
	
}
