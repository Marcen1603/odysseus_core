package de.uniol.inf.is.odysseus.gpu;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		
		Activator.context = bundleContext;		
		
		System.out.println("GPU-Bundle loaded");
				
		String path = bundleContext.getBundle().getLocation().substring(16,bundleContext.getBundle().getLocation().length());	
		
		prepare.setPath(path);
		
		System.out.println(path);
		
		
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
