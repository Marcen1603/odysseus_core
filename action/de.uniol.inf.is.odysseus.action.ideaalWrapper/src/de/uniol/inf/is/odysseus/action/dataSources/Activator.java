package de.uniol.inf.is.odysseus.action.dataSources;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.action.dataSources.ideaal.Sensor;
import de.uniol.inf.is.odysseus.action.dataSources.ideaal.SocketSensorClient;

/**
 * Activator for dataSource Servers. Allows
 * @author Simon Flandergan
 *
 */
public class Activator implements BundleActivator {
	private int startPort = 55555;
	private List<StreamServer> servers;

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		//load properties
		Properties serverProps = new Properties();
		InputStream stream = context.getBundle().getResource("resources/serverConfig.xml").openStream();
		serverProps.loadFromXML(stream);
		
		boolean useIdealSensor = Boolean.valueOf(serverProps.getProperty("useIdealSensor"));
		boolean useGenerator = Boolean.valueOf(serverProps.getProperty("useGenerator"));
		stream.close();
		
		Logger logger = LoggerFactory.getLogger( Activator.class );
		if (useIdealSensor){
			logger.info("Starting wrapper servers ...");
			servers = new ArrayList<StreamServer>();
			for (Sensor sensor : Sensor.values()){
				try {
					StreamServer server = new StreamServer(new SocketSensorClient(sensor), ++startPort);
					server.start();
					servers.add(server);
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		}
		
		if(useGenerator){
			Properties generatorProps = new Properties();
			stream = context.getBundle().getResource("resources/serverConfig.xml").openStream();
			generatorProps.loadFromXML(stream);
			stream.close();
			
			
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
