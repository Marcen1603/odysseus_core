package de.uniol.inf.is.odysseus.wrapper.nmea.sentence;

import java.util.Locale;
import java.util.Map;

import de.uniol.inf.is.odysseus.wrapper.nmea.data.Date;
import de.uniol.inf.is.odysseus.wrapper.nmea.data.Hemisphere;
import de.uniol.inf.is.odysseus.wrapper.nmea.data.SignalIntegrity;
import de.uniol.inf.is.odysseus.wrapper.nmea.data.Status;
import de.uniol.inf.is.odysseus.wrapper.nmea.data.Time;
import de.uniol.inf.is.odysseus.wrapper.nmea.util.ParseUtils;

/**
 * RMC - Recomended Minimum Navigation Information<br>
 * <br>
 * 
 * <pre>
 * {@code
 * .      1         2 3       4 5        6 7   8   9   10  11 12
 *        |         | |       | |        | |   |   |    |   | |
 * $--RMC,hhmmss.ss,A,llll.ll,a,yyyyy.yy,a,x.x,x.x,xxxx,x.x,a*hh
 * }
 * </pre>
 * <ol>
 * <li>Time (UTC)</li>
 * <li>Status, V = Navigation receiver warning</li>
 * <li>Latitude</li>
 * <li>N or S</li>
 * <li>Longitude</li>
 * <li>E or W</li>
 * <li>Speed over ground, knots</li>
 * <li>Track made good, degrees true</li>
 * <li>Date, ddmmyy</li>
 * <li>Magnetic Variation, degrees</li>
 * <li>E or W</li>
 * <li>Checksum</li>
 * </ol>
 * 
 * @author jboger <juergen.boger@offis.de>
 * 
 */
public class RMCSentence extends Sentence {
	/** Default begin char for this sentence type. */
	public static final char BEGIN_CHAR = '$';
	/** Default talker for this sentence. */
	public static final String DEFAULT_TALKER = "GP";
	/** Sentence id. */
	public static final String SENTENCE_ID = "RMC";
	/** Default count of fields. */
	public static final int FIELD_COUNT = 12;
	
	/** Time (UTC) */
	private Time time;
	/** Status, V = Navigation receiver warning */
	private Status status;
	/** Latitude */
	private Double latitude;
	/** N or S */
	private Hemisphere latitudeHem;
	/** Longitude */
	private Double longitude;
	/** E or W */
	private Hemisphere longitudeHem;
	/** Speed over ground, knots */
	private Double speedOverGround;
	/** Track made good, degrees true */
	private Double trackMadeGood;
	/** Date, ddmmyy */
	private Date date;
	/** Magnetic Variation, degrees */
	private Double magneticVariation;
	/** E or W */
	private Hemisphere magneticHem;
	/** Checksum */
	private SignalIntegrity signalIntegrity;

	/**
	 * Default constructor for parsing.
	 * @param nmea
	 * Nmea String to be parsed.
	 */
	public RMCSentence(String nmea) {
		super(nmea);
	}
	
	/**
	 * Default constructor for writing. Empty Sentence to fill attributes and
	 * call {@link #toNMEA()}.
	 */
	public RMCSentence() {
		super(BEGIN_CHAR, DEFAULT_TALKER, SENTENCE_ID, FIELD_COUNT);
	}

	/**
	 * Constructor for creating a message from a map. Reverse function of fillMap()
	 * 
	 * @param source
	 *            Map containing specific keys.
	 */		
	public RMCSentence(Map<String, Object> source)	
	{
		super(source, FIELD_COUNT);
		
		time = Time.fromMap("time", source);
		if (source.containsKey("status")) status = ParseUtils.parseStatus((String) source.get("status"));
		if (source.containsKey("latitude")) latitude = ParseUtils.parseCoordinate((String) source.get("latitude"));
		if (source.containsKey("latitudeHem")) latitudeHem = ParseUtils.parseHemisphere((String) source.get("latitudeHem"));
		if (source.containsKey("longitude")) longitude = ParseUtils.parseCoordinate((String) source.get("longitude"));
		if (source.containsKey("longitudeHem")) longitudeHem = ParseUtils.parseHemisphere((String) source.get("longitudeHem"));
		if (source.containsKey("speedOverGround")) speedOverGround = (double) source.get("speedOverGround");		
		if (source.containsKey("date")) date = ParseUtils.parseDate((String) source.get("date"));
		if (source.containsKey("magneticHem")) magneticHem = ParseUtils.parseHemisphere((String) source.get("magneticHem"));
		if (source.containsKey("signalIntegrity")) signalIntegrity = ParseUtils.parseSignalIntegrity((String) source.get("signalIntegrity"));		
		if (source.containsKey("trackMadeGood")) trackMadeGood = (double) source.get("trackMadeGood");
		if (source.containsKey("magneticVariation")) magneticVariation = (double) source.get("magneticVariation");		
	}				
	
