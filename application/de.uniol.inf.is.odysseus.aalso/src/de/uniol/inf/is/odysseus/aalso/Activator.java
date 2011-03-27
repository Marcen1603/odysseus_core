package de.uniol.inf.is.odysseus.aalso;

// import java.net.URL;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.aalso.control.Controller;

public class Activator implements BundleActivator {

	private static BundleContext context;
	private Controller siafuInstance;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		// Siafu.main(new String[]{"-c=config.xml", "-s=IDEAAL.jar"});
		
		// URL configURL = Activator.getContext().getBundle().getEntry("/de/uniol/inf/is/odysseus/aalso/simulation/simulationConfigs/config.xml");
		String configPath = "C:\\Users\\Horizon\\.Siafu\\config.xml";//"/de/uniol/inf/is/odysseus/aalso/simulation/simulationConfigs/config.xml";//configURL.getFile();
		// URL simulationURL = Activator.getContext().getBundle().getEntry("de/uniol/inf/is/odysseus/aalso/simulation/src");
		String simulationPath = "/de/uniol/inf/is/odysseus/aalso/simulation/src";//simulationURL.getFile();
		siafuInstance = new Controller(configPath, simulationPath);
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
		siafuInstance.endSimulator();
	}
}
