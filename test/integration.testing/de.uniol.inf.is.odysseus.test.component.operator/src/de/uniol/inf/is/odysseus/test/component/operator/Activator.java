package de.uniol.inf.is.odysseus.test.component.operator;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator implements BundleActivator {

	public static BundleContext context;
	
	@Override
	public void start(BundleContext ctx) throws Exception {
		context = ctx;
	}
	
	@Override
	public void stop(BundleContext context) throws Exception {
		context = null;
	}

}
