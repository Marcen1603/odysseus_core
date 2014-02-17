package de.uniol.inf.is.odysseus.wrapper.nmea.sentence;

import java.util.Map;

import de.uniol.inf.is.odysseus.wrapper.nmea.data.Status;
import de.uniol.inf.is.odysseus.wrapper.nmea.data.Unit;
import de.uniol.inf.is.odysseus.wrapper.nmea.util.ParseUtils;

/**
 * VTG - Track Made Good and Ground Speed<br>
 * <br>
 * 
 * <pre>
 * {@code
 * .      1   2 3   4 5   6 7   8 9
 *        |   | |   | |   | |   | |
 * $--VTG,x.x,T,x.x,M,x.x,N,x.x,K*hh
 * }
 * </pre>
 * <ol>
 * <li>Track Degrees</li>
 * <li>T = True</li>
 * <li>Track Degrees</li>
 * <li>M = Magnetic</li>
 * <li>Speed Knots</li>
 * <li>N = Knots</li>
 * <li>Speed Kilometers Per Hour</li>
 * <li>K = Kilometers Per Hour</li>
 * </ol>
 * 
 * @author aoppermann <alexander.oppermann@offis.de>
 *
 */
public class VTGSentence extends Sentence{
	/** Default begin char for this sentence type. */
	public static final char BEGIN_CHAR = '$';
	/** Default talker for this sentence. */
	public static final String DEFAULT_TALKER = "GP";
	/** Sentence id. */
	public static final String SENTENCE_ID = "VTG";
	/** Default count of fields. */
	public static final int FIELD_COUNT = 8;
	
	/** Track Degrees */
	private Double degreesT;
	/** T = True */
	private Status status;
	/** Track Degrees */
	private Double degreesM;
	/** M = Magnetic */
	private String magnetic;
	/** Speed Knots */
	private Double speedKnots;
	/** N = Knots */
	private Unit knots;
	/** Speed Kilometers Per Hour */
	private Double speedKilometers;
	/** K = Kilometers per Hour */
	private Unit kilometersPerHour;
	
	/**
	 * Default constructor for writing. Empty Sentence to fill attributes and
	 * call {@link #toNMEA()}.
	 */
	public VTGSentence() {
		super(BEGIN_CHAR, DEFAULT_TALKER, SENTENCE_ID, FIELD_COUNT);
	}

	/**
	 * Default constructor for parsing.
	 * 
	 * @param nmea
	 *            Nmea String to be parsed.
	 */
	public VTGSentence(String nmea) {
		super(nmea);
	}
	
	@Override
	protected void decode() {
		int index = 0;
		degreesT = ParseUtils.parseDouble(getValue(index++));
		status = ParseUtils.parseStatus(getValue(index++));
		degreesM = ParseUtils.parseDouble(getValue(index++));
		magnetic = getValue(index++);
		speedKnots = ParseUtils.parseDouble(getValue(index++));
		knots = ParseUtils.parseUnit(getValue(index++));
		speedKilometers = ParseUtils.parseDouble(getValue(index++));
		kilometersPerHour = ParseUtils.parseUnit(getValue(index++));
	}

	@Override
	protected void encode() {
		int index = 0;
		setValue(index++, ParseUtils.toString(degreesT));
		setValue(index++, ParseUtils.toString(status));
		setValue(index++, ParseUtils.toString(degreesM));
		setValue(index++, magnetic);
		setValue(index++, ParseUtils.toString(speedKnots));
		setValue(index++, ParseUtils.toString(knots));
		setValue(index++, ParseUtils.toString(speedKilometers));
		setValue(index++, ParseUtils.toString(kilometersPerHour));
	}

	@Override
	protected void fillMap(Map<String, Object> res) {
		if (degreesT != null) res.put("degreesT", degreesT);
		if (status != null) res.put("status", status);
		if (degreesM != null) res.put("degreesM", degreesM);
		if (magnetic != null) res.put("magnetic", magnetic);
		if (speedKnots != null) res.put("speedKnots", speedKnots);
		if (knots != null) res.put("knots", knots);
		if (speedKilometers != null) res.put("speedKilometers", speedKilometers);
		if (kilometersPerHour != null) res.put("kilometers", kilometersPerHour);
	}

	public Double getDegreesT(){
		return degreesT;
	}
	
	public void setDegreesT(Double degreesT){
		this.degreesT = degreesT;
	}
	
	public Status getStatus(){
		return status;
	}
	
	public void setStatus(Status status){
		this.status = status;
	}
	
	public Double getDegreesM(){
		return degreesM;
	}
	
	public void setDegreesM(Double degreesM){
		this.degreesM = degreesM;
	}
	
	public String getMagnetic(){
		return magnetic;
	}
	
	public void setMagnetic(String magnetic){
		this.magnetic = magnetic;
	}
	
	public Double getSpeedKnots(){
		return speedKnots;
	}
	
	public void setSpeedKnots(Double speedKnots){
		this.speedKnots = speedKnots;
	}
	
	public Unit getKnots(){
		return knots;
	}
	
	public void setKnots(Unit knots){
		this.knots = knots;
	}
	
	public Double getSpeedKilometers(){
		return speedKilometers;
	}
	
	public void setSpeedKilometers(Double speedKilometers){
		this.speedKilometers = speedKilometers;
	}
	
	public Unit getKilometersPerHour(){
		return kilometersPerHour;
	}
	
	public void setKilometersPerHour(Unit kilometersPerHour){
		this.kilometersPerHour = kilometersPerHour;
	}
}
