package de.uniol.inf.is.odysseus.test;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * Activator for odysses.test-Plugin
 * 
 * @author Timo Michelsen
 *
 */
public class NexmarkTestPlugIn implements BundleActivator {

	static public BundleContext context;

	@Override
	public void start(BundleContext ctx) throws Exception {
		context = ctx;
	}

	@Override
	public void stop(BundleContext ctx) throws Exception {
		context = null;
	}

	
}
