package de.uniol.inf.is.odysseus.visualquerylanguage;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
/**
 * The activator class controls the plug-in life cycle
 */
public class Activator implements BundleActivator {
	
	static BundleContext context;

	public void start(BundleContext bc) throws Exception {
		context = bc;	
	}
	
	public void stop(BundleContext arg0) throws Exception {
		context = null;
	}

	public static BundleContext getContext() {
		return context;
	}
	
}
