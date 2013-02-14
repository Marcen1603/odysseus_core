/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.core.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagement;
import de.uniol.inf.is.odysseus.core.server.util.FileUtils;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.PermissionException;

public class OdysseusConfiguration {

	static Logger _logger = null;

	static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(OdysseusConfiguration.class);
		}
		return _logger;
	}

	static Properties props = new Properties();

	// TODO: Make Platform specific homedir
	private static String odysseusDefaultHome = String.format("%s"+File.separator+"%sodysseus"+File.separator,
			System.getProperty("user.home"),
			getDot(System.getProperty("os.name")));
	private static String homeDir;

	static {
		try {
			homeDir = System.getenv("ODYSSEUS_HOME");
			if (homeDir == null || homeDir.length() == 0) {
				homeDir = odysseusDefaultHome;
			}
			loadProperties(homeDir, "odysseus.conf", props);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void loadProperties(String odysseusHome, String filename,
			Properties properties) throws IOException, FileNotFoundException {
		// If there are new properties, load defaults and overwrite with
		// file-properties
		setDefaults(odysseusHome);
		File f = FileUtils.openOrCreateFile(odysseusHome + filename);
		FileInputStream in;
		in = new FileInputStream(f);
//		try {
//			properties.load(in);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		try {
			properties.loadFromXML(in);
		} catch (Exception e) {
			// Migration of old format
			try{
				properties.load(in);
				getLogger().debug("Property file migrated");
			}catch(Exception e2){
				e2.printStackTrace();
			}
		}
		in.close();
		// Save always because there could be new properties ... do only if
		// there are new defaults?
		savePropertyFile(odysseusHome);
	}

	private static void setDefaults(String odysseusHome) {
		props.setProperty("odysseusHome", odysseusHome);

		// Store-Filename
		props.setProperty("userStoreFilename", odysseusHome
				+ "store/users.store");
		props.setProperty("privilegStoreFilename", odysseusHome
				+ "store/priviliges.store");
		props.setProperty("roleStoreFilename", odysseusHome
				+ "store/roles.store");
		props.setProperty("slasFilename", odysseusHome + "store/slas.store");
		props.setProperty("streamDefinitionsFilename", odysseusHome
				+ "store/viewDefinitions.store");
		props.setProperty("streamOrViewFromUserFilename", odysseusHome
				+ "store/viewFromUser.store");
		props.setProperty("viewDefinitionsFilename", odysseusHome
				+ "store/logicalViewDefinitions.store");
		props.setProperty("sinkDefinitionsFilename", odysseusHome
				+ "store/sinkDefinitions.store");
		props.setProperty("sinkDefinitionsUserFilename", odysseusHome
				+ "store/sinkDefinitionsUser.store");
		props.setProperty("entitiesFilename", odysseusHome
				+ "store/entities.store");
		props.setProperty("sourceTypeMapFilename", odysseusHome
				+ "store/sourceTypeMap.store");
		props.setProperty("entityFromUserFilename", odysseusHome
				+ "store/entityFromUser.store");
		props.setProperty("sourceFromUserFilename", odysseusHome
				+ "store/sourceFromUser.store");
		props.setProperty("queriesFilename", odysseusHome
				+ "store/queries.store");
		props.setProperty("queriesUserFilename", odysseusHome
				+ "store/queriesUser.store");
		props.setProperty("queriesBuildParamFilename", odysseusHome
				+ "store/queriesBuildParam.store");

		props.setProperty("storeReloadLog", Boolean.TRUE.toString());
		props.setProperty("reloadLogStoreFilename", odysseusHome
				+ "reloadlog.store");

		props.setProperty("schedulingConfigFile", odysseusHome
				+ "scheduling.conf");
		props.setProperty("datatypesFromDatatypesFilename", odysseusHome
				+ "store/datatypes.store");

		props.setProperty("sessionTimeout", (240 * 60000) + ""); // Milliseconds

		// Scheduling
		props.setProperty("debug_Scheduler", Boolean.FALSE.toString());
		props.setProperty("debug_Scheduler_maxLines", 1048476 + "");
		props.setProperty("scheduler_TimeSlicePerStrategy", 10 + "");
		props.setProperty("scheduler_simpleThreadScheduler_executorThreadsCount",(-1)+"");
		props.setProperty("scheduler_DebugFileName", "SchedulerLog");
		props.setProperty("scheduler_trainSize", 1 + "");

		// SLA
		props.setProperty("sla_history_size", 10000 + ""); // Milliseconds
		props.setProperty("sla_updatePenaltyTime", 60000 + ""); // Milliseconds

		// new SLA
		props.setProperty("sla_starvationFreedomFuncName", "QueueSizeSF");
		props.setProperty("sla_prioFuncName", "max");
		props.setProperty("sla_starvationFreedomDecay", 0.1 + "");
		props.setProperty("sla_querySharingCostModel", "none");
		props.setProperty("sla_querySharing", Boolean.FALSE.toString());
		props.setProperty("sla_costFunctionName", "quadratic");
		props.setProperty("sla_pathTimeUpdateInterval", 10000 + ""); // Milliseconds

		// Odysseus Storing
		props.setProperty("storing_database", odysseusHome + "database.conf");

		// AC
		props.setProperty("ac_memHeadroom", "0.4");
		props.setProperty("ac_cpuHeadroom", "0.4");
		props.setProperty("ac_standardMemCost", "4");
		props.setProperty("ac_standardCpuCost", "0.00002");

		// Event Dispatcher
		props.setProperty("EventHandlerDispatcherPoolSize", "10");
	}

	private static void savePropertyFile(String odysseusHome) {
		FileOutputStream out;
		try {
			out = new FileOutputStream(odysseusHome + "odysseus.conf");
			props.storeToXML(out,
					"Odysseus Property File edit only if you know what you are doing");
			out.close();
			getLogger().info("New Odysseus-Config-File created");
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}

	public static String get(String key) {
		String ret = props.getProperty(key);
		if (ret == null) {
			getLogger().warn(
					"Try to get a property that is not registered " + key);
		}
		return ret;
	}

	public static long getLong(String key, long defaultValue) {
		String val = props.getProperty(key);
		return val != null ? Long.parseLong(val) : defaultValue;
	}
	
	public static int getInt( String key, int defaultValue ) {
		String val = props.getProperty(key);
		try {
			return val != null ? Integer.parseInt(val) : defaultValue;
		} catch ( Exception ex ) {
			_logger.error("Exception during getting value of configuration-key '{}'", key, ex);
			return defaultValue;
		}
	}

	public static boolean getBoolean(String key) {
		String val = props.getProperty(key);
		if (val != null){
			return Boolean.parseBoolean(val);
		}
		return false;
	}

	
	public static void set(String key, String value, boolean permanent,
			ISession caller) {

		if (UserManagement.getUsermanagement().hasPermission(caller,
				ConfigurationPermission.SET_PARAM, ConfigurationPermission.objectURI)) {
			props.setProperty(key, value);
			if (permanent) {
				if (UserManagement.getUsermanagement().hasPermission(caller,
						ConfigurationPermission.SAVE_PARAM, ConfigurationPermission.objectURI)) {
					savePropertyFile(homeDir);
				} else {
					throw new PermissionException(
							"User "
									+ caller
									+ " is not allowed to permanently set config param "
									+ key);
				}
			}
		} else {
			throw new PermissionException("User " + caller
					+ " is not allowed to temporally set config param " + key);
		}
	}

	public static String getHomeDir() {
		return homeDir;
	}

	/**
	 * Returns a dot on specific operating systems: unix,linux, and mac.
	 * 
	 */
	private static String getDot(String os) {
		os = os.toLowerCase();
		if ((os.indexOf("win") >= 0)) {
			// Windows
			getLogger().info("OS: Windows");
			return "";
		} else if ((os.indexOf("mac") >= 0)) {
			// Macintosh
			getLogger().info("OS: MacOS");
			return ".";
		} else if ((os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0)) {
			// Unix
			getLogger().info("OS: Unix/Linux");
			return ".";
		} else {
			// All other
			getLogger().info("OS: not Supported");
			return "";
		}
	}

}
