package de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.decoded;

import java.util.Map;

import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.AISMessageType;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.MMSI;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.ShipType;

public class StaticDataReport extends DecodedAISPayload {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1833249589260757573L;
	
	private final Integer partNumber;
	private final String shipName;
	private final ShipType shipType;
	private final String vendorId;
	private final String callsign;
	private final Integer toBow;
	private final Integer toStern;
	private final Integer toStarboard;
	private final Integer toPort;
	private final MMSI mothershipMmsi;
	
	public StaticDataReport(String originalNmea,
			Integer repeatIndicator, MMSI sourceMmsi, Integer partNumber,
			String shipName, ShipType shipType, String vendorId,
			String callsign, Integer toBow, Integer toStern,
			Integer toStarboard, Integer toPort, MMSI mothershipMmsi) {
		super(originalNmea, AISMessageType.StaticDataReport, repeatIndicator, sourceMmsi);
		this.partNumber = partNumber;
		this.shipName = shipName;
		this.shipType = shipType;
		this.vendorId = vendorId;
		this.callsign = callsign;
		this.toBow = toBow;
		this.toStern = toStern;
		this.toStarboard = toStarboard;
		this.toPort = toPort;
		this.mothershipMmsi = mothershipMmsi;
	}

	public final Integer getPartNumber() {
		return partNumber;
	}

	public final String getShipName() {
		return shipName;
	}

	public final ShipType getShipType() {
		return shipType;
	}

	public final String getVendorId() {
		return vendorId;
	}

	public final String getCallsign() {
		return callsign;
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

	public final MMSI getMothershipMmsi() {
		return mothershipMmsi;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("StaticDataReport [partNumber=")
				.append(partNumber).append(", shipName=").append(shipName)
				.append(", shipType=").append(shipType).append(", vendorId=")
				.append(vendorId).append(", callsign=").append(callsign)
				.append(", toBow=").append(toBow).append(", toStern=")
				.append(toStern).append(", toStarboard=").append(toStarboard)
				.append(", toPort=").append(toPort).append(", mothershipMmsi=")
				.append(mothershipMmsi).append("]");
		return builder.toString();
	}

	@Override
	public void fillMap(Map<String, Object> res) {
		if (partNumber != null) res.put("partNumber", partNumber);
		if (shipName != null) res.put("shipName", shipName);
		if (shipType != null) res.put("shipType", shipType);
		if (vendorId != null) res.put("vendorId", vendorId);
		if (callsign != null) res.put("callsign", callsign);
		if (toBow != null) res.put("toBow", toBow);
		if (toStern != null) res.put("toStern", toStern);
		if (toStarboard != null) res.put("toStarboard", toStarboard);
		if (toPort != null) res.put("toPort", toPort);
		if (mothershipMmsi != null) res.put("mothershipMmsi", mothershipMmsi);
	}
}
