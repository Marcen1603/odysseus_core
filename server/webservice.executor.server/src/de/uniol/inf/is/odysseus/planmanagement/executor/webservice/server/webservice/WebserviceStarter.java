package de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.WebServicePublisher;

public class WebserviceStarter {
	
	private static final Logger LOG = LoggerFactory.getLogger(WebserviceServer.class);
	 
	public static void start() {
		// wait for bundle activation
		Thread t = new Thread(new Runnable() {
			@Override public void run() {
				try {
					WebServicePublisher.publish(new WebserviceServer(), "");
				}catch(Exception e) {
					LOG.error("Error starting web service", e);
				}
			}
		});
		t.setDaemon(true);
		t.start();
	}
}
