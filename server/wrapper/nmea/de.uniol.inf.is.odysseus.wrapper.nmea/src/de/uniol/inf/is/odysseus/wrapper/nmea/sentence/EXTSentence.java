package de.uniol.inf.is.odysseus.wrapper.nmea.sentence;

import java.util.Map;

import de.uniol.inf.is.odysseus.wrapper.nmea.util.ParseUtils;

/**
 * EXT - Elsfleth eXtended Tuples - Proprietary sentence providing a vessels acceleration data<br>
 * <br>
 * 
 * <pre>
 * {@code
 * .     1   2   3 
 *       |   |   | 
 * $-EXT,x.x,x.x,x.x
 * }
 * </pre>
 * <ol>
 * <li>Acceleration X</li>
 * <li>Acceleration Y</li>
 * <li>Acceleration Z</li>
 * </ol>
 * 
 * @author jboger <juergen.boger@offis.de>
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
	public static final int FIELD_COUNT = 3;
	
	/** Acceleration X */
	private Double accX;
	/** Acceleration Y */
	private Double accY;
	/** Acceleration Y */
	private Double accZ;

	
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

	@Override
	protected void decode() {
		int index = 0;
		accX = ParseUtils.parseDouble(getValue(index++));
		accY = ParseUtils.parseDouble(getValue(index++));
		accZ = ParseUtils.parseDouble(getValue(index++));
	}

	@Override
	protected void encode() {
		int index = 0;
		setValue(index++, ParseUtils.toString(accX));
		setValue(index++, ParseUtils.toString(accY));
		setValue(index++, ParseUtils.toString(accZ));
	}

	@Override
	protected void fillMap(Map<String, Object> res) {
		if (accX != null) res.put("heading", accX);
		if (accY != null) res.put("deviation", accY);
		if (accZ != null) res.put("variation", accZ);
	}

	public Double getHeading() {
		return accX;
	}

	public void setHeading(Double heading) {
		this.accX = heading;
	}

	public Double getDeviation() {
		return accY;
	}

	public void setDeviation(Double deviation) {
		this.accY = deviation;
	}

	public Double getDeviationDir() {
		return accZ;
	}

	public void setDeviationDir(Double deviationDir) {
		this.accZ = deviationDir;
	}
}
