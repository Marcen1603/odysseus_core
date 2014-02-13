package de.uniol.inf.is.odysseus.wrapper.nmea.data;

/**
 * Enumeration type for signal integrity. Also provides method to parse and
 * representation in nmea.
 * 
 * @author jboger <juergen.boger@offis.de>
 * 
 */
public enum SignalIntegrity {
	/** Autonomous mode. */
	AUTONOMOUS_MODE("A"),
	/** Differential mode. */
	DIFFERENTIAL_MODE("D"),
	/** Estimated mode. */
	ESTIMATED_MODE("E"),
	/** Manual input mode. */
	MANUAL_INPUT_MODE("M"),
	/** Simulated mode. */
	SIMULATED_MODE("S"),
	/** Not valid. */
	NOT_VALID("N"),
	/** Not set. */
	NULL("");

	/** Stores the nmea representation. */
	private String shortName;

	/**
	 * Constructor.
	 * 
	 * @param shortName
	 *            Nmea representation.
	 */
	SignalIntegrity(String shortName) {
		this.shortName = shortName;
	}

	/**
	 * Gets the nmea representation of this enum.
	 * @return
	 * Nmea String.
	 */
	public String getShortName() {
		return shortName;
	}

	/**
	 * Parses the given String and returns the enum.
	 * @param s
	 * Nmea String.
	 * @return
	 * Enum for the given String.
	 */
	public static SignalIntegrity parse(String s) {
		if (s.equals("A")) {
			return AUTONOMOUS_MODE;
		} else if (s.equals("D")) {
			return DIFFERENTIAL_MODE;
		} else if (s.equals("E")) {
			return ESTIMATED_MODE;
		} else if (s.equals("M")) {
			return MANUAL_INPUT_MODE;
		} else if (s.equals("S")) {
			return SIMULATED_MODE;
		} else if (s.equals("N")) {
			return NOT_VALID;
		} else {
			return NULL;
		}
	}
}
