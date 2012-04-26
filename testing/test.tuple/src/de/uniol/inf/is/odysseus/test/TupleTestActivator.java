package de.uniol.inf.is.odysseus.test;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * Activator 
 * 
 * @author Kai Pancratz
 *
 */
public class TupleTestActivator implements BundleActivator {

	public static BundleContext context;

	@Override
	public void start(BundleContext ctx) throws Exception {
		context = ctx;
	}

	@Override
	public void stop(BundleContext ctx) throws Exception {
		context = null;
	}

	
}
