package de.uniol.inf.is.odysseus.test.operator;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class OperatorTestPlugin implements BundleActivator {

	public static BundleContext context;
	
	public void start(BundleContext ctx) throws Exception {
		context = ctx;
	}
	
	public void stop(BundleContext context) throws Exception {
		context = null;
	}

}
