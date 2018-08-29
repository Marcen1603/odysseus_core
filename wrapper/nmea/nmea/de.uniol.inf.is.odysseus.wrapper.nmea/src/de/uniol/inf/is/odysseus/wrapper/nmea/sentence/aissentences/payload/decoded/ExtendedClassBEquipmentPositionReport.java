package de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.decoded;

import java.util.Map;

import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.AISMessageType;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.MMSI;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.PositionFixingDevice;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.ShipType;

public class ExtendedClassBEquipmentPositionReport extends DecodedAISPayload {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7308814923522151815L;
	
	private final String regionalReserved1;
	private final Float speedOverGround;
	private final Boolean positionAccurate;
	private final Float latitude;
	private final Float longitude;
	private final Float courseOverGround;
	private final Integer trueHeading;
	private final Integer second;
	private final String regionalReserved2;
	private final String shipName;
	private final ShipType shipType;
	private final Integer toBow;
	private final Integer toStern;
	private final Integer toStarboard;
	private final Integer toPort;
	private final PositionFixingDevice positionFixingDevice;
	private final Boolean raimFlag;
	private final Boolean dataTerminalReady;
	private final Boolean assigned;
	
	public ExtendedClassBEquipmentPositionReport(String originalNmea,
			Integer repeatIndicator, MMSI sourceMmsi, String regionalReserved1,
			Float speedOverGround, Boolean positionAccurate, Float latitude,
			Float longitude, Float courseOverGround, Integer trueHeading,
			Integer second, String regionalReserved2, String shipName,
			ShipType shipType, Integer toBow, Integer toStern,
			Integer toStarboard, Integer toPort,
			PositionFixingDevice positionFixingDevice, Boolean raimFlag,
			Boolean dataTerminalReady, Boolean assigned) {
		super(originalNmea, AISMessageType.ExtendedClassBEquipmentPositionReport, repeatIndicator, sourceMmsi);
		this.regionalReserved1 = regionalReserved1;
		this.speedOverGround = speedOverGround;
		this.positionAccurate = positionAccurate;
		this.latitude = latitude;
		this.longitude = longitude;
		this.courseOverGround = courseOverGround;
		this.trueHeading = trueHeading;
		this.second = second;
		this.regionalReserved2 = regionalReserved2;
		this.shipName = shipName;
		this.shipType = shipType;
		this.toBow = toBow;
		this.toStern = toStern;
		this.toStarboard = toStarboard;
		this.toPort = toPort;
		this.positionFixingDevice = positionFixingDevice;
		this.raimFlag = raimFlag;
		this.dataTerminalReady = dataTerminalReady;
		this.assigned = assigned;
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

	public final String getShipName() {
		return shipName;
	}

	public final ShipType getShipType() {
		return shipType;
	}

	public final Integer getToBow() {
		return toBow;
	}

	public final Integer getToStern() {
		return toStern;
	}

	public final Integer getToStarboard() {
		return toStarboard;
	}

	public final Integer getToPort() {
		return toPort;
	}

	public final PositionFixingDevice getPositionFixingDevice() {
		return positionFixingDevice;
	}

	public final Boolean getRaimFlag() {
		return raimFlag;
	}

	public final Boolean getDataTerminalReady() {
		return dataTerminalReady;
	}

	public final Boolean getAssigned() {
		return assigned;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(
				"ExtendedClassBEquipmentPositionReport [regionalReserved1=")
				.append(regionalReserved1).append(", speedOverGround=")
				.append(speedOverGround).append(", positionAccurate=")
				.append(positionAccurate).append(", latitude=")
				.append(latitude).append(", longitude=").append(longitude)
				.append(", courseOverGround=").append(courseOverGround)
				.append(", trueHeading=").append(trueHeading)
				.append(", second=").append(second)
				.append(", regionalReserved2=").append(regionalReserved2)
				.append(", shipName=").append(shipName).append(", shipType=")
				.append(shipType).append(", toBow=").append(toBow)
				.append(", toStern=").append(toStern).append(", toStarboard=")
				.append(toStarboard).append(", toPort=").append(toPort)
				.append(", positionFixingDevice=").append(positionFixingDevice)
				.append(", raimFlag=").append(raimFlag)
				.append(", dataTerminalReady=").append(dataTerminalReady)
				.append(", assigned=").append(assigned).append("]");
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
		if (shipName != null) res.put("shipName", shipName);
		if (shipType != null) res.put("shipType", shipType);
		if (toBow != null) res.put("toBow", toBow);
		if (toStern != null) res.put("toStern", toStern);
		if (toStarboard != null) res.put("toStarboard", toStarboard);
		if (toPort != null) res.put("toPort", toPort);
		if (positionFixingDevice != null) res.put("positionFixingDevice", positionFixingDevice);
		if (raimFlag != null) res.put("raimFlag", raimFlag);
		if (dataTerminalReady != null) res.put("dataTerminalReady", dataTerminalReady);
		if (assigned != null) res.put("assigned", assigned);
	}
}
