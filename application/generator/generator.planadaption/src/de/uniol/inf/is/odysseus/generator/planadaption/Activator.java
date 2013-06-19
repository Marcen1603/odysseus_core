package de.uniol.inf.is.odysseus.generator.planadaption;


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
		
		StreamServer tempServer = new StreamServer(57001, new TemperatureGenerator());
		tempServer.start();
		System.out.println("Published TemperaturGenerator on port 57001");
		
		StreamServer humServer = new StreamServer(57002, new HumidityGenerator());
		humServer.start();
		System.out.println("Published HumidityGenerator on port 57002");
		
		StreamServer windServer = new StreamServer(57003, new WindGenerator());
		windServer.start();
		System.out.println("Published WindGenerator on port 57003");
		
		StreamServer pressureServer = new StreamServer(57004, new PressureGenerator());
		pressureServer.start();
		System.out.println("Published PressureGenerator on port 57004");

		StreamServer precipitationServer = new StreamServer(57005, new PrecipitationGenerator());
		precipitationServer.start();
		System.out.println("Published PrecipitationGenerator on port 57005");

		StreamServer visibilityServer = new StreamServer(57006, new VisibilityGenerator());
		visibilityServer.start();
		System.out.println("Published VisibilityGenerator on port 57006");		
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
