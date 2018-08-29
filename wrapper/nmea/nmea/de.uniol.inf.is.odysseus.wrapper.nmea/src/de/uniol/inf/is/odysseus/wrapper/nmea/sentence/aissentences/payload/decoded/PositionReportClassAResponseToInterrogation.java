package de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.decoded;

import java.util.Map;

import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.AISMessageType;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.MMSI;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.ManeuverIndicator;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.NavigationStatus;

public class PositionReportClassAResponseToInterrogation extends PositionReport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 453668667509941374L;

	public PositionReportClassAResponseToInterrogation(String originalNmea, AISMessageType messageType,
			Integer repeatIndicator, MMSI sourceMmsi,
			NavigationStatus navigationStatus, Integer rateOfTurn,
			Double speedOverGround, Boolean positionAccurate, Double latitude,
			Double longitude, Double courseOverGround, Integer trueHeading,
			Integer second, ManeuverIndicator maneuverIndicator,
			Boolean raimFlag) {
		super(originalNmea, messageType, repeatIndicator, sourceMmsi, navigationStatus, rateOfTurn,
				speedOverGround, positionAccurate, latitude, longitude,
				courseOverGround, trueHeading, second, maneuverIndicator,
				raimFlag);
	}

	@Override
	public void fillMap(Map<String, Object> res) {
		if (getNavigationStatus() != null) res.put("navigationStatus", getNavigationStatus());
		if (getRateOfTurn() != null) res.put("rateOfTurn", getRateOfTurn());
		if (getSpeedOverGround() != null) res.put("speedOverGround", getSpeedOverGround());
		if (getPositionAccurate() != null) res.put("positionAccurate", getPositionAccurate());
		if (getLatitude() != null) res.put("latitude", getLatitude());
		if (getLongitude() != null) res.put("longitude", getLongitude());
		if (getCourseOverGround() != null) res.put("courseOverGround", getCourseOverGround());
		if (getTrueHeading() != null) res.put("trueHeading", getTrueHeading());
		if (getSecond() != null) res.put("second", getSecond());
		if (getManeuverIndicator() != null) res.put("maneuverIndicator", getManeuverIndicator());
		if (getRaimFlag() != null) res.put("raimFlag", getRaimFlag());
		if (getSourceMmsi().getMMSI() != null) res.put("sourceMmsi", getSourceMmsi().getMMSI());
	}
}
