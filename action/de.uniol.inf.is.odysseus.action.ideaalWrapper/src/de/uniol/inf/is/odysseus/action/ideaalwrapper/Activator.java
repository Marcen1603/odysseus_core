package de.uniol.inf.is.odysseus.action.ideaalwrapper;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
	private int startPort = 55555;
	private List<StreamServer> servers;

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		servers = new ArrayList<StreamServer>();
		for (Sensor sensor : Sensor.values()){
			servers.add(new StreamServer(sensor, ++startPort));
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		if (this.servers != null){
			for (StreamServer server : this.servers){
				server.closeSockets();
			}
		}
		this.servers.clear();
	}

}
