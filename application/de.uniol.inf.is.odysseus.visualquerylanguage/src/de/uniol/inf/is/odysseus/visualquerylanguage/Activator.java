package de.uniol.inf.is.odysseus.visualquerylanguage;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.viewer.swt.resource.SWTResourceManager;




/**
 * The activator class controls the plug-in life cycle
 */
public class Activator implements BundleActivator {
	
	static BundleContext context;

	public void start(final BundleContext bc) throws Exception {
		context = bc;
		SWTResourceManager.resourceBundle = bc.getBundle();
	}
	
	public void stop(BundleContext arg0) throws Exception {
		context = null;
		SWTResourceManager.resourceBundle = null;
	}

	public static BundleContext getContext() {
		return context;
	}
	
}
