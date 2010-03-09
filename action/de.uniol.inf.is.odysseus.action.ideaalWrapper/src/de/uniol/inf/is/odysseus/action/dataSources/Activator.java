package de.uniol.inf.is.odysseus.action.dataSources;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.action.dataSources.generator.GeneratorConfig;
import de.uniol.inf.is.odysseus.action.dataSources.generator.MachineMaintenaceClient;
import de.uniol.inf.is.odysseus.action.dataSources.generator.ScenarioDatamodel;
import de.uniol.inf.is.odysseus.action.dataSources.generator.TupleGenerator.GeneratorType;
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

		this.servers = new ArrayList<StreamServer>();
		
		Logger logger = LoggerFactory.getLogger( Activator.class );
		
		if(useGenerator){
			Properties generatorProps = new Properties();
			stream = context.getBundle().getResource("resources/generatorConfig.xml").openStream();
			generatorProps.loadFromXML(stream);
			
			GeneratorConfig generatorConfig = new GeneratorConfig(generatorProps);
			stream.close();
			
			logger.info("Starting MachineMaintenance generators ...");
			
			ScenarioDatamodel.initiDataModel(generatorConfig);
			for (GeneratorType type : GeneratorType.values()){
				try {
					if ( 	(type.equals(GeneratorType.Install_Pure) && generatorConfig.isSimulateDB() ) ||
							(type.equals(GeneratorType.Install_DB) && !generatorConfig.isSimulateDB()  ) ){
						continue;
					}
					MachineMaintenaceClient client = new MachineMaintenaceClient(generatorConfig, type);
					StreamServer server = new StreamServer(client, ++startPort);
					server.start();
					this.servers.add(server);
				}catch (Exception e){
					e.printStackTrace();
				}
				
			}
		}
		
		if (useIdealSensor){
			logger.info("Starting wrapper servers ...");
			for (Sensor sensor : Sensor.values()){
				try {
					StreamServer server = new StreamServer(new SocketSensorClient(sensor ), ++startPort);
					server.start();
					servers.add(server);
				}catch (Exception e){
					e.printStackTrace();
				}
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
