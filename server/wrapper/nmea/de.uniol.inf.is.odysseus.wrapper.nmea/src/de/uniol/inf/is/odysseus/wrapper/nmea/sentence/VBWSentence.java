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
 * @author aoppermann <alexander.oppermann@offis.de>
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
	private Double waterSpeedL;
	/** Transverse water speed, "-" means port. */
	private Double waterSpeedT;
	/** Status, A = data valid. */
	private Status statusWater;
	/** Longitudinal ground speed, "-" means astern. */
	private Double groundSpeedL;
	/** Transverse ground speed, "-" means port. */
	private Double groundSpeedT;
	/** Status, A = data valid. */
	private Status statusGround;
	
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
		waterSpeedL = ParseUtils.parseDouble(getValue(index++));
		waterSpeedT = ParseUtils.parseDouble(getValue(index++));
		statusWater = ParseUtils.parseStatus(getValue(index++));
		groundSpeedL = ParseUtils.parseDouble(getValue(index++));
		groundSpeedT = ParseUtils.parseDouble(getValue(index++));
		statusGround = ParseUtils.parseStatus(getValue(index++));
	}

	@Override
	protected void encode() {
		int index = 0;
		setValue(index++, ParseUtils.toString(waterSpeedL));
		setValue(index++, ParseUtils.toString(waterSpeedT));
		setValue(index++, ParseUtils.toString(statusWater));
		setValue(index++, ParseUtils.toString(groundSpeedL));
		setValue(index++, ParseUtils.toString(groundSpeedT));
		setValue(index++, ParseUtils.toString(statusGround));
	}

	@Override
	protected void fillMap(Map<String, Object> res) {
		if (waterSpeedL != null) res.put("waterSpeedL", waterSpeedL);
		if (waterSpeedT != null) res.put("waterSpeedT", waterSpeedT);
		if (statusWater != null) res.put("statusWater", statusWater);
		if (groundSpeedL != null) res.put("groundSpeedL", groundSpeedL);
		if (groundSpeedT != null) res.put("groundSpeedT", groundSpeedT);
		if (statusGround != null) res.put("statusGround", statusGround);
	}

	public Double getWaterSpeedL(){
		return waterSpeedL;
	}
	
	public void setWaterSpeedL(Double waterSpeedL){
		this.waterSpeedL = waterSpeedL;
	}
	
	public Double getWaterSpeedT(){
		return waterSpeedT;
	}
	
	public void setWaterSpeedT(Double waterSpeedT){
		this.waterSpeedT = waterSpeedT;
	}
	
	public Status getStatusWater(){
		return statusWater;
	}
	
	public void setStatusWater(Status statusWater){
		this.statusWater = statusWater;
	}
	
	public Double getGroundSpeedL(){
		return groundSpeedL;
	}
	
	public void setGroundSpeedL(Double groundSpeedL){
		this.groundSpeedL = groundSpeedL;
	}
	
	public Double getGroundSpeedT(){
		return groundSpeedT;
	}
	
	public void setGroundSpeedT(Double groundSpeedT){
		this.groundSpeedT = groundSpeedT;
	}
	
	public Status getStatusGround(){
		return statusGround;
	}
	
	public void setStatusGround(Status statusGround){
		this.statusGround = statusGround;
	}
}
