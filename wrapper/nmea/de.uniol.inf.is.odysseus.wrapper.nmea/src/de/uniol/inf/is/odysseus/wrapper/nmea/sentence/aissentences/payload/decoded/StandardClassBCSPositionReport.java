package de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.decoded;

import java.util.Map;

import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.AISMessageType;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.MMSI;

/**
 * 
 * 
 */

public class StandardClassBCSPositionReport extends DecodedAISPayload {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5449522125711146975L;
	
	private final String regionalReserved1;
	private final Float speedOverGround;
	private final Boolean positionAccurate;
	private final Float latitude;
	private final Float longitude;
	private final Float courseOverGround;
	private final Integer trueHeading;
	private final Integer second;
	private final String regionalReserved2;
	private final Boolean csUnit;
	private final Boolean display;
	private final Boolean dsc;
	private final Boolean band;
	private final Boolean message22;
	private final Boolean assigned;
	private final Boolean raimFlag;
	private final String radioStatus;
	
	public StandardClassBCSPositionReport(String originalNmea,
			Integer repeatIndicator, MMSI sourceMmsi, String regionalReserved1,
			Float speedOverGround, Boolean positionAccurate, Float latitude,
			Float longitude, Float courseOverGround, Integer trueHeading,
			Integer second, String regionalReserved2, Boolean csUnit,
			Boolean display, Boolean dsc, Boolean band, Boolean message22,
			Boolean assigned, Boolean raimFlag, String radioStatus) {
		super(originalNmea, AISMessageType.StandardClassBCSPositionReport, repeatIndicator, sourceMmsi);
		this.regionalReserved1 = regionalReserved1;
		this.speedOverGround = speedOverGround;
		this.positionAccurate = positionAccurate;
		this.latitude = latitude;
		this.longitude = longitude;
		this.courseOverGround = courseOverGround;
		this.trueHeading = trueHeading;
		this.second = second;
		this.regionalReserved2 = regionalReserved2;
		this.csUnit = csUnit;
		this.display = display;
		this.dsc = dsc;
		this.band = band;
		this.message22 = message22;
		this.assigned = assigned;
		this.raimFlag = raimFlag;
		this.radioStatus = radioStatus;
	}

	public final String getRegionalReserved1() {
		return regionalReserved1;
	}

	public final Float getSpeedOverGround() {
		return speedOverGround;
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

	public final Integer getTrueHeading() {
		return trueHeading;
	}

	public final Integer getSecond() {
		return second;
	}

	public final String getRegionalReserved2() {
		return regionalReserved2;
	}

	public final Boolean getCsUnit() {
		return csUnit;
	}

	public final Boolean getDisplay() {
		return display;
	}

	public final Boolean getDsc() {
		return dsc;
	}

	public final Boolean getBand() {
		return band;
	}

	public final Boolean getMessage22() {
		return message22;
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
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("StandardClassBCSPositionReport [regionalReserved1=")
				.append(regionalReserved1).append(", speedOverGround=")
				.append(speedOverGround).append(", positionAccurate=")
				.append(positionAccurate).append(", latitude=")
				.append(latitude).append(", longitude=").append(longitude)
				.append(", courseOverGround=").append(courseOverGround)
				.append(", trueHeading=").append(trueHeading)
				.append(", second=").append(second)
				.append(", regionalReserved2=").append(regionalReserved2)
				.append(", csUnit=").append(csUnit).append(", display=")
				.append(display).append(", dsc=").append(dsc).append(", band=")
				.append(band).append(", message22=").append(message22)
				.append(", assigned=").append(assigned).append(", raimFlag=")
				.append(raimFlag).append(", radioStatus=").append(radioStatus)
				.append("]");
		return builder.toString();
	}

	@Override
	public void fillMap(Map<String, Object> res) {
		if (regionalReserved1 != null) res.put("regionalReserved1", regionalReserved1);
		if (speedOverGround != null) res.put("speedOverGround", speedOverGround);
		if (positionAccurate != null) res.put("positionAccurate", positionAccurate);
		if (latitude != null) res.put("latitude", latitude);
		if (longitude != null) res.put("longitude", longitude);
		if (courseOverGround != null) res.put("courseOverGround", courseOverGround);
		if (trueHeading != null) res.put("trueHeading", trueHeading);
		if (second != null) res.put("second", second);
		if (regionalReserved2 != null) res.put("regionalReserved2", regionalReserved2);
		if (csUnit != null) res.put("csUnit", csUnit);
		if (display != null) res.put("display", display);
		if (dsc != null) res.put("dsc", dsc);
		if (band != null) res.put("band", band);
		if (message22 != null) res.put("message22", message22);
		if (assigned != null) res.put("assigned", assigned);
		if (raimFlag != null) res.put("raimFlag", raimFlag);
		if (radioStatus != null) res.put("radioStatus", radioStatus);
		
	}
}
