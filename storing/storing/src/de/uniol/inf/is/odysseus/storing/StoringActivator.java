package de.uniol.inf.is.odysseus.storing;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.OdysseusDefaults;

public class StoringActivator implements BundleActivator{

	volatile protected static Logger LOGGER = LoggerFactory.getLogger(StoringActivator.class);
	
	@Override
	public void start(BundleContext context) throws Exception {
		StoringActivator.LOGGER.info("Startup Storing Bundle.");
		String storingDB = OdysseusDefaults.get("storing_database");
		if (storingDB == null){
			throw new RuntimeException("Storing not correctly defined. Missing Entry storing_database in odysseus.properties. Terminating");
		}
		File file = new File(storingDB);
		if (!file.exists()) {
			StoringActivator.LOGGER.error(String.format("File does not exist: %s !", file.toString()));
		} else {
			StoringActivator.LOGGER.info(String.format("Read configuration from %s ...", file.toString()));
			FileInputStream inputStream = new FileInputStream(file);
			Properties properties = new Properties();
			properties.loadFromXML(inputStream);
			inputStream.close();
		    DatabaseService.registerDefaultUser(properties.getProperty("user"), properties.getProperty("password"));
			StoringActivator.LOGGER.info(String.format("Done"));	
		}
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		StoringActivator.LOGGER.info("Stoped Storing Bundle.");		
	}

	
}
