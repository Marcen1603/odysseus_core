package de.uniol.inf.is.odysseus.sensormanagement.server;

import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.WebServicePublisher;

public class SensorServiceStarter {

	public static void start() {
		// wait for bundle activation
		Thread t = new Thread(new Runnable() {
			@Override public void run() {				
				WebServicePublisher.publish(new SensorService(), "sensormanagement");
			}
		});
		t.setDaemon(true);
		t.start();
	}
}
