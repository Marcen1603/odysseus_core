package de.uniol.inf.is.odysseus.dbIntegration;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
	

	private static BundleContext context;
	
	public void start(BundleContext context) throws Exception {
		Activator.setContext(context);
		
	}


	public void stop(BundleContext context) throws Exception {
	}

	public static BundleContext getContext() {
		return context;
	}

	private static void setContext(BundleContext context) {
		Activator.context = context;
		
	}
}