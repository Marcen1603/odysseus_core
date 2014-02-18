package de.uniol.inf.is.odysseus.wrapper.nmea.sentence;

import java.util.Map;

import de.uniol.inf.is.odysseus.wrapper.nmea.data.Status;
import de.uniol.inf.is.odysseus.wrapper.nmea.util.ParseUtils;

/**
 * VBW Dual Ground/Water Speed<br>
 * <br>
 * 
 * <pre>
 * {@code
 * .      1   2   3 4   5   6 7
 *        |   |   | |   |   | |
 * $--VBW,x.x,x.x,A,x.x,x.x,A*hh
 * }
 * </pre>
 * <ol>
 * <li>Longitudinal water speed, "-" means astern</li>
 * <li>Transverse water speed, "-" means port</li>
 * <li>Status, A = data valid</li>
 * <li>Longitudinal ground speed, "-" means astern</li>
 * <li>Transverse ground speed, "-" means port</li>
 * <li>Status, A = data valid</li>
 * </ol>
 * 
 * @author jboger <juergen.boger@offis.de>
 *
 */
public class VBWSentence extends Sentence{
	/** Default begin char for this sentence type. */
	public static final char BEGIN_CHAR = '$';
	/** Default talker for this sentence. */
	public static final String DEFAULT_TALKER = "GP";
	/** Sentence id. */
	public static final String SENTENCE_ID = "VBW";
	/** Default count of fields. */
	public static final int FIELD_COUNT = 6;
	
	/** Longitudinal water speed, "-" means astern. */
	private Double waterSpeedLongitudinal;
	/** Transverse water speed, "-" means port. */
	private Double waterSpeedTransverse;
	/** Status, A = data valid. */
	private Status statusWaterSpeed;
	/** Longitudinal ground speed, "-" means astern. */
	private Double groundSpeedLongitudinal;
	/** Transverse ground speed, "-" means port. */
	private Double groundSpeedTransverse;
	/** Status, A = data valid. */
	private Status statusGroundSpeed;
	
	/**
	 * Default constructor for writing. Empty Sentence to fill attributes and
	 * call {@link #toNMEA()}.
	 */
	public VBWSentence() {
		super(BEGIN_CHAR, DEFAULT_TALKER, SENTENCE_ID, FIELD_COUNT);
	}

	/**
	 * Default constructor for parsing.
	 * 
	 * @param nmea
	 *            Nmea String to be parsed.
	 */
	public VBWSentence(String nmea) {
		super(nmea);
	}
	
	@Override
	protected void decode() {
		int index = 0;
		waterSpeedLongitudinal = ParseUtils.parseDouble(getValue(index++));
		waterSpeedTransverse = ParseUtils.parseDouble(getValue(index++));
		statusWaterSpeed = ParseUtils.parseStatus(getValue(index++));
		groundSpeedLongitudinal = ParseUtils.parseDouble(getValue(index++));
		groundSpeedTransverse = ParseUtils.parseDouble(getValue(index++));
		statusGroundSpeed = ParseUtils.parseStatus(getValue(index++));
	}

	@Override
	protected void encode() {
		int index = 0;
		setValue(index++, ParseUtils.toString(waterSpeedLongitudinal));
		setValue(index++, ParseUtils.toString(waterSpeedTransverse));
		setValue(index++, ParseUtils.toString(statusWaterSpeed));
		setValue(index++, ParseUtils.toString(groundSpeedLongitudinal));
		setValue(index++, ParseUtils.toString(groundSpeedTransverse));
		setValue(index++, ParseUtils.toString(statusGroundSpeed));
	}

	@Override
	protected void fillMap(Map<String, Object> res) {
		if (waterSpeedLongitudinal != null) res.put("waterSpeedLongitudinal", waterSpeedLongitudinal);
		if (waterSpeedTransverse != null) res.put("waterSpeedTransverse", waterSpeedTransverse);
		if (statusWaterSpeed != null) res.put("statusWaterSpeed", statusWaterSpeed);
		if (groundSpeedLongitudinal != null) res.put("groundSpeedLongitudinal", groundSpeedLongitudinal);
		if (groundSpeedTransverse != null) res.put("groundSpeedTransverse", groundSpeedTransverse);
		if (statusGroundSpeed != null) res.put("statusGroundSpeed", statusGroundSpeed);
	}

	public Double getWaterSpeedLongitudinal() {
		return waterSpeedLongitudinal;
	}

	public void setWaterSpeedLongitudinal(Double waterSpeedLongitudinal) {
		this.waterSpeedLongitudinal = waterSpeedLongitudinal;
	}

	public Double getWaterSpeedTransverse() {
		return waterSpeedTransverse;
	}

	public void setWaterSpeedTransverse(Double waterSpeedTransverse) {
		this.waterSpeedTransverse = waterSpeedTransverse;
	}

	public Status getStatusWaterSpeed() {
		return statusWaterSpeed;
	}

	public void setStatusWaterSpeed(Status statusWaterSpeed) {
		this.statusWaterSpeed = statusWaterSpeed;
	}

	public Double getGroundSpeedLongitudinal() {
		return groundSpeedLongitudinal;
	}

	public void setGroundSpeedLongitudinal(Double groundSpeedLongitudinal) {
		this.groundSpeedLongitudinal = groundSpeedLongitudinal;
	}

	public Double getGroundSpeedTransverse() {
		return groundSpeedTransverse;
	}

	public void setGroundSpeedTransverse(Double groundSpeedTransverse) {
		this.groundSpeedTransverse = groundSpeedTransverse;
	}

	public Status getStatusGroundSpeed() {
		return statusGroundSpeed;
	}

	public void setStatusGroundSpeed(Status statusGroundSpeed) {
		this.statusGroundSpeed = statusGroundSpeed;
	}
}
