package de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.decoded;

import java.util.Map;

import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.AISMessageType;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.MMSI;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.ReportingInterval;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.ShipType;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.StationType;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.TxRxMode;

/**
 * 
 */

public class GroupAssignmentCommand extends DecodedAISPayload {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 464584230705346361L;
	
	private final Float northEastLatitude;
	private final Float northEastLongitude;
	private final Float southWestLatitude;
	private final Float southWestLongitude;
	private final StationType stationType;
	private final ShipType shipType;
	private final TxRxMode transmitReceiveMode;
	private final ReportingInterval reportingInterval;
	private final Integer quietTime;
	
	public GroupAssignmentCommand(String originalNmea,
			Integer repeatIndicator, MMSI sourceMmsi, Float northEastLatitude,
			Float northEastLongitude, Float southWestLatitude,
			Float southWestLongitude, StationType stationType,
			ShipType shipType, TxRxMode transmitReceiveMode,
			ReportingInterval reportingInterval, Integer quietTime) {
		super(originalNmea, AISMessageType.GroupAssignmentCommand, repeatIndicator, sourceMmsi);
		this.northEastLatitude = northEastLatitude;
		this.northEastLongitude = northEastLongitude;
		this.southWestLatitude = southWestLatitude;
		this.southWestLongitude = southWestLongitude;
		this.stationType = stationType;
		this.shipType = shipType;
		this.transmitReceiveMode = transmitReceiveMode;
		this.reportingInterval = reportingInterval;
		this.quietTime = quietTime;
	}
	public final Float getNorthEastLatitude() {
		return northEastLatitude;
	}
	public final Float getNorthEastLongitude() {
		return northEastLongitude;
	}
	public final Float getSouthWestLatitude() {
		return southWestLatitude;
	}
	public final Float getSouthWestLongitude() {
		return southWestLongitude;
	}
	public final StationType getStationType() {
		return stationType;
	}
	public final ShipType getShipType() {
		return shipType;
	}
	public final TxRxMode getTransmitReceiveMode() {
		return transmitReceiveMode;
	}
	public final ReportingInterval getReportingInterval() {
		return reportingInterval;
	}
	public final Integer getQuietTime() {
		return quietTime;
	}
	@Override
	public void fillMap(Map<String, Object> res) {
		if (northEastLatitude != null) res.put("northEastLatitude", northEastLatitude);
		if (northEastLongitude != null) res.put("northEastLongitude", northEastLongitude);
		if (southWestLatitude != null) res.put("southWestLatitude", southWestLatitude);
		if (southWestLongitude != null) res.put("southWestLongitude", southWestLongitude);
		if (stationType != null) res.put("stationType", stationType);
		if (shipType != null) res.put("shipType", shipType);
		if (transmitReceiveMode != null) res.put("transmitReceiveMode", transmitReceiveMode);
		if (reportingInterval != null) res.put("reportingInterval", reportingInterval);
		if (quietTime != null) res.put("quietTime", quietTime);
	}
}
