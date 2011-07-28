package de.uniol.inf.is.odysseus.storing;

import java.net.URL;
import java.util.Properties;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StoringActivator implements BundleActivator{

	volatile protected static Logger LOGGER = LoggerFactory.getLogger(StoringActivator.class);
	
	@Override
	public void start(BundleContext context) throws Exception {
		StoringActivator.LOGGER.info("Startup Storing Bundle.");
		URL configuration = context.getBundle().getEntry(System.getenv("databasecf"));		
		StoringActivator.LOGGER.info(String.format("Read configuration from %s ...", configuration));
		Properties properties = new Properties();
		properties.loadFromXML(configuration.openStream());
	    DatabaseService.registerDefaultUser(properties.getProperty("user"), properties.getProperty("password"));
		StoringActivator.LOGGER.info(String.format("Done"));
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		StoringActivator.LOGGER.info("Stoped Storing Bundle.");		
	}

	
}
