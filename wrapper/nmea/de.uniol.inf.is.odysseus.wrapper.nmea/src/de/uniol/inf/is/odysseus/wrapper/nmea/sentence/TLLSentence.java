package de.uniol.inf.is.odysseus.wrapper.nmea.sentence;

import java.util.Map;

import de.uniol.inf.is.odysseus.wrapper.nmea.data.Hemisphere;
import de.uniol.inf.is.odysseus.wrapper.nmea.data.TargetNumber;
import de.uniol.inf.is.odysseus.wrapper.nmea.data.TargetReference;
import de.uniol.inf.is.odysseus.wrapper.nmea.data.TargetStatus;
import de.uniol.inf.is.odysseus.wrapper.nmea.data.Time;
import de.uniol.inf.is.odysseus.wrapper.nmea.util.ParseUtils;

/**
 * TTL - Target latitude and longitude<br>
 * <br>
 * 
 * <pre>
 * {@code
 *.      1  2          3           4    5         6 7 8
 *        |  |          |           |    |         | | |
 * $--TLL,xx,llll.lll,a,yyyyy.yyy,a,c--c,hhmmss.ss,a,a*hh
 * }
 * </pre>
 * <ol>
 * <li> Target number 00 - 99</li>
 * <li> Latitude, N/S</li>
 * <li> Longitude, E/W</li>
 * <li> Target name</li>
 * <li> UTC of data</li>
 * <li> Target status(see note)</li>
    L = lost,tracked target has beenlost
    Q = query,target in the process of acquisition
    T = tracking
 * <li> Reference target=R,null otherwise</li>
 * <li> Checksum</li>
 * </ol>
 * 
 * @author ChrisToenjesDeye
 * 
 */
public class TLLSentence extends Sentence{

	/** Default begin char for this sentence type. */
	public static final char BEGIN_CHAR = '$';
	/** Default talker for this sentence. */
	public static final String DEFAULT_TALKER = "RA";
	/** Sentence id. */
	public static final String SENTENCE_ID = "TTL";
	/** Default count of fields. */
	public static final int FIELD_COUNT = 9;
	
	
	/** Target Number */
	private TargetNumber targetNumber;
	/** Latitude */
	private Double latitude;
	/** N or S */
	private Hemisphere latitudeHem;
	/** Longitude */
	private Double longitude;
	/** E or W */
	private Hemisphere longitudeHem;
	/** Target name */
	private String targetLabel;
	/** Time (UTC) */
	private Time time;
	/** Target Status (T = Target, L = Lost target, Q = Target being acquired) */
	private TargetStatus targetStatus;
	/** Reference Target (R = Reference target, blank = designated fixed target) */
	private TargetReference referenceTarget;
	
	
	/**
	 * Default constructor for writing. Empty Sentence to fill attributes and
	 * call {@link #toNMEA()}.
	 */
	public TLLSentence() {
		super(BEGIN_CHAR, DEFAULT_TALKER, SENTENCE_ID, FIELD_COUNT);
	}
	
	/**
	 * Default constructor for parsing.
	 * @param nmea
	 * Nmea String to be parsed.
	 */
	public TLLSentence(String nmea) {
		super(nmea);
	}
	
