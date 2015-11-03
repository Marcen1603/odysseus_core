package de.uniol.inf.is.odysseus.net.discovery.broadcast;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.net.connect.IOdysseusNodeConnectionManager;

public class BroadcastDiscoveryPlugIn implements BundleActivator {

	public static final int BROADCAST_PORT_MIN = 60100;
	public static final int BROADCAST_PORT_MAX = 60106;
	
	public static final String BROADCAST_REQUEST_CONTENT = "BROADCAST_REQUEST";
	public static final String DISCOVERER_INTERVAL_CONFIG_KEY = "net.discoverer.interval";
	
	private static IOdysseusNodeConnectionManager connectionManager;

	// called by OSGi-DS
	public static void bindOdysseusNodeConnectionManager(IOdysseusNodeConnectionManager serv) {
		connectionManager = serv;
	}

	// called by OSGi-DS
	public static void unbindOdysseusNodeConnectionManager(IOdysseusNodeConnectionManager serv) {
		if (connectionManager == serv) {
			connectionManager = null;
		}
	}
	
	public static IOdysseusNodeConnectionManager getOdysseusNodeConnectionManager() {
		return connectionManager;
	}
	
	@Override
	public void start(BundleContext bundleContext) throws Exception {
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
	}

}
