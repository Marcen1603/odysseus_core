package de.uniol.inf.is.odysseus.generator.securitypunctuations;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.generator.StreamServer;

public class Activator implements BundleActivator {
	
	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		StreamServer server1 = new StreamServer(25598, new SPProvider("Stream1","Heartbeat",1000));
		
		server1.start();
		
		
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		Activator.context = null;

	}

}
