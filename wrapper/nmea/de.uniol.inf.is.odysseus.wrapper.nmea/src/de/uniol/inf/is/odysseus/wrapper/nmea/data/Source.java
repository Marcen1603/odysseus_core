package de.uniol.inf.is.odysseus.wrapper.nmea.data;

/**
 * Enumeration type for sources. Also provides method to parse and representation
 * in nmea.
 * 
 * @author jboger <juergen.boger@offis.de>
 * 
 */
public enum Source {
	/** Shaft. */
	SHAFT("S"),
	/** Engine. */
	ENGINE("E"),
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
	Source(String shortName) {
		this.shortName = shortName;
	}

	/**
	 * Gets the nmea representation of this enum.
	 * 
	 * @return Nmea String.
	 */
	public String getShortName() {
		return shortName;
	}

	/**
	 * Parses the given String and returns the enum.
	 * 
	 * @param s
	 *            Nmea String.
	 * @return Enum for the given String.
	 */
	public static Source parse(String s) {
		for (Source r : Source.class.getEnumConstants()) {
			if (r.shortName.equals(s)) {
				return r;
			}
		}
		return NULL;
	}
}
