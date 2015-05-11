package de.uniol.inf.is.odysseus.genrator.vibration;

import java.util.Scanner;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.generator.StreamServer;

public class Activator implements BundleActivator {

	private static BundleContext context;
	private Scanner sc;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		FridgeVibrationSensorDataProvider fridge = new FridgeVibrationSensorDataProvider();
		StreamServer server = new StreamServer(54322, fridge);
		server.start();
		while(true) {
			sc = new Scanner(System.in);
			String command = sc.nextLine();
			if (command.equals("pause")) {
				fridge.pause();
				System.out.println("paused");
			} else {
				fridge.resume();
				System.out.println("resumed");
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		sc.close();
		Activator.context = null;
	}

}
