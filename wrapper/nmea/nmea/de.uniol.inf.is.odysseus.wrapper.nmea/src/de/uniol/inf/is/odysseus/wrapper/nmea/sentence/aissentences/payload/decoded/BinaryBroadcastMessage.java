package de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.decoded;

import java.util.Map;

import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.AISMessageType;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.MMSI;

/**
 * 
 * 
 */

public class BinaryBroadcastMessage extends DecodedAISPayload {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7702966671077548107L;
	
	private final Integer designatedAreaCode;
	private final Integer functionalId;
	private final String binaryData;
	
	public BinaryBroadcastMessage(String originalNmea, Integer repeatIndicator, MMSI sourceMmsi,
			Integer designatedAreaCode, Integer functionalId, String binaryData) {
		super(originalNmea, AISMessageType.BinaryBroadcastMessage, repeatIndicator,
				sourceMmsi);
		this.designatedAreaCode = designatedAreaCode;
		this.functionalId = functionalId;
		this.binaryData = binaryData;
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
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BinaryBroadcastMessage [designatedAreaCode=")
				.append(designatedAreaCode).append(", functionalId=")
				.append(functionalId).append(", binaryData=")
				.append(binaryData).append("]");
		return builder.toString();
	}

	@Override
	public void fillMap(Map<String, Object> res) {
		if (designatedAreaCode != null) res.put("designatedAreaCode", designatedAreaCode);
		if (functionalId != null) res.put("functionalId", functionalId);
		if (binaryData != null) res.put("binaryData", binaryData);
	}
}
