package de.uniol.inf.is.odysseus.recovery.incomingelements.badast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.config.OdysseusBaseConfiguration;

/**
 * Access to the configuration of the BaDaSt application
 * (OdysseusHome/badast.conf").
 * 
 * @author Michael Brand
 *
 */
@SuppressWarnings(value = { "nls" })
public class BaDaStConfiguration {

	/**
	 * The logger for this class.
	 */
	private static final Logger cLog = LoggerFactory.getLogger(BaDaStConfiguration.class);

	/**
	 * The name of the configuration file.
	 */
	private static final String cFileName = OdysseusBaseConfiguration.getHomeDir() + "badast.conf";

	/**
	 * The BaDaSt configuration.
	 */
	private static Properties cConfig = new Properties();

	/**
	 * Gets the configuration.
	 * 
	 * @return All entries as key value pairs (string, string).
	 */
	public static Properties get() {
		return cConfig;
	}

	/**
	 * Adds a configuration entry.
	 * 
	 * @param key
	 *            The key for the entry.
	 * @param value
	 *            The value for the entry.
	 */
	public static void add(String key, String value) {
		cConfig.put(key, value);
		saveConfiguration(cFileName);
	}

	// Load the configuration once.
	static {
		loadConfiguration(cFileName);
	}

	/**
	 * Loads the default configuration and overrides it with properties in the
	 * badast configuration file. Saves it to the file.
	 * 
	 * @param fileName
	 *            The name of the badast configuration file.
	 */
	private static void loadConfiguration(String fileName) {
		setDefaults();

		File file = new File(fileName);
		try {
			if (file.createNewFile()) {
				cLog.debug("Created new file " + file.getAbsolutePath());
				saveConfiguration(fileName);
				return;
			}
		} catch (IOException e) {
			cLog.error("Could not create file " + file.getAbsolutePath(), e);
		}

		// File already existed
		@SuppressWarnings("resource")
		FileInputStream stream = null;
		try {
			stream = new FileInputStream(file);
			cConfig.loadFromXML(stream);
		} catch (@SuppressWarnings("unused") IOException e) {
			try {
				cConfig.load(stream);
			} catch (IOException e1) {
				cLog.error("Could not load BaDaSt config from file " + fileName, e1);
			}
		} finally {
			saveConfiguration(fileName);
			try {
				if (stream != null) {
					stream.close();
				}
			} catch (IOException e1) {
				cLog.error("Could not close file stream!", e1);
			}
		}
	}

	/**
	 * Gets a part of the configuration.
	 * 
	 * @param prefix
	 *            The prefix of the wanted configuration entries, e.g.,
	 *            "badast.".
	 * @return All configurations for the wanted entity (keys without the
	 *         prefix).
	 */
	public static Properties getConfig(String prefix) {
		Properties out = new Properties();
		for (Object key : cConfig.keySet()) {
			String strKey = (String) key;
			if (strKey.startsWith(prefix)) {
				out.put(strKey.substring(prefix.length()), cConfig.get(strKey));
			}
		}
		return out;
	}

	/**
	 * Saves {@link #cConfig} to a given file (xml).
	 * 
	 * @param fileName
	 *            The given file.
	 */
	private static void saveConfiguration(String fileName) {
		try (FileOutputStream out = new FileOutputStream(fileName)) {
			cConfig.storeToXML(out, "BaDaSt Property File edit only if you know what you are doing");
		} catch (Exception e) {
			cLog.error("Could not save BaDaSt config to file!", e);
		}
	}

	/**
	 * Sets all default values for a BaDaSt server. All keys will begin with
	 * "badast.".
	 */
	private static void setDefaults() {
		// the name of the badast host
		cConfig.put("badast.host.name", "localhost");
		// the port at which the clients will connect
		cConfig.put("badast.clientPort", "6789");
	}

	/**
	 * Gets the BaDaSt configuration.
	 * 
	 * @return All values for a BaDaSt server.
	 */
	public static Properties getBaDaStConfig() {
		return getConfig("badast.");
	}

}