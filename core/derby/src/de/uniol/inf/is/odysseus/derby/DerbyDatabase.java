package de.uniol.inf.is.odysseus.derby;

/** Copyright [2011] [The Odysseus Team]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.util.Properties;

import de.uniol.inf.is.odysseus.storing.DatabaseService;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is only the Derby database loader. The connection is managed in the
 * odysseus storing bundle.
 */
public class DerbyDatabase implements BundleActivator {

	volatile protected static Logger LOGGER = LoggerFactory
			.getLogger(DerbyDatabase.class);
	
	private static String 	database = null,
							user = null,
							password = null,
							host = null,
							port = null,
							create = null;
	//private static boolean 	create = false;
	
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		LOGGER.info("Start Derby Database.");
		
		Properties properties;
		InputStream inputStream;
		
		String configFile = System.getenv("dcf");
		if(configFile.isEmpty()){
			LOGGER.info("No configuration file in the environment the argument dcf is empty.");
		}
		else{
			LOGGER.info(String.format("Configuration file: %s.", configFile));
			
			properties = new Properties();
			inputStream = getClass().getResourceAsStream("/" + configFile);
			try {
				properties.loadFromXML(inputStream);
				
				DerbyDatabase.host = properties.getProperty("host").trim();
				DerbyDatabase.port = properties.getProperty("port").trim();
				DerbyDatabase.database = properties.getProperty("database").trim();
				DerbyDatabase.create = properties.getProperty("create").trim();
				DerbyDatabase.user = properties.getProperty("user").trim();
				DerbyDatabase.password = properties.getProperty("password").trim();
				
//				String defaultURL = String.format("jdbc:derby://%s:%s/%s;create=%s",host,port,database,create);
//				
//				DatabaseService.registerDefault(defaultURL, user, password);
//				DatabaseService.getDefaultConnection();
				
			} catch (FileNotFoundException e) {			
				LOGGER.error(String.format("Configuration file not found: %s.", configFile), e.getStackTrace());
			} catch (IOException e) {
				LOGGER.error(String.format("Cannot read configuration file: %s.", configFile), e.getStackTrace());
			}	
			
			
		}
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		LOGGER.info("Stop Derby Database.");
	}

}
