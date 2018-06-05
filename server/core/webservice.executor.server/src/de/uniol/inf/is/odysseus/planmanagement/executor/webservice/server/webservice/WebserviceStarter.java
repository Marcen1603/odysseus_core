package de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice;

import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.WebServicePublisher;

public class WebserviceStarter {
	public static void start() {
		// wait for bundle activation
		Thread t = new Thread(new Runnable() {
			@Override public void run() {
				WebServicePublisher.publish(new WebserviceServer(), "");
			}
		});
		t.setDaemon(true);
		t.start();
	}
}
