package de.uniol.inf.is.odysseus.wrapper.nmea.data;

/**
 * Enumeration type for temperature units. Also provides method to parse and
 * representation in nmea.
 * 
 * @author jboger <juergen.boger@offis.de>
 * 
 */
public enum TemperatureUnit {
	/** Celsius. */
	CELSIUS("C"),
	/** Celsius. */
	FAHRENHEIT("F"),
	/** Kelvin. */
	KELVIN("K"),
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
	TemperatureUnit(String shortName) {
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
	public static TemperatureUnit parse(String s) {
		for (TemperatureUnit r : TemperatureUnit.class.getEnumConstants()) {
			if (r.shortName.equals(s)) {
				return r;
			}
		}
		return NULL;
	}
}
