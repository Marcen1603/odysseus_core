package de.uniol.inf.is.odysseus.wrapper.nmea.data;

/**
 * Symbolizes a two digit target number for TTA or TLL sentences. Internal
 * representation is handled as decimal number. Provides methods for parsing or
 * converting to nmea.
 * 
 * @author jboger <juergen.boger@offis.de>
 * 
 */
public class TargetNumber {
	/** The target number. */
	private byte number;

	/**
	 * Constructs a target number with the given number.
	 * 
	 * @param number
	 *            The target number.
	 */
	public TargetNumber(byte number) {
		this.number = number;
	}

	@Override
	public String toString() {
		return String.format("%02d", number);
	}

	/**
	 * Gets the number.
	 * 
	 * @return Number as byte.
	 */
	public byte getNumber() {
		return this.number;
	}

	/**
	 * Sets the number.
	 * 
	 * @param number
	 *            Number as byte.
	 */
	public void setNumber(byte number) {
		this.number = number;
	}

	/**
	 * Parses a String and returns a TargetNumber object.
	 * 
	 * @param value
	 *            String value from nmea
	 * @return {@link TargetNumber}.
	 */
	public static TargetNumber parse(String value) {
		try {
			byte res = Byte.parseByte(value);
			return new TargetNumber(res);
		} catch (Exception e) {
			return null;
		}
	}
}
