package de.uniol.inf.is.debschallenge.reader;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.generator.StreamServer;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator implements BundleActivator  {


	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	@Override
	public void start(BundleContext context) throws Exception {
		Activator.context = context;
		StreamServer server = new StreamServer(54321, new DataProvider());
		server.start();
//		(new RepairTool()).go();
		
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		Activator.context = null;		
	}


}
