package de.uniol.inf.is.odysseus.wrapper.nmea.data;

/**
 * Enumeration type for status. Also provides method to parse and representation
 * in nmea.
 * 
 * @author jboger <juergen.boger@offis.de>
 * 
 */
public enum Status {
	/** OK */
	OK("A"),
	/** Warning */
	WARNING("V"),
	/** Invalid or not set. */
	NULL("");

	/** Stores the nmea representation. */
	private String shortName;

	/**
	 * Constructor.
	 * 
	 * @param shortName
	 *            Nmea representation.
	 */
	Status(String shortName) {
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
	public static Status parse(String s) {
		if (s.equals("A")) {
			return OK;
		} else if (s.equals("V")) {
			return WARNING;
		} else {
			return NULL;
		}
	}
}
