package de.uniol.inf.is.odysseus.wrapper.nmea.sentence;

import java.util.Map;

import de.uniol.inf.is.odysseus.wrapper.nmea.data.Status;
import de.uniol.inf.is.odysseus.wrapper.nmea.util.ParseUtils;

/**
 * RSA - Rudder Sensor Angle<br>
 * <br>
 * 
 * <pre>
 * {@code
 * .      1   2 3   4 5
 *        |   | |   | |
 * $--RSA,x.x,A,x.x,A*hh
 * }
 * </pre>
 * <ol>
 * <li>Starboard (or single) rudder sensor, "-" means Turn To Port</li>
 * <li>Status, A means data is valid</li>
 * <li>Port rudder sensor</li>
 * <li>Status, A means data is valid</li>
 * <li>Checksum</li>
 * </ol>
 * 
 * @author aoppermann <alexander.oppermann@offis.de>
 *
 */

public class RSASentence extends Sentence{
	/** Default begin char for this sentence type. */
	public static final char BEGIN_CHAR = '$';
	/** Default talker for this sentence. */
	public static final String DEFAULT_TALKER = "IN";
	/** Sentence id. */
	public static final String SENTENCE_ID = "RSA";
	/** Default count of fields. */
	public static final int FIELD_COUNT = 4;
	
	/** Starboard (or single) rudder sensor, "-" means Turn To Port. */
	private Double srSensorValue;
	/** Status, A means data is valid. */
	private Status srStatus;
	/** Port rudder sensor. */
	private Double prSensorValue;
	/** Status, A means data is valid. */
	private Status prStatus;
	
	
	/**
	 * Default constructor for writing. Empty Sentence to fill attributes and
	 * call {@link #toNMEA()}.
	 */
	public RSASentence() {
		super(BEGIN_CHAR, DEFAULT_TALKER, SENTENCE_ID, FIELD_COUNT);
	}

	/**
	 * Default constructor for parsing.
	 * 
	 * @param nmea
	 *            Nmea String to be parsed.
	 */
	public RSASentence(String nmea) {
		super(nmea);
	}
	
	@Override
	protected void decode() {
		int index = 0;
		srSensorValue = ParseUtils.parseDouble(getValue(index++));
		srStatus = ParseUtils.parseStatus(getValue(index++));
		prSensorValue = ParseUtils.parseDouble(getValue(index++));
		prStatus = ParseUtils.parseStatus(getValue(index++));
	}

	@Override
	protected void encode() {
		int index = 0;
		setValue(index++, ParseUtils.toString(srSensorValue));
		setValue(index++, ParseUtils.toString(srStatus));
		setValue(index++, ParseUtils.toString(prSensorValue));
		setValue(index++, ParseUtils.toString(prStatus));
	}

	@Override
	protected void fillMap(Map<String, Object> res) {
		if (srSensorValue != null) res.put("srSensorValue", srSensorValue);
		if (srStatus != null) res.put("srStatus", srStatus);
		if (prSensorValue != null) res.put("prSensorValue", prSensorValue);
		if (prStatus != null) res.put("prStatus", prStatus);
	}

	public Double getStarboardRudderSensor(){
		return srSensorValue;
	}
	
	public void setStarboardRudderSensor(Double srSensorValue){
		this.srSensorValue = srSensorValue;
	}
	
	public Status getStarboardRudderStatus(){
		return srStatus;
	}
	
	public void setStarboardRudderStatus(Status srStatus){
		this.srStatus = srStatus;
	}
	
	public Double getPortRudderSensor(){
		return prSensorValue;
	}
	
	public void setPortRudderSensor(Double prSensorValue){
		this.prSensorValue = prSensorValue;
	}
	
	public Status getPortRudderStatus(){
		return prStatus;
	}
	
	public void setPortRudderStatus(Status prStatus){
		this.prStatus = prStatus;
	}
}
