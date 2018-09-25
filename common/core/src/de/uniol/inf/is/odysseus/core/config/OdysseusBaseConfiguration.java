package de.uniol.inf.is.odysseus.core.config;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Basis configuration things (attention a clone of this file is placed
 * under de.uniol.inf.is.odysseus.rcp.config, so any change need to be 
 * done there two!)
 * 
 * @author Marco Grawunder
 *
 */
public class OdysseusBaseConfiguration {
	
	private static final Logger LOG = LoggerFactory
			.getLogger(OdysseusBaseConfiguration.class);

	// TODO: Make Platform specific homedir
	private static String odysseusDefaultHome = String.format("%s"
			+ File.separator + "%sodysseus" + File.separator,
			System.getProperty("user.home"),
			getDot(System.getProperty("os.name")));
	private static String homeDir;

	static {
		homeDir = System.getProperty("ODYSSEUS_HOME");
		if (homeDir == null || homeDir.length() == 0) {
			homeDir = System.getenv("ODYSSEUS_HOME");
		}
		if (homeDir == null || homeDir.length() == 0) {
			homeDir = odysseusDefaultHome;
		}
	}

	public static String getHomeDir(){
		return homeDir;
	}
	
	/**
	 * Returns a URL for the specified path in the Odysseus home directory
	 * @param path the path in the Odysseus home
	 * @return A URL representing the given path, or null if the entry does not exist
	 */
	public static URL getEntry(String path){
		URL url = null;
		File homeDir = new File(getHomeDir());
		
		File fullPath = new File(homeDir, path);
		try {
			url = fullPath.toURI().toURL();
		} catch (MalformedURLException e) {
			LOG.error(e.getMessage());
		}
		return url;
	}
	
	/**
	 * Returns a dot on specific operating systems: unix,linux, and mac.
	 * 
	 */
	private static String getDot(String os) {
		os = os.toLowerCase();
		if ((os.indexOf("win") >= 0)) {
			// Windows
			LOG.trace("OS: Windows");
			return "";
		} else if ((os.indexOf("mac") >= 0)) {
			// Macintosh
			LOG.trace("OS: MacOS");
			return ".";
		} else if ((os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0)) {
			// Unix
			LOG.trace("OS: Unix/Linux");
			return ".";
		} else {
			// All other
			LOG.error("OS: not Supported");
			return "";
		}
	}
	
}
