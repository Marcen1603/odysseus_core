package de.uniol.inf.is.odysseus.recovery.checkpointing;

/**
 * Units for checkpoints. <br />
 * In most cases time units should be used, but the unit "elements" is also
 * provided.
 * 
 * @see ICheckpointManager#getUnit()
 * @author Michael Brand
 *
 */
public enum CheckpointUnit {

	/**
	 * Units for hourly checkpoints.
	 */
	HOURS(true, 3600000),

	/**
	 * Units for minutely checkpoints.
	 */
	MINUTES(true, 60000),

	/**
	 * Units for secondly checkpoints.
	 */
	SECONDS(true, 1000),

	/**
	 * Units for millisecondly checkpoints.
	 */
	MILLISECONDS(true, 1), ELEMENTS(false, -1);

	/**
	 * True, if the unit is a time unit.
	 */
	private final boolean timeUnit;

	/**
	 * For time units, this is the factor for the following calculation: <br />
	 * 1 current unit = {@code factor} milliseconds.
	 */
	private final int conversionFactor;

	/**
	 * Creates a new unit for checkpoints.
	 */
	private CheckpointUnit(boolean isTimeUnit,
			int conversionFactorForTimeUnits) {
		this.timeUnit = isTimeUnit;
		this.conversionFactor = conversionFactorForTimeUnits;
	}

	/**
	 * Checks, if the unit is a time unit.
	 */
	public boolean isTimeUnit() {
		return this.timeUnit;
	}

	/**
	 * Gets the conversion factor for milliseconds.
	 * 
	 * @return For time units, this is the factor for the following calculation:
	 *         1 current unit = {@code factor} milliseconds.
	 */
	public int getConversionFactor() {
		return this.conversionFactor;
	}

}