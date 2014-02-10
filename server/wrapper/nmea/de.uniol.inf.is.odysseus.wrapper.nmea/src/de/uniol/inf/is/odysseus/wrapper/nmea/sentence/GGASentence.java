package de.uniol.inf.is.odysseus.wrapper.nmea.sentence;

import java.util.Date;
import java.util.Map;

import de.uniol.inf.is.odysseus.wrapper.nmea.data.GpsQuality;
import de.uniol.inf.is.odysseus.wrapper.nmea.data.Hemisphere;
import de.uniol.inf.is.odysseus.wrapper.nmea.data.Unit;
import de.uniol.inf.is.odysseus.wrapper.nmea.util.ParseUtils;

public class GGASentence extends Sentence {
	public static final char BEGIN_CHAR = '$';
	public static final String DEFAULT_TALKER = "GP";
	public static final String SENTENCE_ID = "GGA";
	public static final int FIELD_COUNT = 14;
	
	private Date time;
	private Double latitude;
	private Hemisphere latitudeHem;
	private Double longitude;
	private Hemisphere longitudeHem;
	private GpsQuality gpsQuality;
	private Integer numberOfSattelites;
	private Double horizontalDilution;
	private Double antennaAltitude;
	private Unit antennaAltUnits;
	private Double geoidalSeparation;
	private Unit geoidalSepUnits;
	private Double ageOfDgps;
	private Integer differentialRefId;
	
	public GGASentence() {
		super(BEGIN_CHAR, DEFAULT_TALKER, SENTENCE_ID, FIELD_COUNT);
	}
	
	public GGASentence(String nmea) {
		super(nmea);
	}

	@Override
	protected void decode() {
		int index = 0;
		time = ParseUtils.parseUTCTime(getValue(index++));
		latitude = ParseUtils.parseCoordinate(getValue(index++));
		latitudeHem = ParseUtils.parseHemisphere(getValue(index++));
		longitude = ParseUtils.parseCoordinate(getValue(index++));
		longitudeHem = ParseUtils.parseHemisphere(getValue(index++));
		gpsQuality = ParseUtils.parseGpsQuality(getValue(index++));
		numberOfSattelites = ParseUtils.parseInteger(getValue(index++));
		horizontalDilution = ParseUtils.parseDouble(getValue(index++));
		antennaAltitude = ParseUtils.parseDouble(getValue(index++));
		antennaAltUnits = ParseUtils.parseUnit(getValue(index++));
		geoidalSeparation = ParseUtils.parseDouble(getValue(index++));
		geoidalSepUnits = ParseUtils.parseUnit(getValue(index++));
		ageOfDgps = ParseUtils.parseDouble(getValue(index++));
		differentialRefId = ParseUtils.parseInteger(getValue(index++));
	}

	@Override
	protected void encode() {
		int index = 0;
		setValue(index++, ParseUtils.getTime(time));
		setValue(index++, ParseUtils.toString(latitude, 2));
		setValue(index++, ParseUtils.toString(latitudeHem));
		setValue(index++, ParseUtils.toString(latitude, 3));
		setValue(index++, ParseUtils.toString(longitudeHem));
		setValue(index++, ParseUtils.toString(gpsQuality));
		setValue(index++, ParseUtils.toString(numberOfSattelites));
		setValue(index++, ParseUtils.toString(horizontalDilution));
		setValue(index++, ParseUtils.toString(antennaAltitude));
		setValue(index++, ParseUtils.toString(antennaAltUnits));
		setValue(index++, ParseUtils.toString(geoidalSeparation));
		setValue(index++, ParseUtils.toString(geoidalSepUnits));
		setValue(index++, ParseUtils.toString(ageOfDgps));
		setValue(index++, ParseUtils.toString(differentialRefId));
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

	public GpsQuality getGpsQuality() {
		return gpsQuality;
	}

	public void setGpsQuality(GpsQuality gpsQuality) {
		this.gpsQuality = gpsQuality;
	}

	public Integer getNumberOfSattelites() {
		return numberOfSattelites;
	}

	public void setNumberOfSattelites(Integer numberOfSattelites) {
		this.numberOfSattelites = numberOfSattelites;
	}

	public Double getHorizontalDilution() {
		return horizontalDilution;
	}

	public void setHorizontalDilution(Double horizontalDilution) {
		this.horizontalDilution = horizontalDilution;
	}

	public Double getAntennaAltitude() {
		return antennaAltitude;
	}

	public void setAntennaAltitude(Double antennaAltitude) {
		this.antennaAltitude = antennaAltitude;
	}

	public Unit getAntennaAltUnits() {
		return antennaAltUnits;
	}

	public void setAntennaAltUnits(Unit antennaAltUnits) {
		this.antennaAltUnits = antennaAltUnits;
	}

	public Double getGeoidalSeparation() {
		return geoidalSeparation;
	}

	public void setGeoidalSeparation(Double geoidalSeparation) {
		this.geoidalSeparation = geoidalSeparation;
	}

	public Unit getGeoidalSepUnits() {
		return geoidalSepUnits;
	}

	public void setGeoidalSepUnits(Unit geoidalSepUnits) {
		this.geoidalSepUnits = geoidalSepUnits;
	}

	public Double getAgeOfDgps() {
		return ageOfDgps;
	}

	public void setAgeOfDgps(Double ageOfDgps) {
		this.ageOfDgps = ageOfDgps;
	}

	public Integer getDifferentialRefId() {
		return differentialRefId;
	}

	public void setDifferentialRefId(Integer differentialRefId) {
		this.differentialRefId = differentialRefId;
	}
}
