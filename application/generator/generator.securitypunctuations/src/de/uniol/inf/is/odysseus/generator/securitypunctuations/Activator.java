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
//		StreamServer server1 = new StreamServer(25598, new SPProvider("Heartbeat",1000,true));
//		StreamServer server2 = new StreamServer(25599, new SPProvider("Pulse",1000,false));
//		server1.start();
//		server2.start();
		StreamServer server1=new StreamServer(25599,new CSVTupleProvider(1000,"InputStream1.csv"));
	StreamServer server2=new StreamServer(25600,new CSVTupleProvider(1000,"InputStream2.csv"));
		server1.start();
		server2.start();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		Activator.context = null;

	}

}
