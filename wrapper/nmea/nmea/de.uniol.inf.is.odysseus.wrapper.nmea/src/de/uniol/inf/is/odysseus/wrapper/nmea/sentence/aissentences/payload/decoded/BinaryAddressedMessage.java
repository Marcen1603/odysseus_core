package de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.decoded;

import java.util.Map;

import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.AISMessageType;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.MMSI;

/**
 * 
 * 
 */

public class BinaryAddressedMessage extends DecodedAISPayload {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4929275421942010922L;
	
	private final Integer sequenceNumber;
	private final MMSI destinationMmsi;
	private final Boolean retransmit;
	private final Integer designatedAreaCode;
	private final Integer functionalId;
	private final String binaryData;
	
	public BinaryAddressedMessage(String originalNmea,
			Integer repeatIndicator, MMSI sourceMmsi, Integer sequenceNumber,
			MMSI destinationMmsi, Boolean retransmit,
			Integer designatedAreaCode, Integer functionalId, String binaryData) {
		super(originalNmea, AISMessageType.BinaryAddressedMessage, repeatIndicator, sourceMmsi);
		this.sequenceNumber = sequenceNumber;
		this.destinationMmsi = destinationMmsi;
		this.retransmit = retransmit;
		this.designatedAreaCode = designatedAreaCode;
		this.functionalId = functionalId;
		this.binaryData = binaryData;
	}

	public final Integer getSequenceNumber() {
		return sequenceNumber;
	}

	public final MMSI getDestinationMmsi() {
		return destinationMmsi;
	}

	public final Boolean getRetransmit() {
		return retransmit;
	}

	public final Integer getDesignatedAreaCode() {
		return designatedAreaCode;
	}

	public final Integer getFunctionalId() {
		return functionalId;
	}

	public final String getBinaryData() {
		return binaryData;
	}

	@Override
	public void fillMap(Map<String, Object> res) {
		if (sequenceNumber != null) res.put("sequenceNumber", sequenceNumber);
		if (destinationMmsi != null) res.put("destinationMmsi", destinationMmsi);
		if (retransmit != null) res.put("retransmit", retransmit);
		if (designatedAreaCode != null) res.put("designatedAreaCode", designatedAreaCode);
		if (functionalId != null) res.put("functionalId", functionalId);
		if (binaryData != null) res.put("binaryData", binaryData);
	}
}
