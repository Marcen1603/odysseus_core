package de.uniol.inf.is.odysseus.rcp.tenants.view;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWTError;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "de.uniol.inf.is.odysseus.rcp.tenants.view"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				try{
				Bundle bundle = Activator.getDefault().getBundle();
				ImageRegistry imageRegistry = Activator.getDefault().getImageRegistry();
				imageRegistry.put("repository", ImageDescriptor.createFromURL(bundle.getEntry("Icons/repository_rep.gif")));
				imageRegistry.put("user", ImageDescriptor.createFromURL(bundle.getEntry("Icons/user.png")));
				imageRegistry.put("loggedinuser", ImageDescriptor.createFromURL(bundle.getEntry("Icons/user--plus.png")));
				imageRegistry.put("users", ImageDescriptor.createFromURL(bundle.getEntry("Icons/users.png")));
				imageRegistry.put("sla", ImageDescriptor.createFromURL(bundle.getEntry("Icons/document-block.png")));
				imageRegistry.put("percentile", ImageDescriptor.createFromURL(bundle.getEntry("Icons/document-tag.png")));
				imageRegistry.put("tenant", ImageDescriptor.createFromURL(bundle.getEntry("Icons/user-business.png")));
				
				}catch(SWTError e){
					// Ignore :-)
				}
			}
			
		});
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
}
