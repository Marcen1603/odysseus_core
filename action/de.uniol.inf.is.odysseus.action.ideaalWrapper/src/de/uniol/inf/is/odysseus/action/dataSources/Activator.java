package de.uniol.inf.is.odysseus.action.dataSources;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.action.dataSources.ideaal.Sensor;

public class Activator implements BundleActivator {
	private int startPort = 55555;
	private List<StreamServer> servers;

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		Logger logger = LoggerFactory.getLogger( Activator.class );
		logger.info("Starting wrapper servers ...");
		servers = new ArrayList<StreamServer>();
		for (Sensor sensor : Sensor.values()){
			try {
				StreamServer server = new StreamServer(sensor, ++startPort);
				server.start();
				servers.add(server);
			}catch (Exception e){
				e.printStackTrace();
			}
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
