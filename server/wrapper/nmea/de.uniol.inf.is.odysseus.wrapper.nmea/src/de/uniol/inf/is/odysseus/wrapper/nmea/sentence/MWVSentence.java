package de.uniol.inf.is.odysseus.wrapper.nmea.sentence;

import java.util.Map;

import de.uniol.inf.is.odysseus.wrapper.nmea.data.Reference;
import de.uniol.inf.is.odysseus.wrapper.nmea.data.SpeedUnit;
import de.uniol.inf.is.odysseus.wrapper.nmea.data.Status;
import de.uniol.inf.is.odysseus.wrapper.nmea.util.ParseUtils;

/**
 * MWV - Wind Speed and Angle<br>
 * <br>
 * 
 * <pre>
 * {@code
 * .      1   2 3   4 5 6
 *        |   | |   | | |
 * $--MWV,x.x,a,x.x,a,A*hh
 * }
 * </pre>
 * <ol>
 * <li>Wind Angle, 0 to 360 degrees</li>
 * <li>Reference, R = Relative, T = True</li>
 * <li>Wind Speed</li>
 * <li>Wind Speed Units, K/M/N</li>
 * <li>Status, A = Data Valid</li>
 * <li>Checksum</li>
 * </ol>
 *                           
 * @author aoppermann <alexander.oppermann@offis.de>
 *
 */

public class MWVSentence extends Sentence{
	/** Default begin char for this sentence type. */
	public static final char BEGIN_CHAR = '$';
	/** Default talker for this sentence. */
	public static final String DEFAULT_TALKER = "WI";
	/** Sentence id. */
	public static final String SENTENCE_ID = "MWV";
	/** Default count of fields. */
	public static final int FIELD_COUNT = 5;
	
	/** Wind Angle, 0 to 360 degrees. */
	private Double angle;
	/** Reference, R = Relative, T = True. */
	private Reference reference;
	/** Wind Speed. */
	private Double speed;
	/** Wind Speed Units, K/M/N. */
	private SpeedUnit speedUnit;
	/** Status, A = Data Valid. */
	private Status status;
	
	/**
	 * Default constructor for writing. Empty Sentence to fill attributes and
	 * call {@link #toNMEA()}.
	 */
	public MWVSentence() {
		super(BEGIN_CHAR, DEFAULT_TALKER, SENTENCE_ID, FIELD_COUNT);
	}

	/**
	 * Default constructor for parsing.
	 * 
	 * @param nmea
	 *            Nmea String to be parsed.
	 */
	public MWVSentence(String nmea) {
		super(nmea);
	}
	
	@Override
	protected void decode() {
		int index = 0;
		angle = ParseUtils.parseDouble(getValue(index++));
		reference = ParseUtils.parseReference(getValue(index++));
		speed = ParseUtils.parseDouble(getValue(index++));
		speedUnit = ParseUtils.parseSpeedUnit(getValue(index++));
		status = ParseUtils.parseStatus(getValue(index++));
	}

	@Override
	protected void encode() {
		int index = 0;
		setValue(index++, ParseUtils.toString(angle));
		setValue(index++, ParseUtils.toString(reference));
		setValue(index++, ParseUtils.toString(speed));
		setValue(index++, ParseUtils.toString(speedUnit));
		setValue(index++, ParseUtils.toString(status));
	}

	@Override
	protected void fillMap(Map<String, Object> res) {
		if (angle != null) res.put("angle", angle);
		if (reference != Reference.NULL) res.put("reference", reference);
		if (speed != null) res.put("speed", speed);
		if (speedUnit != SpeedUnit.NULL) res.put("speedUnit", speedUnit);
		if (status != Status.NULL) res.put("status", status);
	}

	public Double getAngle(){
		return angle;
	}
	
	public void setAngle(Double angle){
		this.angle = angle;
	}
	
	public Reference getReference(){
		return reference;
	}
	
	public void setReference(Reference reference){
		this.reference = reference;
	}
	
	public Double getSpeed(){
		return speed;
	}
	
	public void setSpeed(Double speed){
		this.speed = speed;
	}
	
	public SpeedUnit getSpeedUnit(){
		return speedUnit;
	}
	
	public void setSpeedUnit(SpeedUnit speedUnit){
		this.speedUnit = speedUnit;
	}
	
	public Status getStatus(){
		return status;
	}
	
	public void setStatus(Status status){
		this.status = status;
	}
}
