package de.uniol.inf.is.odysseus.wrapper.nmea.sentence;

import java.util.Map;

import de.uniol.inf.is.odysseus.wrapper.nmea.data.Hemisphere;
import de.uniol.inf.is.odysseus.wrapper.nmea.data.Status;
import de.uniol.inf.is.odysseus.wrapper.nmea.data.Time;
import de.uniol.inf.is.odysseus.wrapper.nmea.util.ParseUtils;

/**
 * GLL - Geographic Position – Latitude/Longitude<br>
 * <br>
 * 
 * 
 * <pre>
 * {@code
 * .      1       2 3        4 5         6 7
 *        |       | |        | |         | |
 * $--GLL,llll.ll,a,yyyyy.yy,a,hhmmss.ss,A*hh
 * }
 * </pre>
 * <ol>
 * <li>Latitude</li>
 * <li>N or S (North or South)</li>
 * <li>Longitude</li>
 * <li>E or W (East or West)</li>
 * <li>Time (UTC)</li>
 * <li>Status A - Data Valid, V - Data Invalid</li>
 * <li>Checksum</li>
 * </ol>
 * 
 * @author aoppermann <alexander.oppermann@offis.de>
 * 
 */
public class GLLSentence extends Sentence {
	/** Default begin char for this sentence type. */
	public static final char BEGIN_CHAR = '$';
	/** Default talker for this sentence. */
	public static final String DEFAULT_TALKER = "IN";
	/** Sentence id. */
	public static final String SENTENCE_ID = "GLL";
	/** Default count of fields. */
	public static final int FIELD_COUNT = 6;

	/** Latitude */
	private Double latitude;
	/** N or S (North or South) */
	private Hemisphere latitudeHem;
	/** Longitude */
	private Double longitude;
	/** E or W (East or West) */
	private Hemisphere longitudeHem;
	/** Time (UTC) */
	private Time time;
	/** Status A - Data Valid, V - Data Invalid */
	private Status status;

	/**
	 * Default constructor for writing. Empty Sentence to fill attributes and
	 * call {@link #toNMEA()}.
	 */
	public GLLSentence() {
		super(BEGIN_CHAR, DEFAULT_TALKER, SENTENCE_ID, FIELD_COUNT);
	}

	/**
	 * Default constructor for parsing.
	 * 
	 * @param nmea
	 *            Nmea String to be parsed.
	 */
	public GLLSentence(String nmea) {
		super(nmea);
	}

	/**
	 * Constructor for creating a message from a map. Reverse function of fillMap()
	 * 
	 * @param source
	 *            Map containing specific keys.
	 */	
	public GLLSentence(Map<String, Object> source)	
	{
		super(source, FIELD_COUNT);
		
		time = Time.fromMap("time", source);
		if (source.containsKey("latitudeHem")) latitudeHem = ParseUtils.parseHemisphere((String) source.get("latitudeHem"));
		if (source.containsKey("longitudeHem")) longitudeHem = ParseUtils.parseHemisphere((String) source.get("longitudeHem"));
		if (source.containsKey("status")) status = ParseUtils.parseStatus((String) source.get("status"));
		if (source.containsKey("latitude"))	latitude = (Double) source.get("latitude");
		if (source.containsKey("longitude")) longitude = (Double) source.get("longitude");		
	}		
	
	@Override
	protected void decode() {
		int index = 0;
		latitude = ParseUtils.parseCoordinate(getValue(index++));
		latitudeHem = ParseUtils.parseHemisphere(getValue(index++));
		longitude = ParseUtils.parseCoordinate(getValue(index++));
		longitudeHem = ParseUtils.parseHemisphere(getValue(index++));
		time = ParseUtils.parseTime(getValue(index++));
		status = ParseUtils.parseStatus(getValue(index++));
	}

	@Override
	protected void encode() {
		int index = 0;
		setValue(index++, ParseUtils.toString(latitude, 2));
		setValue(index++, ParseUtils.toString(latitudeHem));
		setValue(index++, ParseUtils.toString(longitude, 3));
		setValue(index++, ParseUtils.toString(longitudeHem));
		setValue(index++, ParseUtils.toString(time));
		setValue(index++, ParseUtils.toString(status));
	}

	@Override
	protected void fillMap(Map<String, Object> res) {
		if (latitude != null) res.put("latitude", latitude);
		if (latitudeHem != Hemisphere.NULL) res.put("latitudeHem", latitudeHem.name());
		if (longitude != null) res.put("longitude", longitude);
		if (longitudeHem != Hemisphere.NULL) res.put("longitudeHem", longitudeHem.name());
		if (time != null) time.addToMap("time", res);
		if (status != Status.NULL) res.put("status", status.name());
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

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}
	
	public Status getStatus(){
		return status;
	}
	
	public void setStatus(Status status){
		this.status = status;
	}
}
