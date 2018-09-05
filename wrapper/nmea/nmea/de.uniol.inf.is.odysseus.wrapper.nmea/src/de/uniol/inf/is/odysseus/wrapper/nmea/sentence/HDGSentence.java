package de.uniol.inf.is.odysseus.wrapper.nmea.sentence;

import java.util.Map;

import de.uniol.inf.is.odysseus.wrapper.nmea.data.Hemisphere;
import de.uniol.inf.is.odysseus.wrapper.nmea.util.ParseUtils;

/**
 * HDG - Heading-Deviation & Variation<br>
 * <br>
 * 
 * <pre>
 * {@code
 * .      1   2   3 4   5 6
 *        |   |   | |   | |
 * $--HDG,x.x,x.x,a,x.x,a*hh
 * }
 * </pre>
 * <ol>
 * <li>Magnetic Sensor heading in degrees</li>
 * <li>Magnetic Deviation, degrees</li>
 * <li>Magnetic Deviation direction, E = Easterly, W = Westerly</li>
 * <li>Magnetic Variation degrees</li>
 * <li>Magnetic Variation direction, E = Easterly, W = Westerly</li>
 * <li>Checksum</li>
 * </ol>
 * 
 * @author jboger <juergen.boger@offis.de>
 * 
 */
public class HDGSentence extends Sentence {
	/** Default begin char for this sentence type. */
	public static final char BEGIN_CHAR = '$';
	/** Default talker for this sentence. */
	public static final String DEFAULT_TALKER = "HC";
	/** Sentence id. */
	public static final String SENTENCE_ID = "HDG";
	/** Default count of fields. */
	public static final int FIELD_COUNT = 6;
	
	/** Magnetic Sensor heading in degrees */
	private Double heading;
	/** Magnetic Deviation, degrees */
	private Double deviation;
	/** Magnetic Deviation direction, E = Easterly, W = Westerly */
	private Hemisphere deviationDir;
	/** Magnetic Variation degrees */
	private Double variation;
	/** Magnetic Variation direction, E = Easterly, W = Westerly */
	private Hemisphere variationDir;
	
	/**
	 * Default constructor for writing. Empty Sentence to fill attributes and
	 * call {@link #toNMEA()}.
	 */
	public HDGSentence() {
		super(BEGIN_CHAR, DEFAULT_TALKER, SENTENCE_ID, FIELD_COUNT);
	}
	
	/**
	 * Default constructor for parsing.
	 * @param nmea
	 * Nmea String to be parsed.
	 */
	public HDGSentence(String nmea) {
		super(nmea);
	}
	
	/**
	 * Constructor for creating a message from a map. Reverse function of fillMap()
	 * 
	 * @param source
	 *            Map containing specific keys.
	 */		
	public HDGSentence(Map<String, Object> source)	
	{
		super(source, FIELD_COUNT);
		
		if (source.containsKey("heading")) heading = (double) source.get("heading");
		if (source.containsKey("deviation")) deviation = (double) source.get("deviation");		
		if (source.containsKey("variation")) variation = (double) source.get("variation");
		if (source.containsKey("deviationDir")) deviationDir = ParseUtils.parseHemisphere((String) source.get("deviationDir"));
		if (source.containsKey("variationDir")) variationDir = ParseUtils.parseHemisphere((String) source.get("variationDir"));
	}				

	@Override
	protected void decode() {
		int index = 0;
		heading = ParseUtils.parseDouble(getValue(index++));
		deviation = ParseUtils.parseDouble(getValue(index++));
		deviationDir = ParseUtils.parseHemisphere(getValue(index++));
		variation = ParseUtils.parseDouble(getValue(index++));
		variationDir = ParseUtils.parseHemisphere(getValue(index++));
	}

	@Override
	protected void encode() {
		int index = 0;
		setValue(index++, ParseUtils.toString(heading));
		setValue(index++, ParseUtils.toString(deviation));
		setValue(index++, ParseUtils.toString(deviationDir));
		setValue(index++, ParseUtils.toString(variation));
		setValue(index++, ParseUtils.toString(variationDir));
	}

	@Override
	protected void fillMap(Map<String, Object> res) {
		if (heading != null) res.put("heading", heading);
		if (deviation != null) res.put("deviation", deviation);
		if (deviationDir != Hemisphere.NULL) res.put("deviationDir", deviationDir.name());
		if (variation != null) res.put("variation", variation);
		if (variationDir != Hemisphere.NULL) res.put("variationDir", variationDir.name());
	}

	public Double getHeading() {
		return heading;
	}

	public void setHeading(Double heading) {
		this.heading = heading;
	}

	public Double getDeviation() {
		return deviation;
	}

	public void setDeviation(Double deviation) {
		this.deviation = deviation;
	}

	public Hemisphere getDeviationDir() {
		return deviationDir;
	}

	public void setDeviationDir(Hemisphere deviationDir) {
		this.deviationDir = deviationDir;
	}

	public Double getVariation() {
		return variation;
	}

	public void setVariation(Double variation) {
		this.variation = variation;
	}

	public Hemisphere getVariationDir() {
		return variationDir;
	}

	public void setVariationDir(Hemisphere variationDir) {
		this.variationDir = variationDir;
	}
}
