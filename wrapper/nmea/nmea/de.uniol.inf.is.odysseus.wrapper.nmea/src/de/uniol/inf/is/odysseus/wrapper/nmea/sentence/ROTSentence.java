package de.uniol.inf.is.odysseus.wrapper.nmea.sentence;

import java.util.Locale;
import java.util.Map;

import de.uniol.inf.is.odysseus.wrapper.nmea.util.ParseUtils;

/**
 * ROT - Rate Of Turn
 * <br>
 * 
 * 
 * <pre>
 * {@code
 * .      1   2 3
 *        |   | |
 * $--ROT,x.x,A*hh
 * }
 * </pre>
 * <ol>
 * <li>Rate Of Turn, </li>
 * <li>Status</li>
 * <li>Checksum</li>
 * </ol>
 * 
 * @author hsurm <henrik.surm@uni-oldenburg.de>
 * 
 */
public class ROTSentence extends Sentence {
	/** Default begin char for this sentence type. */
	public static final char BEGIN_CHAR = '$';
	/** Default talker for this sentence. */
	public static final String DEFAULT_TALKER = "IN";
	/** Sentence id. */
	public static final String SENTENCE_ID = "ROT";
	/** Default count of fields. */
	public static final int FIELD_COUNT = 2;

	private Double rateOfTurn; // degrees per minute, "-" means bow turns to port
	private Boolean valid;	// A means data is valid, V means data is invalid
	/**
	 * Default constructor for writing. Empty Sentence to fill attributes and
	 * call {@link #toNMEA()}.
	 */
	public ROTSentence() {
		super(BEGIN_CHAR, DEFAULT_TALKER, SENTENCE_ID, FIELD_COUNT);
	}

	/**
	 * Default constructor for parsing.
	 * 
	 * @param nmea
	 *            Nmea String to be parsed.
	 */
	public ROTSentence(String nmea) {
		super(nmea);
	}

	/**
	 * Constructor for creating a message from a map. Reverse function of fillMap()
	 * 
	 * @param source
	 *            Map containing specific keys.
	 */	
	public ROTSentence(Map<String, Object> source)	
	{
		super(source, FIELD_COUNT);
		
		if (source.containsKey("rateOfTurn")) rateOfTurn = (double) source.get("rateOfTurn");
		if (source.containsKey("valid")) valid = (boolean) source.get("valid");
	}		
	
	private static boolean parseValid(String val)
	{
		return val == null ? null : val.equals("A");
	}
	
	private static String validToString(boolean val)
	{
		return val ? "A" : "V";
	}
	
	@Override
	protected void decode() {
		int index = 0;
		rateOfTurn = ParseUtils.parseDouble(getValue(index++));
		valid = parseValid(getValue(index++));
	}

	@Override
	protected void encode() {
		int index = 0;
		setValue(index++, String.format(Locale.US, "%.1f", rateOfTurn));
		setValue(index++, validToString(valid));
	}

	@Override
	protected void fillMap(Map<String, Object> res) {
		if (rateOfTurn != null) res.put("rateOfTurn", rateOfTurn);
		if (valid != null) res.put("valid", valid);
	}

	public Double getRateOfTurn() {
		return rateOfTurn;
	}

	public void setRateOfTurn(Double rateOfTurn) {
		this.rateOfTurn = rateOfTurn;
	}

	public Boolean getValid() {
		return valid;
	}

	public void setValid(Boolean valid) {
		this.valid = valid;
	}
}
