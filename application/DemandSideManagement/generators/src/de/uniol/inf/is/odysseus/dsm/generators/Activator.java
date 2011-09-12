package de.uniol.inf.is.odysseus.dsm.generators;

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
		
		System.out.println("Starting fridge server...");
		StreamServer fridgeServer = new StreamServer(54321, Fridge.class);
		fridgeServer.start();
		
		System.out.println("Starting washing machine server...");
		StreamServer washingMachineServer = new StreamServer(54322, WashingMachine.class);
		washingMachineServer.start();
		
		System.out.println("Starting microwave server...");
		StreamServer microwaveServer = new StreamServer(54323, Microwave.class);
		microwaveServer.start();
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
