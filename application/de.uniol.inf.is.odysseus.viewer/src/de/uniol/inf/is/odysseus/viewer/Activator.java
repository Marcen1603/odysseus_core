package de.uniol.inf.is.odysseus.viewer;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;


public class Activator implements BundleActivator {

	static BundleContext context;
	
	@Override
	public void start(BundleContext bc) throws Exception {
		context = bc;
		
	}

	@Override
	public void stop(BundleContext arg0) throws Exception {
		context = null;
	}
	
	public static BundleContext getContext() {
		return context;
	}

}
