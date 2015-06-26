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

		// Create a temperature data provider
		TemperatureDataProvider tempProvider = new TemperatureDataProvider();
		StreamServer tempServer = new StreamServer(53212, tempProvider);
		tempServer.start();

		// Create a manual data provider
		ManualDataProvider manualProvider = new ManualDataProvider();
		StreamServer manualServer = new StreamServer(53213, manualProvider);
		manualServer.start();

		// Create a fridge vibration data provider
		FridgeVibrationSensorDataProvider fridgeProvider = new FridgeVibrationSensorDataProvider();
		StreamServer fridgeServer = new StreamServer(53214, fridgeProvider);
		fridgeServer.start();

		// Create a slow temperature data provider
		SlowTemperatureDataProvider slowTempProvider = new SlowTemperatureDataProvider();
		StreamServer slowTempServer = new StreamServer(53215, slowTempProvider);
		slowTempServer.start();

		// Create a data provider for the milling cutter
		MillingCutterPowerConsumptionDataProvider millingCutterProvider = new MillingCutterPowerConsumptionDataProvider();
		StreamServer millingCutterServer = new StreamServer(53216, millingCutterProvider);
		millingCutterServer.start();

		// And another fridge
		FridgeVibrationSensorDataProvider fridgeProvider2 = new FridgeVibrationSensorDataProvider();
		StreamServer fridgeServer2 = new StreamServer(53217, fridgeProvider2);
		fridgeServer2.start();

		// Listen for commands
		while (true) {
			sc = new Scanner(System.in);
			while (sc.hasNextLine()) {
				String command = sc.nextLine();
				if (command.equalsIgnoreCase("stopfirst")) {
					// Wind power plant
					powerPlants.get(0).stopPlant();
					System.out.println("Paused plant 0");
				} else if (command.equalsIgnoreCase("resume")) {
					// Wind power plant
					powerPlants.get(0).restartPlant();
					System.out.println("Resumed plant 0");
				} else if (command.equalsIgnoreCase("reduceFirst")) {
					// Wind power plant
					powerPlants.get(0).reducePlant();
					System.out.println("Reduced plant 0");
				} else if (command.equalsIgnoreCase("pauseFridge")) {
					// Fridge
					fridgeProvider.pause();
					System.out.println("Paused fridge");
				} else if (command.equalsIgnoreCase("resumeFridge")) {
					// Fridge
					fridgeProvider.resume();
					System.out.println("Resumed fridge");
				} else if (command.equalsIgnoreCase("pauseFridge2")) {
					// Fridge 2
					fridgeProvider2.pause();
				} else if (command.equalsIgnoreCase("resumeFridge2")) {
					// Fridge 2
					fridgeProvider2.resume();
				} else {

					double newValue = 0;
					try {
						newValue = Double.parseDouble(command);
					} catch (Exception e) {
						System.out.println("Could not parse command.");
					}

					manualProvider.sendNewValue(newValue);
				}
				Thread.sleep(10);
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
