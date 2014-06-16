package de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.decoded;

import java.util.Map;

import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.AISMessageType;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.MMSI;

public class StandardSARAircraftPositionReport extends DecodedAISPayload {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6474280807670439357L;
	
	private final Integer altitude;
	private final Integer speed;
	private final Boolean positionAccurate;
	private final Float latitude;
	private final Float longitude;
	private final Float courseOverGround;
	private final Integer second;
	private final String regionalReserved;
	private final Boolean dataTerminalReady;
	private final Boolean assigned;
	private final Boolean raimFlag;
	private final String radioStatus;
	
	public StandardSARAircraftPositionReport(String originalNmea,
			Integer repeatIndicator, MMSI sourceMmsi, Integer altitude,
			Integer speed, Boolean positionAccurate, Float latitude,
			Float longitude, Float courseOverGround, Integer second,
			String regionalReserved, Boolean dataTerminalReady,
			Boolean assigned, Boolean raimFlag, String radioStatus) {
		super(originalNmea, AISMessageType.StandardSARAircraftPositionReport, repeatIndicator, sourceMmsi);
		this.altitude = altitude;
		this.speed = speed;
		this.positionAccurate = positionAccurate;
		this.latitude = latitude;
		this.longitude = longitude;
		this.courseOverGround = courseOverGround;
		this.second = second;
		this.regionalReserved = regionalReserved;
		this.dataTerminalReady = dataTerminalReady;
		this.assigned = assigned;
		this.raimFlag = raimFlag;
		this.radioStatus = radioStatus;
	}

	public final Integer getAltitude() {
		return altitude;
	}

	public final Integer getSpeed() {
		return speed;
	}

	public final Boolean getPositionAccurate() {
		return positionAccurate;
	}

	public final Float getLatitude() {
		return latitude;
	}

	public final Float getLongitude() {
		return longitude;
	}

	public final Float getCourseOverGround() {
		return courseOverGround;
	}

	public final Integer getSecond() {
		return second;
	}

	public final String getRegionalReserved() {
		return regionalReserved;
	}

	public final Boolean getDataTerminalReady() {
		return dataTerminalReady;
	}

	public final Boolean getAssigned() {
		return assigned;
	}

	public final Boolean getRaimFlag() {
		return raimFlag;
	}

	public final String getRadioStatus() {
		return radioStatus;
	}

	@Override
	public void fillMap(Map<String, Object> res) {
		if (altitude != null) res.put("altitude", altitude);
		if (speed != null) res.put("speed", speed);
		if (positionAccurate != null) res.put("positionAccurate", positionAccurate);
		if (latitude != null) res.put("latitude", latitude);
		if (longitude != null) res.put("longitude", longitude);
		if (courseOverGround != null) res.put("courseOverGround", courseOverGround);
		if (second != null) res.put("second", second);
		if (regionalReserved != null) res.put("regionalReserved", regionalReserved);
		if (dataTerminalReady != null) res.put("dataTerminalReady", dataTerminalReady);
		if (assigned != null) res.put("assigned", assigned);
		if (raimFlag != null) res.put("raimFlag", raimFlag);
		if (radioStatus != null) res.put("radioStatus", radioStatus);
	}
}
