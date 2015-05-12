package de.uniol.inf.is.odysseus.generator.conditionmonitoring;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.generator.StreamServer;

public class Activator implements BundleActivator {

	private static BundleContext context;

	private static PlantDataProvider plantDataProvider;
	private static int windPowerPlantId;

	private Scanner sc;

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
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		plantDataProvider = new PlantDataProvider();
		windPowerPlantId = 0;

		// Create a windpark with many wind power plants
		int port = 54321;
		int numberOfPowerPlants = 5;
		List<StreamServer> servers = new ArrayList<StreamServer>(numberOfPowerPlants);
		List<WindPowerPlantDataProvider> powerPlants = new ArrayList<WindPowerPlantDataProvider>(numberOfPowerPlants);

		for (int i = 0; i < numberOfPowerPlants; i++) {
			WindPowerPlantDataProvider powerPlant = new WindPowerPlantDataProvider(windPowerPlantId++,
					plantDataProvider);
			servers.add(new StreamServer(port++, powerPlant));
			powerPlants.add(powerPlant);
		}

		for (StreamServer server : servers) {
			server.start();
		}
		
		// Create a state data provider
		StateDataProvider stateProvider = new StateDataProvider();
		StreamServer server = new StreamServer(53211, stateProvider);
		server.start();

		// Listen for commands
		while (true) {
			sc = new Scanner(System.in);
			String command = sc.nextLine();
			if (command.equals("stopfirst")) {
				powerPlants.get(0).stopPlant();
				System.out.println("Paused plant 0");
			} else {
				powerPlants.get(0).restartPlant();
				System.out.println("Resumed plant 0");
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
