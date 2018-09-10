package de.uniol.inf.is.odysseus.rcp.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;


public final class OdysseusRCPConfiguration {

	private static final Logger LOG = LoggerFactory.getLogger(OdysseusRCPConfiguration.class);
	
	public static final String ODYSSEUS_RCP_CONFIGURATION_FILE = OdysseusBaseConfiguration.getHomeDir() + "odysseusRCP.conf";

	private static final Properties properties = new Properties();
	private static boolean isFirstLoaded = false;

	private OdysseusRCPConfiguration() {
		// no instance allowed
	}
	
	private static void loadConfiguration() {
		try {
			LOG.info("Loading rcp configurations from "+ODYSSEUS_RCP_CONFIGURATION_FILE);

			File confFile = openOrCreateFile(ODYSSEUS_RCP_CONFIGURATION_FILE);
			
			FileInputStream in = new FileInputStream(confFile);
			try {
				properties.loadFromXML(in);
			} finally {
				in.close();
			} 
			LOG.info("Loadeding rcp configurations done");
		} catch (IOException ex) {
			LOG.error("Could not load configuration file '" + ODYSSEUS_RCP_CONFIGURATION_FILE + "'", ex);
		}
	}

	public static void save() {
		try {
			FileOutputStream out = new FileOutputStream(ODYSSEUS_RCP_CONFIGURATION_FILE);
			try {
				properties.storeToXML(out, "Odysseus Property File edit only if you know what you are doing");
			} finally {
				try {
					out.close();
				} catch (IOException ex) {
				}
			}
			
		} catch (IOException ex) {
			LOG.error("Could not save configuration file '" + ODYSSEUS_RCP_CONFIGURATION_FILE + "'", ex);
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
				LOG.debug("Created new File " + f.getAbsolutePath());
			}
		} else {
			LOG.debug("Read from file " + f.getAbsolutePath());
		}
		return f;
	}

	public static void set(String key, String value) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(key), "SettingKey must not be null or empty!");

		firstLoadIfNeeded();
		
		String oldValue = (String)properties.get(key);
		if (oldValue == null || !oldValue.equals(value)) {
			properties.put(key, value);
			save();
		}
	}

	public static String get(String key) throws OdysseusRCPConfiguartionException {
		firstLoadIfNeeded();
		
		if (exists(key)) {
			return (String)properties.get(key);
		}

		throw new OdysseusRCPConfiguartionException("Settings with key '" + key + "' does not exist!");
	}

	private static void firstLoadIfNeeded() {
		if( !isFirstLoaded ) {
			loadConfiguration();
			isFirstLoaded = true;
		}
	}

	public static String get(String key, String defaultValue) {
		try {
			return get(key);
		} catch (OdysseusRCPConfiguartionException e) {
			return defaultValue;
		}
	}

	public static boolean exists(String key) {
		firstLoadIfNeeded();

		return properties.containsKey(key);
	}
	
	public static Collection<String> getKeys() {
		firstLoadIfNeeded();
		List<String> keys = Lists.newArrayList();
		for( Object key : properties.keySet() ) {
			keys.add(key.toString());
		}
		return keys;
	}
}
