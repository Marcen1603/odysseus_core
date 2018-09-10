package de.uniol.inf.is.odysseus.wrapper.urg.utils;

public class Constants {
	/** Maximal length of the message string. */
	public static final int MAX_MESSAGE_LENGTH = 16;
	
	/** Line feed. */
	public static final String LF = "\n";
	
	/** Command to enable the laser beam. */
	public static final String ENABLE_COMMAND = "BM";
	
	/** Command to disable the laser beam. */
	public static final String DISABLE_COMMAND = "QT";

	/** Command to start the measurement. */
	public static final String START_COMMAND = "MD";
	
	/** Default start step. */
	public static final int DEFAULT_START_STEP = 44;
	
	/** Default end step. */
	public static final int DEFAULT_END_STEP = 725;
}
