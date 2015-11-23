package de.uniol.inf.is.odysseus.server.opcua;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.server.opcua.binding.OPCUAComponent;

public class Activator implements BundleActivator {

	private final Logger log = LoggerFactory.getLogger(Activator.class);

	private static BundleContext context;

	private OPCUAComponent component;

	@Override
	public void start(BundleContext ctx) throws Exception {
		log.info("Starting bundle...");
		context = ctx;
		// Force-load every OPC UA class
		RegisterClasses.registerEnums();
		RegisterClasses.registerStructures();
		// Start OPC UA server
		component = OPCUAComponent.Instance;
	}

	@Override
	public void stop(BundleContext ctx) throws Exception {
		// Stop OPC UA server
		component.close();
		// Finish bundle
		log.info("Stopping bundle...");
		context = null;
	}

	public static BundleContext getContext() {
		return context;
	}
}