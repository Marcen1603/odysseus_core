package de.uniol.inf.is.odysseus.wrapper.nmea.sentence;

import java.util.Locale;
import java.util.Map;

import de.uniol.inf.is.odysseus.wrapper.nmea.data.Reference;
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
 * @author jboger <juergen.boger@offis.de>
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
	private Double headingTrack;
	/** T = True */
	private Reference trackReference;
	/** Track Degrees */
	private Double headingMagnetic;
	/** M = Magnetic */
	private Reference magneticReference;
	/** Speed Knots */
	private Double speedKnots;
	/** N = Knots */
	private Unit speedKnotsUnits;
	/** Speed Kilometers Per Hour */
	private Double speedKilometers;
	/** K = Kilometers per Hour */
	private Unit speedKilometersUnits;
	
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
	
	/**
	 * Constructor for creating a message from a map. Reverse function of fillMap()
	 * 
	 * @param source
	 *            Map containing specific keys.
	 */		
	public VTGSentence(Map<String, Object> source)	
	{
		super(source, FIELD_COUNT);
		
		if (source.containsKey("headingTrack")) headingTrack = (double) source.get("headingTrack");
		if (source.containsKey("trackReference")) trackReference = ParseUtils.parseReference((String) source.get("trackReference"));
		if (source.containsKey("headingMagnetic")) headingMagnetic = (double) source.get("headingMagnetic");
		if (source.containsKey("magneticReference")) magneticReference = ParseUtils.parseReference((String) source.get("magneticReference"));
		if (source.containsKey("speedKnots")) speedKnots = (double) source.get("speedKnots");
		if (source.containsKey("speedKnotsUnits")) speedKnotsUnits = ParseUtils.parseUnit((String) source.get("speedKnotsUnits"));
		if (source.containsKey("speedKilometers")) speedKilometers = (double) source.get("speedKilometers");
		if (source.containsKey("speedKilometersUnits")) speedKilometersUnits = ParseUtils.parseUnit((String) source.get("speedKilometersUnits"));
	}				
	
	@Override
	protected void decode() {
		int index = 0;
		headingTrack = ParseUtils.parseDouble(getValue(index++));
		trackReference = ParseUtils.parseReference(getValue(index++));
		headingMagnetic = ParseUtils.parseDouble(getValue(index++));
		magneticReference = ParseUtils.parseReference(getValue(index++));
		speedKnots = ParseUtils.parseDouble(getValue(index++));
		speedKnotsUnits = ParseUtils.parseUnit(getValue(index++));
		speedKilometers = ParseUtils.parseDouble(getValue(index++));
		speedKilometersUnits = ParseUtils.parseUnit(getValue(index++));
	}

	@Override
	protected void encode() {
		int index = 0;
		
		setValue(index++, String.format(Locale.US, "%.1f", headingTrack));
		setValue(index++, ParseUtils.toString(trackReference));
		setValue(index++, String.format(Locale.US, "%.1f", headingMagnetic));
		setValue(index++, ParseUtils.toString(magneticReference));
		setValue(index++, String.format(Locale.US, "%.1f", speedKnots));
		setValue(index++, ParseUtils.toString(speedKnotsUnits));
		setValue(index++, String.format(Locale.US, "%.1f", speedKilometers));
		setValue(index++, ParseUtils.toString(speedKilometersUnits));
	}

	@Override
	protected void fillMap(Map<String, Object> res) {
		if (headingTrack != null) res.put("headingTrack", headingTrack);
		if (trackReference != Reference.NULL) res.put("trackReference", trackReference.name());
		if (headingMagnetic != null) res.put("headingMagnetic", headingMagnetic);
		if (magneticReference != Reference.NULL) res.put("magneticReference", magneticReference.name());
		if (speedKnots != null) res.put("speedKnots", speedKnots);
		if (speedKnotsUnits != Unit.NULL) res.put("speedKnotsUnits", speedKnotsUnits.name());
		if (speedKilometers != null) res.put("speedKilometers", speedKilometers);
		if (speedKilometersUnits != Unit.NULL) res.put("speedKilometersUnits", speedKilometersUnits.name());
	}

	public Double getHeadingTrack() {
		return headingTrack;
	}

	public void setHeadingTrack(Double headingTrack) {
		this.headingTrack = headingTrack;
	}

	public Reference getTrackReference() {
		return trackReference;
	}

	public void setTrackReference(Reference trackReference) {
		this.trackReference = trackReference;
	}

	public Double getHeadingMagnetic() {
		return headingMagnetic;
	}

	public void setHeadingMagnetic(Double headingMagnetic) {
		this.headingMagnetic = headingMagnetic;
	}

	public Reference getMagneticReference() {
		return magneticReference;
	}

	public void setMagneticReference(Reference magneticReference) {
		this.magneticReference = magneticReference;
	}

	public Double getSpeedKnots() {
		return speedKnots;
	}

	public void setSpeedKnots(Double speedKnots) {
		this.speedKnots = speedKnots;
	}

	public Unit getSpeedKnotsUnits() {
		return speedKnotsUnits;
	}

	public void setSpeedKnotsUnits(Unit speedKnotsUnits) {
		this.speedKnotsUnits = speedKnotsUnits;
	}

	public Double getSpeedKilometers() {
		return speedKilometers;
	}

	public void setSpeedKilometers(Double speedKilometers) {
		this.speedKilometers = speedKilometers;
	}

	public Unit getSpeedKilometersUnits() {
		return speedKilometersUnits;
	}

	public void setSpeedKilometersUnits(Unit speedKilometersUnits) {
		this.speedKilometersUnits = speedKilometersUnits;
	}
}
