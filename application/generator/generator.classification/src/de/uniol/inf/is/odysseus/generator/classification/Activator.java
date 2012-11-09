package de.uniol.inf.is.odysseus.generator.classification;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.generator.StreamServer;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		StreamServer train = new StreamServer(54321, new ClassificationProvider());
		train.start();
		StreamServer test= new StreamServer(54322, new ClassificationProvider());
		test.start();
		
		StreamServer adultTrain = new StreamServer(54324, new AdultDataProvider("adult.data"));
		adultTrain.start();
		StreamServer adultTest = new StreamServer(54325, new AdultDataProvider("adult.test"));
		adultTest.start();
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