	@Override
	protected void decode() {
		int index = 0;
		time = ParseUtils.parseTime(getValue(index++));
		status = ParseUtils.parseStatus(getValue(index++));
		latitude = ParseUtils.parseCoordinate(getValue(index++));
		latitudeHem = ParseUtils.parseHemisphere(getValue(index++));
		longitude = ParseUtils.parseCoordinate(getValue(index++));
		longitudeHem = ParseUtils.parseHemisphere(getValue(index++));
		speedOverGround = ParseUtils.parseDouble(getValue(index++));
		trackMadeGood = ParseUtils.parseDouble(getValue(index++));
		date = ParseUtils.parseDate(getValue(index++));
		magneticVariation = ParseUtils.parseDouble(getValue(index++));
		magneticHem = ParseUtils.parseHemisphere(getValue(index++));
		signalIntegrity = ParseUtils.parseSignalIntegrity(getValue(index++));
	}

	@Override
	protected void encode() {
		int index = 0;
		setValue(index++, ParseUtils.toString(time));
		setValue(index++, ParseUtils.toString(status));
		setValue(index++, ParseUtils.toString(latitude, 2));
		setValue(index++, ParseUtils.toString(latitudeHem));
		setValue(index++, ParseUtils.toString(longitude, 3));
		setValue(index++, ParseUtils.toString(longitudeHem));
		setValue(index++, String.format(Locale.US, "%.1f", speedOverGround));
		setValue(index++, String.format(Locale.US, "%.1f", trackMadeGood));
		setValue(index++, ParseUtils.toString(date));
		setValue(index++, String.format(Locale.US, "%.1f", magneticVariation));
		setValue(index++, ParseUtils.toString(magneticHem));
		//setValue(index++, ParseUtils.toString(signalIntegrity));
	}

	@Override
	protected void fillMap(Map<String, Object> res) {
		if (time != null) time.addToMap("time", res);
		if (status != Status.NULL) res.put("status", status.name());
		if (latitude != null) res.put("latitude", latitude);
		if (latitudeHem != Hemisphere.NULL) res.put("latitudeHem", latitudeHem.name());
		if (longitude != null) res.put("longitude", longitude);
		if (longitudeHem != Hemisphere.NULL) res.put("longitudeHem", longitudeHem.name());
		if (speedOverGround != null) res.put("speedOverGround", speedOverGround);
		if (trackMadeGood != null) res.put("trackMadeGood", trackMadeGood);
		if (date != null) date.addToMap("date", res);		
		if (magneticVariation != null) res.put("magneticVariation", magneticVariation);
		if (magneticHem != Hemisphere.NULL) res.put("magneticHem", magneticHem.name());
		if (signalIntegrity != SignalIntegrity.NULL) res.put("signalIntegrity", signalIntegrity.name());
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
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

	public Double getSpeedOverGround() {
		return speedOverGround;
	}

	public void setSpeedOverGround(Double speedOverGround) {
		this.speedOverGround = speedOverGround;
	}

	public Double getTrackMadeGood() {
		return trackMadeGood;
	}

	public void setTrackMadeGood(Double trackMadeGood) {
		this.trackMadeGood = trackMadeGood;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Double getMagneticVariation() {
		return magneticVariation;
	}

	public void setMagneticVariation(Double magneticVariation) {
		this.magneticVariation = magneticVariation;
	}

	public Hemisphere getMagneticHem() {
		return magneticHem;
	}

	public void setMagneticHem(Hemisphere magneticHem) {
		this.magneticHem = magneticHem;
	}

	public SignalIntegrity getSignalIntegrity() {
		return signalIntegrity;
	}

	public void setSignalIntegrity(SignalIntegrity signalIntegrity) {
		this.signalIntegrity = signalIntegrity;
	}
}
