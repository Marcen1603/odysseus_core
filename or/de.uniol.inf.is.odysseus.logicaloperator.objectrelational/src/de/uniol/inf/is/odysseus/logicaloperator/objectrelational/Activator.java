package de.uniol.inf.is.odysseus.logicaloperator.objectrelational;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * @author Jendrik Poloczek
 */
public class Activator implements BundleActivator {

	static BundleContext context;
	
	public void start(BundleContext c) throws Exception {
		context = c;
	}

	public void stop(BundleContext context) throws Exception {
	}
	
	static BundleContext getContext(){
		return context;
	}
}

