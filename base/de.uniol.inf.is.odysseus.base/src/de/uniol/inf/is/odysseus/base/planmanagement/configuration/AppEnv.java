package de.uniol.inf.is.odysseus.base.planmanagement.configuration;

/**
 * This object provides some static informations about the application
 * environment. Used to provide a more comfortable way to access system
 * properties.
 * 
 * @author Wolf Bauer
 * 
 */
public class AppEnv {
	/**
	 * Returns the string which represents a line separator on the execution system.
	 */
	public static final String LINE_SEPARATOR = System
			.getProperty("line.separator");
}
