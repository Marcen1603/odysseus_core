package de.uniol.inf.is.odysseus.generator.random;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.generator.StreamServer;

public class RandomGeneratorPlugIn implements BundleActivator {

	private static final Logger LOG = LoggerFactory.getLogger(RandomGeneratorPlugIn.class);
	
	private RandomStreamClientHandler handler1;
	private RandomStreamClientHandler handler2;
	
	public void start(BundleContext bundleContext) throws Exception {
		LOG.debug("Start Generators of random numbers");
		
		handler1 = new RandomStreamClientHandler();
		handler2 = new RandomStreamClientHandler();
		
		StreamServer streamServer1 = new StreamServer(11111, handler1);
		streamServer1.start();
		StreamServer streamServer2 = new StreamServer(22222, handler2);
		streamServer2.start();
	}

	public void stop(BundleContext bundleContext) throws Exception {
		LOG.debug("Stop Generators of random numbers");
		
		handler1.stopGeneration();
		handler2.stopGeneration();
	}

}
