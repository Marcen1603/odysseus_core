package de.uniol.inf.is.odysseus.wrapper.nmea.data;

/**
 * Enumeration type for gps quality. Also provides method to parse and representation
 * in nmea.
 * 
 * @author jboger <juergen.boger@offis.de>
 * 
 */
public enum GpsQuality {
	/** Invalid. */
	INVALID(0),
	/** GPS. */
	GPS(1),
	/** DGPS. */
	DGPS(2),
	/** Estimate. */
	ESTIMATE(6);
	
	/** Stores the nmea representation. */
	private int value;
	
	/**
	 * Constructor.
	 * 
	 * @param value
	 *            Nmea representation.
	 */
	GpsQuality(int value) {
		this.value = value;
	}
	
	/**
	 * Gets the nmea representation of this enum.
	 * @return
	 * Nmea String.
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * Parses the given String and returns the enum.
	 * @param s
	 * Nmea String.
	 * @return
	 * Enum for the given String.
	 */
	public static GpsQuality parse(String s) {
		for (GpsQuality e : GpsQuality.class.getEnumConstants()) {
			if (s.equals(String.valueOf(e.value))) {
				return e;
			}
		}
		return INVALID;
	}
}
