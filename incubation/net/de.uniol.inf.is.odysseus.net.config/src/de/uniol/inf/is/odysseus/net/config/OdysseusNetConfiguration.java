package de.uniol.inf.is.odysseus.net.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;

public class OdysseusNetConfiguration {
	
	private static final Logger LOG = LoggerFactory.getLogger(OdysseusNetConfiguration.class);
	
	private static final String ODYSSEUS_HOME_ENV = "ODYSSEUS_HOME";
	public static final String ODYSSEUS_DEFAULT_HOME_DIR = determineOdysseusDefaultHome();
	public static final String ODYSSEUS_HOME_DIR = determineOdysseusHome();
	public static final String ODYSSEUS_NET_CONFIGURATION_FILE = ODYSSEUS_HOME_DIR + "odysseusNet.conf";

	private static final Properties properties = new Properties();
	private static boolean isFirstLoaded = false;

	private OdysseusNetConfiguration() {
		// no instance allowed
	}
	
	private static String determineOdysseusDefaultHome() {
		return String.format("%s" + File.separator + "%sodysseus" + File.separator, System.getProperty("user.home"), getDot(System.getProperty("os.name")));
	}
	
	private static String getDot(String os) {
		os = os.toLowerCase();
		if ((os.indexOf("win") >= 0)) {
			return "";
		} else if ((os.indexOf("mac") >= 0)) {
			return ".";
		} else if ((os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0)) {
			return ".";
		} else {
			return "";
		}
	}

	private static String determineOdysseusHome() {
		try {
			String homeDir = System.getenv(ODYSSEUS_HOME_ENV);
			if (Strings.isNullOrEmpty(homeDir)) {
				return ODYSSEUS_DEFAULT_HOME_DIR;
			}
			return homeDir;
		} catch (Exception e) {
			return ODYSSEUS_DEFAULT_HOME_DIR;
		}
	}

	private static void loadConfiguration() {
		try {
			File confFile = openOrCreateFile(ODYSSEUS_NET_CONFIGURATION_FILE);
			
			FileInputStream in = new FileInputStream(confFile);
			try {
				try {
					properties.loadFromXML(in);
				} catch( Throwable t ) {
					LOG.warn("Could not load odysseus net configuration contents from file '{}'", confFile.getName(), t);
				}
				
			} finally {
				in.close();
				
				LOG.info("Loaded odysseus net configuration file");
			}
		} catch( InvalidPropertiesFormatException ex ) {
			LOG.warn("Could not load configuration file '" + ODYSSEUS_NET_CONFIGURATION_FILE + "'", ex);
		} catch (IOException ex) {
			LOG.warn("Could not load configuration file '" + ODYSSEUS_NET_CONFIGURATION_FILE + "'", ex);
		} 
	}

	public static void save() {
		try {
			FileOutputStream out = new FileOutputStream(ODYSSEUS_NET_CONFIGURATION_FILE);
			try {
				properties.storeToXML(out, "OdysseusNet Property File edit only if you know what you are doing");
			} finally {
				try {
					out.close();
				} catch (IOException ex) {
				}
			}
			
		} catch (IOException ex) {
			LOG.error("Could not save odysseus net configuration file '{}'", ODYSSEUS_NET_CONFIGURATION_FILE, ex);
		} 
	}
	
	private static File openOrCreateFile(String path) throws IOException {
		File f = new File(path);
		boolean success = false;
		if (!f.exists()) {
			File d = f.getParentFile();
			if (d != null) {
				success = d.mkdirs();
			}
			success = f.createNewFile();
			if (success) {
				LOG.debug("Created new odysseus net configuration file " + f.getAbsolutePath());
			}
		} else {
			LOG.debug("Read from file " + f.getAbsolutePath());
		}
		return f;
	}

	public static void set(String key, String value) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(key), "SettingKey must not be null or empty!");

		loadFirstIfNeeded();
		
		String oldValue = (String)properties.get(key);
		if (oldValue == null || !oldValue.equals(value)) {
			properties.put(key, value);
			save();
		}
	}

	public static Optional<String> get(String key) {
		loadFirstIfNeeded();
		
		Object value = properties.get(key);
		if( value == null ) {
			// backwards compatible
			value = OdysseusConfiguration.get(key);
			
			if( value != null ) {
				set(key, (String)value);
			}
		}
		
		return Optional.fromNullable((String)value);
	}

	private static void loadFirstIfNeeded() {
		if( !isFirstLoaded ) {
			loadConfiguration();
			isFirstLoaded = true;
		}
	}

	public static String get(String key, String defaultValue) {
		Optional<String> optValue = get(key);
		if( !optValue.isPresent() ) {
			set(key, defaultValue);
			return defaultValue;
		}
		
		return optValue.get();
	}

	public static boolean exists(String key) {
		loadFirstIfNeeded();

		return properties.containsKey(key);
	}
	
	public static Collection<String> getKeys() {
		loadFirstIfNeeded();
		List<String> keys = Lists.newArrayList();
		for( Object key : properties.keySet() ) {
			keys.add(key.toString());
		}
		return keys;
	}
}