package de.uniol.inf.is.odysseus.physicaloperator.objectrelational;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * @author Jendrik Poloczek
 */
public class Activator implements BundleActivator {

	static BundleContext context;

	public void start(BundleContext c) throws Exception {	    
	    //TestRunner.run(new AllTests());
		context = c;
	}

	public void stop(BundleContext context) throws Exception {}
	
	static BundleContext getContext(){
		return context;
	}

}
