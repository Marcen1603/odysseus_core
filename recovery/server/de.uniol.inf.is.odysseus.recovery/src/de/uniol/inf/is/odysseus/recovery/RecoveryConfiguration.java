package de.uniol.inf.is.odysseus.recovery;

import java.io.File;

import de.uniol.inf.is.odysseus.core.config.OdysseusBaseConfiguration;

/**
 * Access to global recovery configurations.
 * 
 * @author Michael Brand
 *
 */
public class RecoveryConfiguration {

	/**
	 * The directory for recovery files.
	 */
	private static final File DIRECTORY = new File(OdysseusBaseConfiguration.getHomeDir() + "recovery");

	static {
		if (!DIRECTORY.isDirectory()) {
			DIRECTORY.mkdir();
		}
	}

	/**
	 * Gets the directory for recovery files.
	 */
	public static File getRecoveryDirectory() {
		return DIRECTORY;
	}

}