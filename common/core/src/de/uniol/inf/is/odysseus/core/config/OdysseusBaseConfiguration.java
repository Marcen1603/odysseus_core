package de.uniol.inf.is.odysseus.core.config;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OdysseusBaseConfiguration {
	
	private static final Logger LOG = LoggerFactory
			.getLogger(OdysseusBaseConfiguration.class);

	// TODO: Make Platform specific homedir
	private static String odysseusDefaultHome = String.format("%s"
			+ File.separator + "%sodysseus" + File.separator,
			System.getProperty("user.home"),
			getDot(System.getProperty("os.name")));
	public static String homeDir;

	static {
		homeDir = System.getProperty("ODYSSEUS_HOME");
		if (homeDir == null || homeDir.length() == 0) {
			homeDir = System.getenv("ODYSSEUS_HOME");
		}
		if (homeDir == null || homeDir.length() == 0) {
			homeDir = odysseusDefaultHome;
		}
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
