package de.uniol.inf.is.odysseus;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OdysseusDefaults {
	
	static Logger logger = LoggerFactory.getLogger(OdysseusDefaults.class);
	
	public static String odysseusHome = System.getProperty("user.home")+"/odysseus/";
	public static boolean storeUsers = false;
	public static String userStoreFilename = odysseusHome+"users.store";
	public static boolean storeTenants = false;
	public static String tenantsFilename = odysseusHome+"tenants.store";
	public static String userTenantFilename = odysseusHome+"userTenant.store";
	public static String slasFilename = odysseusHome+"slas.store";
	
	public static boolean storeDataDictionary = false;
	public static String streamDefinitionsFilename = odysseusHome+"viewDefinitions.store";
	public static String streamOrViewFromUserFilename = odysseusHome+"viewFromUser.store";
	public static String viewDefinitionsFilename = odysseusHome+"logicalViewDefinitions.store";
	public static String entitiesFilename = odysseusHome+"entities.store";
	public static String sourceTypeMapFilename = odysseusHome+"sourceTypeMap.store";
	public static String entityFromUserFilename = odysseusHome+"entityFromUser.store";
	public static String sourceFromUserFilename = odysseusHome+"sourceFromUser.store";

	public static String schedulingConfigFile = odysseusHome+"scheduling.conf";

	public static long sessionTimeout = 240*60000; // Milliseconds
	
	static public File openOrCreateFile(String path) throws IOException{
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

}
