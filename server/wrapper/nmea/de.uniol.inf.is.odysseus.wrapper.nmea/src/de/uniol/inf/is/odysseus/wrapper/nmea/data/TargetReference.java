package de.uniol.inf.is.odysseus.wrapper.nmea.data;

/**
 * Enumeration type for target reference. Also provides method to parse and representation
 * in nmea.
 * 
 * @author jboger <juergen.boger@offis.de>
 * 
 */
public enum TargetReference {
	/** Reference target designated. */
	REFERENCE_TARGET("R"),
	/** Not designated. */
	NOT_DESIGNATED("");

	/** Stores the nmea representation. */
	private String shortName;

	/**
	 * Constructor.
	 * 
	 * @param shortName
	 *            Nmea representation.
	 */
	TargetReference(String shortName) {
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
	public static TargetReference parse(String s) {
		for (TargetReference r : TargetReference.class.getEnumConstants()) {
			if (r.shortName.equals(s)) {
				return r;
			}
		}
		return NOT_DESIGNATED;
	}
}
