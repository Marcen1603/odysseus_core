/**
 * Dieses Package beinhaltet die Caching Engine
 */
package mg.dynaquest.queryexecution.caching;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * 
 * Diese Klasse beinhaltet den Cache Manager. Sie implementiert ein thread-safe
 * Singleton Pattern und wird von den the Caching POs durch Lazy Loading
 * instantiiert.
 * 
 * @author Tobias Hesselmann
 * 
 */
public class CacheManager {

	/* Global configuration parameters */
	private Properties properties;
	
    String caching_enabled = null;
	String caching_jdbcstring = null;
	String caching_db_user = null;
	String caching_db_password = null;
	String caching_sqlfile = null;
	String caching_driverClass = null;
	int caching_tupleCapacity;
	int caching_invalidationTimeoutInSeconds; 
	
	/* Logger instance */
	private Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * 
	 * @return true, wenn Caching aktiviert ist
	 */
	public boolean isCachingEnabled() {
		return Boolean.valueOf(this.caching_enabled);
	}

	/**
	 * Holt Konfiguration für Caching-Engine aus dem existierenden DynaQuest
	 * Property File
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 * 
	 */
	private synchronized void loadConfiguration() throws FileNotFoundException, IOException {

		String propFile = System.getProperty("user.home")
				+ "/DynaQuest.caching.properties";
		properties = new Properties();
		// Properties-Datei
		try {
			properties.load(new FileInputStream(propFile));
		} catch (FileNotFoundException e) {
			// Dann neue Properties-Datei mit Default-Werten anlegen
			properties.put("caching_invalidationTimeoutInSeconds","360"); // 10 Minuten 
			properties.put("caching_tupleCapacity", "10000"); // 1000 Datensätze
			properties.put("caching_enabled", "false");
			properties.put("caching_db_user", "dynacache");
			properties.put("caching_db_password", "99PyvqsemyEh9BH9");
			properties.put("caching_jdbcString",
					"jdbc:mysql://134.106.52.241:3306/dynacache");
			properties.put("caching_driverClass", "com.mysql.jdbc.Driver");
			properties
					.put("caching_sqlfile",
							"e:/Development/DynaQuest/DynaQuest/src/sql/createCachingSchemeMySQL.sql");
			properties.store(new FileOutputStream(propFile),
					"Created by caching engine");
			logger.debug("Caching Property File erzeugt");
		}

		this.caching_enabled = properties.getProperty("caching_enabled");
		this.caching_db_user = properties.getProperty("caching_db_user");
		this.caching_db_password = properties
				.getProperty("caching_db_password");
		this.caching_jdbcstring = properties.getProperty("caching_jdbcString");
		this.caching_driverClass = properties
				.getProperty("caching_driverClass");
		this.caching_sqlfile = properties.getProperty("caching_sqlfile");
		this.caching_tupleCapacity = Integer.valueOf(properties.getProperty("caching_tupleCapacity"));
		this.caching_invalidationTimeoutInSeconds = Integer.valueOf(properties.getProperty("caching_invalidationTimeoutInSeconds"));
	}

	/*
	 * Private constructor suppresses generation of a (public) default
	 * constructor
	 */
	private CacheManager() {

		/* Load caching configuration from property file */
		try {
			this.loadConfiguration();
		} catch (FileNotFoundException e) {
			logger.error("Error openening caching property file");
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("Error reading caching configuration");
			e.printStackTrace();
		}
		
	}

	private static class CacheManagerHolder {
		private final static CacheManager INSTANCE = new CacheManager();
	}

	public static CacheManager getInstance() {
		return CacheManagerHolder.INSTANCE;
	}

}
