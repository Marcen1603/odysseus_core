package de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.decoded;

import java.util.Map;

import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.AISMessageType;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.MMSI;

public class MultipleSlotBinaryMessageWithCommunicationState extends DecodedAISPayload {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1316312755814915322L;
	
	private final Boolean addressed;
	private final Boolean structured;
	private final MMSI destinationMmsi;
	private final Integer applicationId;
	private final String data;
	private final String radioStatus;
	
	public MultipleSlotBinaryMessageWithCommunicationState(String originalNmea,
			Integer repeatIndicator, MMSI sourceMmsi, Boolean addressed,
			Boolean structured, MMSI destinationMmsi, Integer applicationId,
			String data, String radioStatus) {
		super(originalNmea, AISMessageType.MultipleSlotBinaryMessageWithCommunicationState, repeatIndicator, sourceMmsi);
		this.addressed = addressed;
		this.structured = structured;
		this.destinationMmsi = destinationMmsi;
		this.applicationId = applicationId;
		this.data = data;
		this.radioStatus = radioStatus;
	}
	
	public final Boolean getAddressed() {
		return addressed;
	}
	public final Boolean getStructured() {
		return structured;
	}
	public final MMSI getDestinationMmsi() {
		return destinationMmsi;
	}
	public final Integer getApplicationId() {
		return applicationId;
	}
	public final String getData() {
		return data;
	}
	public final String getRadioStatus() {
		return radioStatus;
	}

	@Override
	public void fillMap(Map<String, Object> res) {
		if (addressed != null) res.put("addressed", addressed);
		if (structured != null) res.put("structured", structured);
		if (destinationMmsi != null) res.put("destinationMmsi", destinationMmsi);
		if (applicationId != null) res.put("applicationId", applicationId);
		if (data != null) res.put("data", data);
		if (radioStatus != null) res.put("radioStatus", radioStatus);
	}
}
