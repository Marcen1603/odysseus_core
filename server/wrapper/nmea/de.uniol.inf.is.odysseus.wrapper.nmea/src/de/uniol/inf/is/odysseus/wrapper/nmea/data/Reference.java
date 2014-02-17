package de.uniol.inf.is.odysseus.wrapper.nmea.data;

/**
 * Enumeration type for relative reference measurements. Also provides method to
 * parse and representation in nmea.
 * 
 * @author jboger <juergen.boger@offis.de>
 * 
 */
public enum Reference {
	/** True value. */
	TRUE("T"),
	/** Relative value. */
	RELATIVE("R"),
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
	Reference(String shortName) {
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
	public static Reference parse(String s) {
		for (Reference r : Reference.class.getEnumConstants()) {
			if (r.shortName.equals(s)) {
				return r;
			}
		}
		return NULL;
	}
}
