package de.uniol.inf.is.odysseus.generator.securitypunctuation;

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
		
		Integer delay = 5;
		boolean benchmark = true;
		Float spProbability = (float) 0.2;
		
	    StreamServer server1 = new StreamServer(54321, new SecurityPunctuationProvider(true, "attribute", delay, "server1", benchmark, spProbability));
	    StreamServer server2 = new StreamServer(54322, new SecurityPunctuationProvider(true, "attribute", delay, "server2", benchmark, spProbability));
	    StreamServer server3 = new StreamServer(54323, new CSVSPProvider("input.csv", delay, "server3", benchmark));
	    StreamServer server4 = new StreamServer(54324, new CSVSPProvider("input2.csv", delay, "server4", benchmark));
//	    StreamServer server5 = new StreamServer(54325, new SecurityPunctuationProvider(false, "attribute", delay, "server6", benchmark, spProbability));
//	    StreamServer server6 = new StreamServer(54326, new SecurityPunctuationProvider(false, "attribute", delay, "server7", benchmark, spProbability));
	    server1.start();
	    server2.start();
	    server3.start();
	    server4.start();
//	    server5.start();
//	    server6.start();
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
