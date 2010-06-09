package de.uniol.inf.is.odysseus.physicaloperator.objectrelational;

import junit.textui.TestRunner;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.test.TestSuiteAllTests;

public class Activator implements BundleActivator {

	static BundleContext context;
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext c) throws Exception {	    
	    TestRunner.run(new TestSuiteAllTests());
		context = c;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
	}
	
	static BundleContext getContext(){
		return context;
	}

}
