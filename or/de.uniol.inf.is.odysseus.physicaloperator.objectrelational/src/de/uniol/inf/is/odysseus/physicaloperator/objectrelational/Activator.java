package de.uniol.inf.is.odysseus.physicaloperator.objectrelational;

import junit.textui.TestRunner;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.test.AllTests;

/**
 * @author Jendrik Poloczek
 */
public class Activator implements BundleActivator {

	static BundleContext context;

	public void start(BundleContext c) throws Exception {	    
	    TestRunner.run(new AllTests());
		context = c;
	}

	public void stop(BundleContext context) throws Exception {}
	
	static BundleContext getContext(){
		return context;
	}

}
