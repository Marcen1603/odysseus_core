package de.uniol.inf.is.odysseus.wrapper.nmea.sentence;

import java.util.Map;

import de.uniol.inf.is.odysseus.wrapper.nmea.data.TemperatureUnit;
import de.uniol.inf.is.odysseus.wrapper.nmea.util.ParseUtils;

/**
 * MTW - Water Temperature<br>
 * <br>
 * 
 * <pre>
 * {@code
 * .      1   2 3
 *        |   | | 
 * $--MTW,x.x,C*hh
 * }
 * </pre>
 * <ol>
 * <li>Degrees</li>
 * <li>Unit of Measurement, Celsius</li>
 * <li>Checksum</li>
 * </ol>
 * 
 * @author aoppermann <alexander.oppermann@offis.de>
 *
 */

public class MTWSentence extends Sentence{
	/** Default begin char for this sentence type. */
	public static final char BEGIN_CHAR = '$';
	/** Default talker for this sentence. */
	public static final String DEFAULT_TALKER = "WI";
	/** Sentence id. */
	public static final String SENTENCE_ID = "MTW";
	/** Default count of fields. */
	public static final int FIELD_COUNT = 2;
	
	/** Degrees. */
	private Double degrees;
	/** Unit of Measurement, Celsius. */
	private TemperatureUnit unit;
		
	/**
	 * Default constructor for writing. Empty Sentence to fill attributes and
	 * call {@link #toNMEA()}.
	 */
	public MTWSentence() {
		super(BEGIN_CHAR, DEFAULT_TALKER, SENTENCE_ID, FIELD_COUNT);
	}

	/**
	 * Default constructor for parsing.
	 * 
	 * @param nmea
	 *            Nmea String to be parsed.
	 */
	public MTWSentence(String nmea) {
		super(nmea);
	}
	
	/**
	 * Constructor for creating a message from a map. Reverse function of fillMap()
	 * 
	 * @param source
	 *            Map containing specific keys.
	 */		
	public MTWSentence(Map<String, Object> source)	
	{
		super(source, FIELD_COUNT);
		
		if (source.containsKey("unit")) unit = ParseUtils.parseTemperatureUnit((String) source.get("unit"));
		if (source.containsKey("degrees")) degrees = (double) source.get("degrees");
	}				
	
	@Override
	protected void decode() {
		int index = 0;
		degrees = ParseUtils.parseDouble(getValue(index++));
		unit = ParseUtils.parseTemperatureUnit(getValue(index++));
	}

	@Override
	protected void encode() {
		int index = 0;
		setValue(index++, ParseUtils.toString(degrees));
		setValue(index++, ParseUtils.toString(unit));
	}

	@Override
	protected void fillMap(Map<String, Object> res) {
		if (degrees != null) res.put("degrees", degrees);
		if (unit != TemperatureUnit.NULL) res.put("unit", unit.name());
	}

	public Double getDegrees(){
		return degrees;
	}
	
	public void setDegrees(Double degrees){
		this.degrees = degrees;
	}
	
	public TemperatureUnit getUnit(){
		return unit;
	}
	
	public void setUnit(TemperatureUnit unit){
		this.unit = unit;
	}
}
