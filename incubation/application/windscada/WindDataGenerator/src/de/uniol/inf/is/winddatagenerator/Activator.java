package de.uniol.inf.is.winddatagenerator;

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
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		(new Thread(ConsoleInputReader.getInstance())).start();
		StreamServer server = new StreamServer(51128, new WindTurbineDataProvider(WindSource.getInstance().getWindSourceID()));
		StreamServer server2 = new StreamServer(61128, new WindTurbineDataProvider(WindSource.getInstance().getWindSourceID()));
		StreamServer server3 = new StreamServer(11168, new WindTurbineDataProvider(WindSource.getInstance().getWindSourceID()));
		StreamServer server4 = new StreamServer(61168, new WindTurbineDataProvider(WindSource.getInstance().getWindSourceID()));
		StreamServer server5 = new StreamServer(51168, new WindTurbineDataProvider(WindSource.getInstance().getWindSourceID()));
		server.start();
	    server2.start();
	    server3.start();
	    server4.start();
	    server5.start();
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
