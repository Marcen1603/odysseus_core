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

	static Logger _logger = null;
	static Logger getLogger(){
		if (_logger == null){
			_logger = LoggerFactory.getLogger(OdysseusDefaults.class);
		}
		return _logger;
	}
	static Properties props = new Properties();

	private static String odysseusDefaultHome = String.format("%s/%sodysseus/", System.getProperty("user.home"),getDot(System.getProperty("os.name")));
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
				getLogger().debug("Created new File " + f.getAbsolutePath());
			}
		} else {
			getLogger().debug("Read from file " + f.getAbsolutePath());
		}
		return f;
	}

	static {
		try {
			homeDir = System.getenv("ODYSSEUS_HOME");
			if (homeDir == null || homeDir.length() == 0){
				homeDir = odysseusDefaultHome;
			}
			loadProperties(homeDir,"odysseus.conf", props);
			if (props.getProperty("storeUsers") == null) {
				getLogger().info("No Odysseus config found.");
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
		props.setProperty("storeReloadLog", Boolean.TRUE.toString());
		props.setProperty("reloadLogStoreFilename", odysseusHome + "reloadlog.store");
		props.setProperty("storeTenants", Boolean.FALSE.toString());
		props.setProperty("tenantsFilename", odysseusHome + "tenants.store");
		props.setProperty("userTenantFilename", odysseusHome
				+ "userTenant.store");
		props.setProperty("slasFilename", odysseusHome + "slas.store");

		props.setProperty("storeDataDictionary", Boolean.FALSE.toString());
		props.setProperty("defaultDataDictionaryName", "DEFAULT_DD");
		props.setProperty("streamDefinitionsFilename", odysseusHome
				+ "viewDefinitions.store");
		props.setProperty("streamOrViewFromUserFilename", odysseusHome
				+ "viewFromUser.store");
		props.setProperty("viewDefinitionsFilename", odysseusHome
				+ "logicalViewDefinitions.store");
		props.setProperty("sinkDefinitionsFilename", odysseusHome
				+ "sinkDefinitions.store");
		props.setProperty("sinkDefinitionsUserFilename", odysseusHome +
				"sinkDefinitionsUser.store");
		props.setProperty("entitiesFilename", odysseusHome + "entities.store");
		props.setProperty("sourceTypeMapFilename", odysseusHome
				+ "sourceTypeMap.store");
		props.setProperty("entityFromUserFilename", odysseusHome
				+ "entityFromUser.store");
		props.setProperty("sourceFromUserFilename", odysseusHome
				+ "sourceFromUser.store");
		props.setProperty("schedulingConfigFile", odysseusHome
				+ "scheduling.conf");
		props.setProperty("datatypesFromDatatypesFilename", odysseusHome + "datatypes.store");

		props.setProperty("sessionTimeout", (240 * 60000) + ""); // Milliseconds

		// Scheduling
		props.setProperty("debug_Scheduler", Boolean.FALSE.toString());
		props.setProperty("debug_Scheduler_maxLines", 1048476 + "");
		props.setProperty("scheduler_TimeSlicePerStrategy", 10 + "");
		props.setProperty("scheduler_DebugFileName","SchedulerLog");
		props.setProperty("scheduler_trainSize", 1 + "");

		// SLA
		props.setProperty("sla_history_size", 10000 + ""); // Milliseconds
		props.setProperty("sla_updatePenaltyTime", 60000+""); // Milliseconds
		
		// new SLA
		props.setProperty("sla_starvationFreedomFuncName", "QueueSizeSF");
		props.setProperty("sla_prioFuncName", "max");
		props.setProperty("sla_starvationFreedomDecay", 0.1 + "");
		props.setProperty("sla_querySharingCostModel", "none");
		props.setProperty("sla_querySharing", Boolean.FALSE.toString());
		props.setProperty("sla_costFunctionName", "quadratic");
		
		// Odysseus Storing 
		props.setProperty("storing_database", odysseusHome + "database.conf");
		
		// AC
		props.setProperty("ac_memHeadroom", "0.4");
		props.setProperty("ac_cpuHeadroom", "0.4");
		props.setProperty("ac_standardMemCost", "4");
		props.setProperty("ac_standardCpuCost", "0.00002");
	};

	private static void savePropertyFile(String odysseusHome) {
		FileOutputStream out;
		try {
			out = new FileOutputStream(odysseusHome + "odysseus.conf");
			props.store(out,
					"--- Odysseus Property File edit only if you know what you are doing ---");
			out.close();
			getLogger().info("New Odysseus-Config-File created");
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

	/**
	 * Returns a dot on specific operating systems: unix,linux, and mac.
	 * 
	 */
	private static String getDot(String os){
		os = os.toLowerCase();
		if((os.indexOf( "win" ) >= 0)){
			//Windows
			getLogger().info("OS: Windows");
			return "";
		}else if((os.indexOf( "mac" ) >= 0)){
			//Macintosh 
			getLogger().info("OS: MacOS");
			return ".";
		}else if((os.indexOf( "nix") >=0 || os.indexOf( "nux") >=0)){
			//Unix
			getLogger().info("OS: Unix/Linux");
			return ".";
		}else{
			//All other
			getLogger().info("OS: not Supported");
			return "";
		}
	}
}
