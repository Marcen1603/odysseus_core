package de.uniol.inf.is.odysseus.generator.conditionmonitoring;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.generator.StreamServer;
import de.uniol.inf.is.odysseus.generator.conditionmonitoring.offshore.substation.PipeDataProvider;
import de.uniol.inf.is.odysseus.generator.conditionmonitoring.offshore.substation.PumpDataProvider;
import de.uniol.inf.is.odysseus.generator.conditionmonitoring.offshore.substation.PumpStateDataProvider;
import de.uniol.inf.is.odysseus.generator.conditionmonitoring.offshore.substation.TransformerDataProvider;
import de.uniol.inf.is.odysseus.generator.conditionmonitoring.offshore.substation.ValveDataProvider;
import de.uniol.inf.is.odysseus.generator.conditionmonitoring.offshore.substation.model.OffshoreSubstationManager;
import de.uniol.inf.is.odysseus.generator.conditionmonitoring.offshore.windpark.WindturbineDataProvider;
import de.uniol.inf.is.odysseus.generator.conditionmonitoring.offshore.windpark.model.OffshoreWindparkManager;

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
		boolean demo = true;
		if (!demo) {
			startTestInstance();
		} else {
			startWindparkDemonstration();
		}
	}

	private void startTestInstance() throws Exception {
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

		// A context-vibration sensor
		ContextVibrationDataProvider contextVibrationProvider = new ContextVibrationDataProvider();
		StreamServer contextVibrationServer = new StreamServer(53218, contextVibrationProvider);
		contextVibrationServer.start();

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

	private void startWindparkDemonstration() throws Exception {
		OffshoreWindparkManager windparkManager = new OffshoreWindparkManager();
		OffshoreSubstationManager substationManager = new OffshoreSubstationManager(windparkManager);

		// Windpark
		// --------
		WindturbineDataProvider windturbineDataProvider = new WindturbineDataProvider(
				windparkManager.getWindturbines());
		StreamServer windturbineStreamServer = new StreamServer(52000, windturbineDataProvider);
		windturbineStreamServer.start();

		// Substation
		// ----------

		// Pumps
		PumpDataProvider pump1DataProvider = new PumpDataProvider(substationManager.getPump1());
		StreamServer pump1StreamServer = new StreamServer(51000, pump1DataProvider);
		pump1StreamServer.start();

		PumpDataProvider pump2DataProvider = new PumpDataProvider(substationManager.getPump2());
		StreamServer pump2StreamServer = new StreamServer(51001, pump2DataProvider);
		pump2StreamServer.start();

		PumpDataProvider pump3DataProvider = new PumpDataProvider(substationManager.getPump3());
		StreamServer pump3StreamServer = new StreamServer(51002, pump3DataProvider);
		pump3StreamServer.start();

		// States of pumps
		PumpStateDataProvider pump1StateDataProvider = new PumpStateDataProvider(substationManager.getPump1());
		StreamServer pump1StateStreamServer = new StreamServer(51010, pump1StateDataProvider);
		pump1StateStreamServer.start();

		PumpStateDataProvider pump2StateDataProvider = new PumpStateDataProvider(substationManager.getPump2());
		StreamServer pump2StateStreamServer = new StreamServer(51011, pump2StateDataProvider);
		pump2StateStreamServer.start();

		PumpStateDataProvider pump3StateDataProvider = new PumpStateDataProvider(substationManager.getPump3());
		StreamServer pump3StateStreamServer = new StreamServer(51012, pump3StateDataProvider);
		pump3StateStreamServer.start();

		// Valves
		ValveDataProvider valve1DataProvider = new ValveDataProvider(substationManager.getValve1());
		StreamServer valve1StreamServer = new StreamServer(51003, valve1DataProvider);
		valve1StreamServer.start();

		ValveDataProvider valve2DataProvider = new ValveDataProvider(substationManager.getValve2());
		StreamServer valve2StreamServer = new StreamServer(51004, valve2DataProvider);
		valve2StreamServer.start();

		ValveDataProvider valve3DataProvider = new ValveDataProvider(substationManager.getValve3());
		StreamServer valve3StreamServer = new StreamServer(51005, valve3DataProvider);
		valve3StreamServer.start();

		// Transformer
		TransformerDataProvider transformerDataProvider = new TransformerDataProvider(
				substationManager.getTransformer1());
		StreamServer transformerStreamServer = new StreamServer(51006, transformerDataProvider);
		transformerStreamServer.start();

		// Pipe at the transformer
		PipeDataProvider pipeDataProvider = new PipeDataProvider(substationManager.getBigPipeOut());
		StreamServer pipeDataStreamServer = new StreamServer(51007, pipeDataProvider);
		pipeDataStreamServer.start();

		substationManager.run();

		// Listen to commands from the user
		// Listen for commands
		while (true) {
			sc = new Scanner(System.in);
			while (sc.hasNextLine()) {
				String command = sc.nextLine();
				// Valve 2
				if (command.equalsIgnoreCase("switchValve2")) {
					if (substationManager.getValve2().isOpen()) {
						substationManager.getValve2().close();
						System.out.println("Closed valve 2");
					} else {
						substationManager.getValve2().open();
						System.out.println("Opened valve 2");
					}
					// Pump 1
				} else if (command.equalsIgnoreCase("defectPump1")) {
					substationManager.getPump1().setDefect(true);
					System.out.println("Ooops, Pump 1 is now defect");
				} else if (command.equalsIgnoreCase("repairPump1")) {
					substationManager.getPump1().setDefect(false);
					System.out.println("Repaired Pump 1");
				}
				// Windturbine 0
				else if (command.equalsIgnoreCase("defectTurbine0")) {
					windparkManager.getWindturbines().get(0).setDefect(true);
					System.out.println("Windturbine 0 is now defect");
				} else if (command.equalsIgnoreCase("repairTurbine0")) {
					windparkManager.getWindturbines().get(0).setDefect(false);
					System.out.println("Windturbine 0 was repaired");
				}
				// Last windturbine
				else if (command.equalsIgnoreCase("defectLastTurbine")) {
					windparkManager.getWindturbines().get(windparkManager.getWindturbines().size() - 1).setDefect(true);
					System.out.println("Last windturbine is now defect");
				} else if (command.equalsIgnoreCase("repairLastTurbine")) {
					windparkManager.getWindturbines().get(windparkManager.getWindturbines().size() - 1)
							.setDefect(false);
					System.out.println("Last windturbine was repaired");
				}
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
