package de.uniol.inf.is.odysseus.rcp.viewer.view;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.rcp.viewer.view.symbol.ISymbolConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.view.symbol.impl.XMLSymbolConfiguration;

public class Activator extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "de.uniol.inf.is.odysseus.rcp.viewer.view";
	public static ISymbolConfiguration SYMBOL_CONFIGURATION = null;
	
	private static Activator plugin;
	private static BundleContext bundleContext;
	
	public Activator() {
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		bundleContext = context;
		
		SYMBOL_CONFIGURATION = new XMLSymbolConfiguration(
				Activator.getContext().getBundle().getEntry("viewer_cfg/symbol.xml"), 
				Activator.getContext().getBundle().getEntry("viewer_cfg/symbolSchema.xsd"));
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
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	public static BundleContext getContext() {
		return bundleContext;
	}
}
