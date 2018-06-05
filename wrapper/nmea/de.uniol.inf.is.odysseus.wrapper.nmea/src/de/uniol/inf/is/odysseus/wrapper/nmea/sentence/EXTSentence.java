package de.uniol.inf.is.odysseus.wrapper.nmea.sentence;

import java.util.Map;

import de.uniol.inf.is.odysseus.wrapper.nmea.data.SignalIntegrity;
import de.uniol.inf.is.odysseus.wrapper.nmea.util.ParseUtils;

/**
 * EXT - Elsfleth eXtended Tuples - Proprietary sentence providing a vessels acceleration data<br>
 * <br>
 * 
 * <pre>
 * {@code
 * .     1   2   3    4
 *       |   |   |    |   
 * $-EXT,x.x,x.x,x.x*hh
 * }
 * </pre>
 * <ol>
 * <li>Acceleration X</li>
 * <li>Acceleration Y</li>
 * <li>Acceleration Z</li>
 * <li>Checksum</li>
 * </ol>
 * 
 * @author jbmzh <jan.meyer.zu.holte@uni-oldenburg.de>
 * 
 */
public class EXTSentence extends Sentence {
	/** Default begin char for this sentence type. */
	public static final char BEGIN_CHAR = '$';
	/** Default talker for this sentence. */
	public static final String DEFAULT_TALKER = "P";
	/** Sentence id. */
	public static final String SENTENCE_ID = "EXT";
	/** Default count of fields. */
	public static final int FIELD_COUNT = 4;
	
	/** Acceleration X */
	private Double accX;
	/** Acceleration Y */
	private Double accY;
	/** Acceleration Y */
	private Double accZ;
	/** Checksum */
	private SignalIntegrity signalIntegrity;

	
	/**
	 * Default constructor for writing. Empty Sentence to fill attributes and
	 * call {@link #toNMEA()}.
	 */
	public EXTSentence() {
		super(BEGIN_CHAR, DEFAULT_TALKER, SENTENCE_ID, FIELD_COUNT);
	}
	
	/**
	 * Default constructor for parsing.
	 * @param nmea
	 * Nmea String to be parsed.
	 */
	public EXTSentence(String nmea) {
		super(nmea);
	}

	/**
	 * Constructor for creating a message from a map. Reverse function of fillMap()
	 * 
	 * @param source
	 *            Map containing specific keys.
	 */		
	public EXTSentence(Map<String, Object> source)	
	{
		super(source, FIELD_COUNT);
		
		if (source.containsKey("accX")) accX = (double) source.get("accX");
		if (source.containsKey("accY")) accY = (double) source.get("accY");
		if (source.containsKey("accZ")) accZ = (double) source.get("accZ");
		if (source.containsKey("signalIntegrity")) signalIntegrity = ParseUtils.parseSignalIntegrity((String) source.get("signalIntegrity"));	
	}				
	
	@Override
	protected void decode() {
		int index = 0;
		accX = ParseUtils.parseDouble(getValue(index++));
		accY = ParseUtils.parseDouble(getValue(index++));
		accZ = ParseUtils.parseDouble(getValue(index++));
		signalIntegrity = ParseUtils.parseSignalIntegrity(getValue(index++));
	}

	@Override
	protected void encode() {
		int index = 0;
		setValue(index++, ParseUtils.toString(accX));
		setValue(index++, ParseUtils.toString(accY));
		setValue(index++, ParseUtils.toString(accZ));
		setValue(index++, ParseUtils.toString(signalIntegrity));
	}

	@Override
	protected void fillMap(Map<String, Object> res) {
		if (accX != null) res.put("accX", accX);
		if (accY != null) res.put("accY", accY);
		if (accZ != null) res.put("accZ", accZ);
		if (signalIntegrity != SignalIntegrity.NULL) res.put("signalIntegrity", signalIntegrity.name());
	}

	public Double getAccX() {
		return accX;
	}

	public void setAccX(Double accX) {
		this.accX = accX;
	}

	public Double getAccY() {
		return accY;
	}

	public void setAccY(Double accY) {
		this.accY = accY;
	}

	public Double getAccZ() {
		return accZ;
	}

	public void setAccZ(Double accZ) {
		this.accZ = accZ;
	}
}
