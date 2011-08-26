package de.offis.salsa.simulator.car;

import de.offis.salsa.simulator.car.impl.CarModelServerImpl;
import de.offis.salsa.simulator.car.impl.CarStatusConnectionImpl;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length >= 3) {
			final String host = args[0];
			final int port = Integer.parseInt(args[1]);
			final int serverPort = Integer.parseInt(args[2]);
			CarModelServerImpl modelServer = new CarModelServerImpl(
					serverPort);
			modelServer.start();
			CarStatusConnectionImpl statusConnection = new CarStatusConnectionImpl(
					host, port);
			statusConnection.start();
			while (!statusConnection.isInterrupted()) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			modelServer.interrupt();
		} else {
			System.err.println("Invalid arguments: <Host/IP> <Port> <Server Port>");
		}
	}

}
