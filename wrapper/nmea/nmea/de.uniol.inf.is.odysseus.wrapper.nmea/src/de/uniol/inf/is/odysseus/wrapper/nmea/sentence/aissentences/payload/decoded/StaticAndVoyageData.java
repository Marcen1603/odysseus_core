package de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.decoded;

import java.util.Map;

import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.AISMessageType;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.IMO;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.MMSI;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.PositionFixingDevice;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.ShipType;

/**
 * 
 */

public class StaticAndVoyageData extends DecodedAISPayload {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2223457134318263621L;
	
	private final IMO imo;
	private final String callsign;
	private final String shipName;
	private final ShipType shipType;
	private final Integer toBow;
	private final Integer toStern;
	private final Integer toStarboard;
	private final Integer toPort;
	private final PositionFixingDevice positionFixingDevice;
	private final String eta;
	private final Float draught;
	private final String destination;
	private final Boolean dataTerminalReady;
	
	public StaticAndVoyageData(String originalNmea, Integer repeatIndicator,
			MMSI mmsi, IMO imo, String callsign, String shipName,
			ShipType shipType, Integer toBow, Integer toStern,
			Integer toStarboard, Integer toPort,
			PositionFixingDevice positionFixingDevice, String eta, Float draught,
			String destination, Boolean dataTerminalReady) {
		super(originalNmea, AISMessageType.StaticAndVoyageRelatedData, repeatIndicator, mmsi);
		this.imo = imo;
		this.callsign = callsign;
		this.shipName = shipName;
		this.shipType = shipType;
		this.toBow = toBow;
		this.toStern = toStern;
		this.toStarboard = toStarboard;
		this.toPort = toPort;
		this.positionFixingDevice = positionFixingDevice;
		this.eta = eta;
		this.draught = draught;
		this.destination = destination;
		this.dataTerminalReady = dataTerminalReady;
	}
	
	public final IMO getImo() {
		return imo;
	}

	public final String getCallsign() {
		return callsign;
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

	public final String getEta() {
		return eta;
	}

	public final Float getDraught() {
		return draught;
	}

	public final String getDestination() {
		return destination;
	}

	public final Boolean getDataTerminalReady() {
		return dataTerminalReady;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("StaticAndVoyageData [imo=").append(imo)
				.append(", callsign=").append(callsign).append(", shipName=")
				.append(shipName).append(", shipType=").append(shipType)
				.append(", toBow=").append(toBow).append(", toStern=")
				.append(toStern).append(", toStarboard=").append(toStarboard)
				.append(", toPort=").append(toPort)
				.append(", positionFixingDevice=").append(positionFixingDevice)
				.append(", eta=").append(eta).append(", draught=")
				.append(draught).append(", destination=").append(destination)
				.append(", dataTerminalReady=").append(dataTerminalReady)
				.append("]");
		return builder.toString();
	}

	@Override
	public void fillMap(Map<String, Object> res) {	
		if (imo != null) res.put("imo", imo);
		if (callsign != null) res.put("callsign", callsign);
		if (shipName != null) res.put("shipName", shipName);
		if (shipType != null) res.put("shipType", shipType);
		if (toBow != null) res.put("toBow", toBow);
		if (toStern != null) res.put("toStern", toStern);
		if (toStarboard != null) res.put("toStarboard", toStarboard);
		if (toPort != null) res.put("toPort", toPort);
		if (positionFixingDevice != null) res.put("positionFixingDevice", positionFixingDevice);
		if (eta != null) res.put("eta", eta);
		if (draught != null) res.put("draught", draught);
		if (destination != null) res.put("destination", destination);
		if (dataTerminalReady != null) res.put("dataTerminalReady", dataTerminalReady);
		if (getSourceMmsi() != null) res.put("sourceMmsi", getSourceMmsi().getMMSI());
	}
}
