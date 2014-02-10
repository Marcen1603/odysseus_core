package de.uniol.inf.is.odysseus.wrapper.nmea.sentence;

import java.util.Date;
import java.util.Map;

import de.uniol.inf.is.odysseus.wrapper.nmea.data.Hemisphere;
import de.uniol.inf.is.odysseus.wrapper.nmea.data.SignalIntegrity;
import de.uniol.inf.is.odysseus.wrapper.nmea.data.Status;
import de.uniol.inf.is.odysseus.wrapper.nmea.util.ParseUtils;

public class RMCSentence extends Sentence {
	public static final char BEGIN_CHAR = '$';
	public static final String DEFAULT_TALKER = "GP";
	public static final String SENTENCE_ID = "RMC";
	public static final int FIELD_COUNT = 12;
	
	private Date time;
	private Status status;
	private Double latitude;
	private Hemisphere latitudeHem;
	private Double longitude;
	private Hemisphere longitudeHem;
	private Double speedOverGround;
	private Double trackMadeGood;
	private Date date;
	private Double magneticVariation;
	private Hemisphere magneticHem;
	private SignalIntegrity signalIntegrity;

	public RMCSentence(String nmea) {
		super(nmea);
	}
	
	public RMCSentence() {
		super(BEGIN_CHAR, DEFAULT_TALKER, SENTENCE_ID, FIELD_COUNT);
	}

	@Override
	protected void decode() {
		int index = 0;
		time = ParseUtils.parseUTCTime(getValue(index++));
		status = ParseUtils.parseStatus(getValue(index++));
		latitude = ParseUtils.parseCoordinate(getValue(index++));
		latitudeHem = ParseUtils.parseHemisphere(getValue(index++));
		longitude = ParseUtils.parseCoordinate(getValue(index++));
		longitudeHem = ParseUtils.parseHemisphere(getValue(index++));
		speedOverGround = ParseUtils.parseDouble(getValue(index++));
		trackMadeGood = ParseUtils.parseDouble(getValue(index++));
		date = ParseUtils.parseUTCDate(getValue(index++));
		magneticVariation = ParseUtils.parseDouble(getValue(index++));
		magneticHem = ParseUtils.parseHemisphere(getValue(index++));
		signalIntegrity = ParseUtils.parseSignalIntegrity(getValue(index++));
	}

	@Override
	protected void encode() {
		int index = 0;
		setValue(index++, ParseUtils.getTime(time));
		setValue(index++, ParseUtils.toString(status));
		setValue(index++, ParseUtils.toString(latitude, 2));
		setValue(index++, ParseUtils.toString(latitudeHem));
		setValue(index++, ParseUtils.toString(longitude, 3));
		setValue(index++, ParseUtils.toString(longitudeHem));
		setValue(index++, ParseUtils.toString(speedOverGround));
		setValue(index++, ParseUtils.toString(trackMadeGood));
		setValue(index++, ParseUtils.getDate(date));
		setValue(index++, ParseUtils.toString(magneticVariation));
		setValue(index++, ParseUtils.toString(magneticHem));
		setValue(index++, ParseUtils.toString(signalIntegrity));
	}

	@Override
	public void fillMap(Map<String, Object> res) {
		if (latitude != null) res.put("latitude", ParseUtils.toString(latitude, 2));
		if (longitude != null) res.put("longitude", ParseUtils.toString(longitude, 3));
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
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
