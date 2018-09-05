package de.uniol.inf.is.odysseus.wrapper.nmea.data;

/**
 * Enumeration type for units. Also provides method to parse and representation
 * in nmea.
 * 
 * @author jboger <juergen.boger@offis.de>
 * 
 */
public enum Unit {
	/** Meters. */
	METERS("M"),
	/** Kilometers. */
	KILOMETERS("K"),
	/** Kilometers. */
	STATUTE_MILES("S"),
	/** Kilometers. */
	NAUTICAL_MILES("N"),
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
	Unit(String shortName) {
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
	public static Unit parse(String s) {
		for (Unit r : Unit.class.getEnumConstants()) {
			if (r.shortName.equals(s)) {
				return r;
			}
		}
		return NULL;
	}
}
