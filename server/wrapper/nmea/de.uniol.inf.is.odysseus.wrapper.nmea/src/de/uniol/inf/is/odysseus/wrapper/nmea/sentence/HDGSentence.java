package de.uniol.inf.is.odysseus.wrapper.nmea.sentence;

import java.util.Map;

import de.uniol.inf.is.odysseus.wrapper.nmea.data.Hemisphere;
import de.uniol.inf.is.odysseus.wrapper.nmea.util.ParseUtils;

public class HDGSentence extends Sentence {
	public static final char BEGIN_CHAR = '$';
	public static final String DEFAULT_TALKER = "HC";
	public static final String SENTENCE_ID = "HDG";
	public static final int FIELD_COUNT = 6;
	
	private Double heading;
	private Double deviation;
	private Hemisphere deviationDir;
	private Double variation;
	private Hemisphere variationDir;
	
	public HDGSentence() {
		super(BEGIN_CHAR, DEFAULT_TALKER, SENTENCE_ID, FIELD_COUNT);
	}
	
	public HDGSentence(String nmea) {
		super(nmea);
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
	public void fillMap(Map<String, Object> res) {
		if (heading != null) res.put("heading", heading);
		if (deviation != null) res.put("deviation", deviation);
		if (deviationDir != Hemisphere.NULL) res.put("deviationDir", deviationDir);
		if (variation != null) res.put("variation", variation);
		if (variationDir != Hemisphere.NULL) res.put("variationDir", variationDir);
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
