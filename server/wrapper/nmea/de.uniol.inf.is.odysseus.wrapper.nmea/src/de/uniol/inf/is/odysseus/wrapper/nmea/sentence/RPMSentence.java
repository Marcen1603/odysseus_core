package de.uniol.inf.is.odysseus.wrapper.nmea.sentence;

import java.util.Map;

import de.uniol.inf.is.odysseus.wrapper.nmea.data.Status;
import de.uniol.inf.is.odysseus.wrapper.nmea.util.ParseUtils;

/**
 * RPM - Revolutions<br>
 * <br>
 * 
 * <pre>
 * {@code
 * .      1 2 3   4   5 6
 *        | | |   |   | |
 * $--RPM,a,x,x.x,x.x,A*hh
 * }
 * </pre>
 * <ol>
 * <li>Source; S = Shaft, E = Engine</li>
 * <li>Engine or shaft number</li>
 * <li>Speed, Revolutions per minute</li>
 * <li>Propeller pitch, % of maximum, "-" means astern</li>
 * <li>Status, A means data is valid</li>
 * <li>Checksum</li>
 * </ol>
 * 
 * @author aoppermann <alexander.oppermann@offis.de>
 *
 */

public class RPMSentence extends Sentence{
	/** Default begin char for this sentence type. */
	public static final char BEGIN_CHAR = '$';
	/** Default talker for this sentence. */
	public static final String DEFAULT_TALKER = "IN";
	/** Sentence id. */
	public static final String SENTENCE_ID = "RPM";
	/** Default count of fields. */
	public static final int FIELD_COUNT = 5;
	
	/** Source; S = Shaft, E = Engine. */
	private String source;
	/** Engine or shaft number. */
	private Integer number;
	/** Speed, Revolutions per minute. */
	private Double speed;
	/** Propeller pitch, % of maximum, "-" means astern. */
	private Double pitch;
	/** Status, A means data is valid. */
	private Status status;
	
	/**
	 * Default constructor for writing. Empty Sentence to fill attributes and
	 * call {@link #toNMEA()}.
	 */
	public RPMSentence() {
		super(BEGIN_CHAR, DEFAULT_TALKER, SENTENCE_ID, FIELD_COUNT);
	}

	/**
	 * Default constructor for parsing.
	 * 
	 * @param nmea
	 *            Nmea String to be parsed.
	 */
	public RPMSentence(String nmea) {
		super(nmea);
	}
	
	@Override
	protected void decode() {
		int index = 0;
		source = getValue(index++);
		number = ParseUtils.parseInteger(getValue(index++));
		speed = ParseUtils.parseDouble(getValue(index++));
		pitch = ParseUtils.parseDouble(getValue(index++));
		status = ParseUtils.parseStatus(getValue(index++));
		
	}

	@Override
	protected void encode() {
		int index = 0;
		setValue(index++, source);
		setValue(index++, ParseUtils.toString(number));
		setValue(index++, ParseUtils.toString(speed));
		setValue(index++, ParseUtils.toString(pitch));
		setValue(index++, ParseUtils.toString(status));
	}

	@Override
	protected void fillMap(Map<String, Object> res) {
		if (source != null) res.put("source", source);
		if (number != null) res.put("number", number);
		if (speed != null) res.put("speed", speed);
		if (pitch != null) res.put("pitch", pitch);
		if (status != Status.NULL) res.put("status", status);
	}

	public String getSource(){
		return source;
	}
	
	public void setSource(String source){
		this.source = source;
	}
	
	public Integer getNumber(){
		return number;
	}
	
	public void setNumber(Integer number){
		this.number = number;
	}
	
	public Double getSpeed(){
		return speed;
	}
	
	public void setSpeed(Double speed){
		this.speed = speed;
	}
	
	public Double getPitch(){
		return pitch;
	}
	
	public void setPitch(Double pitch){
		this.pitch = pitch;
	}
	
	public Status getStatus(){
		return status;
	}
	
	public void setStatus(Status status){
		this.status = status;
	}
}
