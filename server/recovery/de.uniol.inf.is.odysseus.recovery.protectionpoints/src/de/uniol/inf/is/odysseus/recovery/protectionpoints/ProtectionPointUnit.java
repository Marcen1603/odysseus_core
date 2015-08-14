package de.uniol.inf.is.odysseus.recovery.protectionpoints;

/**
 * Units for protection points. <br />
 * In most cases time units should be used, but the unit "elements" is also
 * provided.
 * 
 * @see IProtectionPointManager#getUnit()
 * @author Michael Brand
 *
 */
public enum ProtectionPointUnit {

	/**
	 * Units for hourly protection points.
	 */
	HOURS(true, 3600000),

	/**
	 * Units for minutely protection points.
	 */
	MINUTES(true, 60000),

	/**
	 * Units for secondly protection points.
	 */
	SECONDS(true, 1000),

	/**
	 * Units for millisecondly protection points.
	 */
	MILLISECONDS(true, 1), ELEMENTS(false, -1);

	/**
	 * True, if the unit is a time unit.
	 */
	private final boolean mIsTimeUnit;

	/**
	 * For time units, this is the factor for the following calculation: <br />
	 * 1 current unit = {@code factor} milliseconds.
	 */
	private final int mConversionFactor;

	/**
	 * Creates a new unit for protection points.
	 * 
	 * @param isTimeUnit
	 *            True, if the unit is a time unit.
	 * @param conversionFactorForTimeUnits
	 *            For time units, this is the factor for the following
	 *            calculation: 1 current unit = {@code factor} milliseconds.
	 */
	private ProtectionPointUnit(boolean isTimeUnit,
			int conversionFactorForTimeUnits) {
		this.mIsTimeUnit = isTimeUnit;
		this.mConversionFactor = conversionFactorForTimeUnits;
	}

	/**
	 * Checks, if the unit is a time unit.
	 * 
	 * @return True, if the unit is a time unit.
	 */
	public boolean isTimeUnit() {
		return this.mIsTimeUnit;
	}

	/**
	 * Gets the conversion factor for milliseconds.
	 * 
	 * @return For time units, this is the factor for the following calculation:
	 *         1 current unit = {@code factor} milliseconds.
	 */
	public int getConversionFactor() {
		return this.mConversionFactor;
	}

}