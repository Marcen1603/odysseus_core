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
package de.uniol.inf.is.odysseus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.usermanagement.AccessControl;
import de.uniol.inf.is.odysseus.usermanagement.HasNoPermissionException;
import de.uniol.inf.is.odysseus.usermanagement.User;

public class OdysseusDefaults {

	static Logger logger = LoggerFactory.getLogger(OdysseusDefaults.class);
	static Properties props = new Properties();

	private static String odysseusDefaultHome = System.getProperty("user.home")
			+ "/odysseus/";
	private static String homeDir;

	static public File openOrCreateFile(String path) throws IOException {
		File f = new File(path);
		boolean success = false;
		if (!f.exists()) {
			File d = f.getParentFile();
			if (d != null) {
				success = d.mkdirs();
			}
			success = f.createNewFile();
			if (success){
				logger.debug("Created new File " + f.getAbsolutePath());
			}
		} else {
			logger.debug("Read from file " + f.getAbsolutePath());
		}
		return f;
	}

	static {
		try {
			homeDir = System.getenv("ODYSSEUS_HOME");
			if (homeDir == null || homeDir.length() == 0){
				homeDir = odysseusDefaultHome;
			}
			loadProperties(homeDir, "odysseus.conf", props);
			if (props.getProperty("storeUsers") == null) {
				logger.info("No Odysseus config found.");
				setDefaults(homeDir);
				savePropertyFile(homeDir);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void loadProperties(String odysseusHome, String filename, Properties properties) throws IOException,
			FileNotFoundException {
		File f = openOrCreateFile(odysseusHome + filename);
		FileInputStream in;
		in = new FileInputStream(f);
		properties.load(in);
		in.close();
	}

	private static void setDefaults(String odysseusHome) {
		props.setProperty("odysseusHome", odysseusHome);
		props.setProperty("storeUsers", Boolean.FALSE.toString());
		props.setProperty("userStoreFilename", odysseusHome + "users.store");
		props.setProperty("storeTenants", Boolean.FALSE.toString());
		props.setProperty("tenantsFilename", odysseusHome + "tenants.store");
		props.setProperty("userTenantFilename", odysseusHome
				+ "userTenant.store");
		props.setProperty("slasFilename", odysseusHome + "slas.store");

		props.setProperty("storeDataDictionary", Boolean.FALSE.toString());
		props.setProperty("streamDefinitionsFilename", odysseusHome
				+ "viewDefinitions.store");
		props.setProperty("streamOrViewFromUserFilename", odysseusHome
				+ "viewFromUser.store");
		props.setProperty("viewDefinitionsFilename", odysseusHome
				+ "logicalViewDefinitions.store");
		props.setProperty("entitiesFilename", odysseusHome + "entities.store");
		props.setProperty("sourceTypeMapFilename", odysseusHome
				+ "sourceTypeMap.store");
		props.setProperty("entityFromUserFilename", odysseusHome
				+ "entityFromUser.store");
		props.setProperty("sourceFromUserFilename", odysseusHome
				+ "sourceFromUser.store");
		props.setProperty("schedulingConfigFile", odysseusHome
				+ "scheduling.conf");

		props.setProperty("sessionTimeout", (240 * 60000) + ""); // Milliseconds

		// Scheduling
		props.setProperty("debug_Scheduler", Boolean.FALSE.toString());
		props.setProperty("debug_Scheduler_maxLines", 1048476 + "");
		props.setProperty("scheduler_TimeSlicePerStrategy", 10 + "");
		props.setProperty("scheduler_DebugFileName","SchedulerLog");

		// SLA
		props.setProperty("sla_history_size", 10000 + ""); // Milliseconds
		props.setProperty("sla_updatePenaltyTime", 60000+""); // Milliseconds
	};

	private static void savePropertyFile(String odysseusHome) {
		FileOutputStream out;
		try {
			out = new FileOutputStream(odysseusHome + "odysseus.conf");
			props.store(out,
					"--- Odysseus Property File edit only if you know what you are doing ---");
			out.close();
			logger.info("New Odysseus-Config-File created");
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}

	public static String get(String key) {
		return props.getProperty(key);
	}

	public static long getLong(String key, long defaultValue) {
		String val = props.getProperty(key);
		return val != null ? Long.parseLong(val) : defaultValue;
	}

	public static void set(String key, String value, boolean permanent,
			User caller) {
		if (AccessControl.hasPermission(ConfigurationAction.SET_PARAM,
				ConfigurationAction.alias, caller)) {
			props.setProperty(key, value);
			if (permanent) {
				if (AccessControl.hasPermission(ConfigurationAction.SAVE_PARAM,
						ConfigurationAction.alias, caller)) {
					savePropertyFile(homeDir);
				} else {
					throw new HasNoPermissionException(
							"User "
									+ caller
									+ " is not allowed to permanently set config param "
									+ key);
				}
			}
		} else {
			throw new HasNoPermissionException("User " + caller
					+ " is not allowed to temporally set config param " + key);
		}
	}
	
	public static String getHomeDir(){
		return homeDir;
	}

}
