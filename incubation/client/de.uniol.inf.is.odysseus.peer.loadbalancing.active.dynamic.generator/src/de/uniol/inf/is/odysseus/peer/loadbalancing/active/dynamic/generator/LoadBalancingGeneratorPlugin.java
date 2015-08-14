package de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.generator;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.generator.StreamServer;

public class LoadBalancingGeneratorPlugin implements BundleActivator {

	private static BundleContext context;
	
	static BundleContext getContext() {
		return context;
	}

	
	@Override
	public void start(BundleContext context) throws Exception {
		LoadBalancingGeneratorPlugin.context = context;
		StreamServer server = new StreamServer(SimpleIncrementGenerator.SERVER_DEFAULT_PORT,new SimpleIncrementGenerator());
		server.start();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		
		
	}

}
