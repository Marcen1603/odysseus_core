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
		
		Integer delay = 50;
		
	    StreamServer server1 = new StreamServer(54321, new SecurityPunctuationProvider(true, "attribute", delay, "server1"));
	    StreamServer server2 = new StreamServer(54322, new SecurityPunctuationProvider(true, "attribute", delay, "server2"));
	    StreamServer server3 = new StreamServer(54323, new CSVSPProvider("input.csv"));
	    StreamServer server4 = new StreamServer(54324, new SecurityPunctuationProvider(false, "attribute", delay, "server4"));
	    StreamServer server5 = new StreamServer(54325, new CSVSPProvider("input2.csv"));
	    server1.start();
	    server2.start();
	    server3.start();
	    server4.start();
	    server5.start();
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
