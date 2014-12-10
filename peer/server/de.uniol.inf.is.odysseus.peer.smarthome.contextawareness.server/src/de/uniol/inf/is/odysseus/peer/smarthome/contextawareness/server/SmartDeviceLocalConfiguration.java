package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Hashtable;
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
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.SmartHomeServerPlugIn;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.config.SmartDeviceConfig;

public class SmartDeviceLocalConfiguration {
	private static final Logger LOG = LoggerFactory
			.getLogger(SmartHomeServerPlugIn.class);

	private static final String ODYSSEUS_HOME_ENV = "ODYSSEUS_HOME";
	public static final String ODYSSEUS_DEFAULT_HOME_DIR = determineOdysseusDefaultHome();
	public static final String ODYSSEUS_HOME_DIR = determineOdysseusHome();
	public static String SMART_HOME_CONFIG_FILE = "odysseusSmartDevice.conf";
	public static String ODYSSEUS_RCP_CONFIGURATION_FILE = ODYSSEUS_HOME_DIR
			+ SMART_HOME_CONFIG_FILE;

	private static final Properties properties = new Properties();
	private static boolean isFirstLoaded = false;

	public static final String SMART_DEVICE_KEY_CONTEXT_NAME = "smartdevice.contextname";

	private SmartDeviceLocalConfiguration() {
		// no instance allowed
	}

	private static String determineOdysseusDefaultHome() {
		return String.format("%s" + File.separator + "%sodysseus"
				+ File.separator, System.getProperty("user.home"),
				getDot(System.getProperty("os.name")));
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
			File confFile = openOrCreateFile(ODYSSEUS_RCP_CONFIGURATION_FILE);

			FileInputStream in = new FileInputStream(confFile);
			try {
				setDefaultValues(properties);
				properties.loadFromXML(in);
				save();
			} finally {
				in.close();
			}
		} catch (InvalidPropertiesFormatException ex) {
			LOG.warn("Could not load configuration file '"
					+ ODYSSEUS_RCP_CONFIGURATION_FILE + "'", ex);
		} catch (IOException ex) {
			LOG.warn("Could not load configuration file '"
					+ ODYSSEUS_RCP_CONFIGURATION_FILE + "'", ex);
		}
	}

	@SuppressWarnings("unused")
	private static void setDefaultValues(Hashtable<Object, Object> props) {
		setDefaultValue(SMART_DEVICE_KEY_CONTEXT_NAME, "");

	}

	private static void setDefaultValue(String key, String defaultValue) {
		if (OdysseusConfiguration.exists(key)) {
			String valueFromOdysseusConfig = OdysseusConfiguration.get(key);
			if (!Strings.isNullOrEmpty(valueFromOdysseusConfig)) {
				properties.put(key, valueFromOdysseusConfig);
			} else {
				properties.put(key, defaultValue);
			}
		} else {
			properties.put(key, defaultValue);
		}
	}

	public static void save() {
		try {
			FileOutputStream out = new FileOutputStream(
					ODYSSEUS_RCP_CONFIGURATION_FILE);
			try {
				properties
						.storeToXML(out,
								"Odysseus Property File edit only if you know what you are doing");
			} finally {
				try {
					out.close();
				} catch (IOException ex) {
				}
			}

		} catch (IOException ex) {
			LOG.error("Could not save configuration file '"
					+ ODYSSEUS_RCP_CONFIGURATION_FILE + "'", ex);
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
		Preconditions.checkArgument(!Strings.isNullOrEmpty(key),
				"SettingKey must not be null or empty!");

		firstLoadIfNeeded();

		String oldValue = (String) properties.get(key);
		if (oldValue == null || !oldValue.equals(value)) {
			properties.put(key, value);
			save();
		}
	}

	public static Optional<String> get(String key) {
		firstLoadIfNeeded();

		Object value = properties.get(key);
		if (value == null) {
			// backwards compatible
			value = OdysseusConfiguration.get(key);

			if (value != null) {
				set(key, (String) value);
			}
		}

		return Optional.fromNullable((String) value);
	}

	private static void firstLoadIfNeeded() {
		if (!isFirstLoaded) {
			loadConfiguration();
			isFirstLoaded = true;
		}
	}

	public static String get(String key, String defaultValue) {
		Optional<String> optValue = get(key);
		return optValue.isPresent() ? optValue.get() : defaultValue;
	}

	public static boolean exists(String key) {
		firstLoadIfNeeded();

		return properties.containsKey(key);
	}

	public static Collection<String> getKeys() {
		firstLoadIfNeeded();
		List<String> keys = Lists.newArrayList();
		for (Object key : properties.keySet()) {
			keys.add(key.toString());
		}
		return keys;
	}

	public static void setSmartDeviceConfig(String configFile,
			SmartDeviceConfig smartDeviceConfig) {
		SMART_HOME_CONFIG_FILE = configFile;
		set(SMART_DEVICE_KEY_CONTEXT_NAME, smartDeviceConfig.getContextname());
		// ...

	}

	public static SmartDeviceConfig getSmartDeviceConfig(String configFile) {
		SMART_HOME_CONFIG_FILE = configFile;

		SmartDeviceConfig sdConfig = new SmartDeviceConfig();
		sdConfig.setContextname(get(SMART_DEVICE_KEY_CONTEXT_NAME, ""));
		// ...

		return sdConfig;
	}
}
