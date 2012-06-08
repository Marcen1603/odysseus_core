package de.uniol.inf.is.odysseus.rcp.viewer.server;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class RCPViewerServerPlugIn extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "de.uniol.inf.is.odysseus.rcp.viewer.server"; 
	public static final String OPERATOR_BREAK_VIEW_ID = "de.uniol.inf.is.odysseus.rcp.views.OperatorBreakView";
	
	private static RCPViewerServerPlugIn plugin;
	
	/**
	 * The constructor
	 */
	public RCPViewerServerPlugIn() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
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
	public static RCPViewerServerPlugIn getDefault() {
		return plugin;
	}

}
