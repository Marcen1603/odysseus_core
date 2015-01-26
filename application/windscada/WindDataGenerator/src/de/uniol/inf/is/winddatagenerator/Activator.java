package de.uniol.inf.is.winddatagenerator;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.generator.StreamServer;

/**
 * Activator of the WinDataGenerator bundle. Creates and starts wind data
 * generators
 * 
 * @author Dennis Nowak
 * 
 */
public class Activator implements BundleActivator {

	private static BundleContext context;
	private List<StreamServer> providerList;

	/**
	 * Returns the BundleContext
	 * 
	 * @return the BundleContext
	 */
	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		// Starts the ConsoleInputReader which can deal with commands that are
		// received in the standard input
		(new Thread(ConsoleInputReader.getInstance())).start();
		// Create and start default WindDataProviders
		providerList = new ArrayList<StreamServer>();
		StreamServer server = new StreamServer(51128,
				new WindTurbineDataProvider(WindSource.getInstance()
						.getWindSourceID()));
		providerList.add(server);
		StreamServer server2 = new StreamServer(61128,
				new WindTurbineDataProvider(WindSource.getInstance()
						.getWindSourceID()));
		providerList.add(server2);
		StreamServer server3 = new StreamServer(11168,
				new WindTurbineDataProvider(WindSource.getInstance()
						.getWindSourceID()));
		providerList.add(server3);
		StreamServer server4 = new StreamServer(61168,
				new WindTurbineDataProvider(WindSource.getInstance()
						.getWindSourceID()));
		providerList.add(server4);
		StreamServer server5 = new StreamServer(51168,
				new WindTurbineDataProvider(WindSource.getInstance()
						.getWindSourceID()));
		providerList.add(server5);
		for (StreamServer provider : providerList) {
			provider.start();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		for (StreamServer provider : providerList) {
			provider.stopClients();
		}
		Activator.context = null;
	}

}
