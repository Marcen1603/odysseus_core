package de.uniol.inf.is.odysseus.wrapper.nmea.data;

/**
 * Enumeration type for speed units. Also provides method to parse and representation
 * in nmea.
 * 
 * @author jboger <juergen.boger@offis.de>
 * 
 */
public enum SpeedUnit {
	/** Meters per second. */
	METERS_PER_SECOND("M"),
	/** Kilometers per Hour. */
	KILOMETERS_PER_HOUR("K"),
	/** Statute miles per hour. */
	STATUTE_MILES_PER_HOUR("S"),
	/** Knots. */
	KNOTS("N"),
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
	SpeedUnit(String shortName) {
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
	public static SpeedUnit parse(String s) {
		for (SpeedUnit r : SpeedUnit.class.getEnumConstants()) {
			if (r.shortName.equals(s)) {
				return r;
			}
		}
		return NULL;
	}
}