	/**
	 * Constructor for creating a message from a map. Reverse function of fillMap()
	 * 
	 * @param source
	 *            Map containing specific keys.
	 */		
	public TLLSentence(Map<String, Object> source)	
	{
		super(source, FIELD_COUNT);
		
		if (source.containsKey("targetNumber")) targetNumber = ParseUtils.parseTargetNumber((String) source.get("targetNumber"));
		if (source.containsKey("latitude")) latitude = ParseUtils.parseCoordinate((String) source.get("latitude"));
		if (source.containsKey("latitudeHem")) latitudeHem = ParseUtils.parseHemisphere((String) source.get("latitudeHem"));
		if (source.containsKey("longitude")) longitude = ParseUtils.parseCoordinate((String) source.get("longitude"));
		if (source.containsKey("longitudeHem")) longitudeHem = ParseUtils.parseHemisphere((String) source.get("longitudeHem"));
		if (source.containsKey("targetLabel")) targetLabel = (String) source.get("targetLabel");		
		time = Time.fromMap("time", source);
		if (source.containsKey("targetStatus")) targetStatus = ParseUtils.parseTargetStatus((String) source.get("targetStatus"));
		if (source.containsKey("referenceTarget")) referenceTarget = ParseUtils.parseTargetReference((String) source.get("referenceTarget"));
		
		throw new UnsupportedOperationException("time from map not implemented yet!");
	}				
	
	@Override
	protected void decode() {
		int index = 0;
		targetNumber = ParseUtils.parseTargetNumber(getValue(index++));
		latitude = ParseUtils.parseCoordinate(getValue(index++));
		latitudeHem = ParseUtils.parseHemisphere(getValue(index++));
		longitude = ParseUtils.parseCoordinate(getValue(index++));
		longitudeHem = ParseUtils.parseHemisphere(getValue(index++));
		targetLabel = getValue(index++);
		time = ParseUtils.parseTime(getValue(index++));
		targetStatus = ParseUtils.parseTargetStatus(getValue(index++));
		referenceTarget = ParseUtils.parseTargetReference(getValue(index++));
	}

	@Override
	protected void encode() {
		int index = 0;
		setValue(index++, ParseUtils.toString(targetNumber));
		setValue(index++, ParseUtils.toString(latitude, 2));
		setValue(index++, ParseUtils.toString(latitudeHem));
		setValue(index++, ParseUtils.toString(longitude, 3));
		setValue(index++, ParseUtils.toString(longitudeHem));
		setValue(index++, targetLabel);
		setValue(index++, ParseUtils.toString(time));
		setValue(index++, ParseUtils.toString(targetStatus));
		setValue(index++, ParseUtils.toString(referenceTarget));
	}

	@Override
	protected void fillMap(Map<String, Object> res) {
		if (targetNumber != null) res.put("targetNumber", targetNumber.getNumber());
		if (latitude != null) res.put("latitude", latitude);
		if (latitudeHem != Hemisphere.NULL) res.put("latitudeHem", latitudeHem.name());
		if (longitude != null) res.put("longitude", longitude);
		if (longitudeHem != Hemisphere.NULL) res.put("longitudeHem", longitudeHem.name());
		if (targetLabel != null) res.put("targetLabel", targetLabel);
		if (time != null) time.addToMap("time", res);
		if (targetStatus != TargetStatus.NULL) res.put("targetStatus", targetStatus.name());
		if (referenceTarget != null) res.put("referenceTarget", referenceTarget);
	}

	public TargetNumber getTargetNumber() {
		return targetNumber;
	}

	public void setTargetNumber(TargetNumber targetNumber) {
		this.targetNumber = targetNumber;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Hemisphere getLatitudeHem() {
		return latitudeHem;
	}

	public void setLatitudeHem(Hemisphere latitudeHem) {
		this.latitudeHem = latitudeHem;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Hemisphere getLongitudeHem() {
		return longitudeHem;
	}

	public void setLongitudeHem(Hemisphere longitudeHem) {
		this.longitudeHem = longitudeHem;
	}

	public String getTargetLabel() {
		return targetLabel;
	}

	public void setTargetLabel(String targetLabel) {
		this.targetLabel = targetLabel;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public TargetReference getReferenceTarget() {
		return referenceTarget;
	}

	public void setReferenceTarget(TargetReference referenceTarget) {
		this.referenceTarget = referenceTarget;
	}

	public TargetStatus getTargetStatus() {
		return targetStatus;
	}

	public void setTargetStatus(TargetStatus targetStatus) {
		this.targetStatus = targetStatus;
	}

}
