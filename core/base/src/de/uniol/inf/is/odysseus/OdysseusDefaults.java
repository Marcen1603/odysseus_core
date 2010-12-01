package de.uniol.inf.is.odysseus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.swing.AccessibleMethod;

import de.uniol.inf.is.odysseus.usermanagement.AccessControl;
import de.uniol.inf.is.odysseus.usermanagement.HasNoPermissionException;
import de.uniol.inf.is.odysseus.usermanagement.User;

public class OdysseusDefaults {

	static Logger logger = LoggerFactory.getLogger(OdysseusDefaults.class);
	static Properties props = new Properties();

	public static String odysseusHome = System.getProperty("user.home")
			+ "/odysseus/";

	static public File openOrCreateFile(String path) throws IOException {
		File f = new File(path);
		if (!f.exists()) {
			File d = f.getParentFile();
			if (d != null) {
				d.mkdirs();
			}
			f.createNewFile();
			logger.debug("Created new File " + f.getAbsolutePath());
		} else {
			logger.debug("Read from file " + f.getAbsolutePath());
		}
		return f;
	}

	static {
		try {
			// TODO: Odysseus-Home configurierbar machen z.B. ueber
			// Umgebungsvariable

			loadProperties(odysseusHome);
			if (props.getProperty("storeUsers") == null) {
				logger.info("No Odysseus config found.");
				setDefaults(odysseusHome);
				savePropertyFile(odysseusHome);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void loadProperties(String odysseusHome) throws IOException,
			FileNotFoundException {
		File f = openOrCreateFile(odysseusHome + "odysseus.conf");
		FileInputStream in;
		in = new FileInputStream(f);
		props.load(in);
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

		props.setProperty("sessionTimeout", (240 * 60000)+""); // Milliseconds

		// SLA
		props.setProperty("sla_history_size", 10000+""); // Milliseconds
		props.setProperty("sla_update_Penalties_Frequency", 30000+"");
		props.setProperty("sla_debug_TimebasedSLAScheduler", Boolean.TRUE.toString());
	}

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

	public static void set(String key, String value, boolean permanent, User caller) {
		if (AccessControl.hasPermission(ConfigurationAction.SET_PARAM, ConfigurationAction.alias, caller)){			
			props.setProperty(key, value);
			if (permanent) {
				if (AccessControl.hasPermission(ConfigurationAction.SAVE_PARAM, ConfigurationAction.alias, caller)){			
					savePropertyFile(odysseusHome);
				}else{
					throw new HasNoPermissionException("User "+caller+" is not allowed to permanently set config param "+key);					
				}
			}
		}else{
			throw new HasNoPermissionException("User "+caller+" is not allowed to temporally set config param "+key);
		}
	}

}
