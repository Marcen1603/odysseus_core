package de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.decoded;

import java.util.Map;

import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.AISMessageType;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.MMSI;


public class SafetyRelatedBroadcastMessage extends DecodedAISPayload {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6056524571343671498L;
	
	private final String text;

	public SafetyRelatedBroadcastMessage(String originalNmea, Integer repeatIndicator, MMSI sourceMmsi, String text) {
		super(originalNmea, AISMessageType.SafetyRelatedBroadcastMessage, repeatIndicator, sourceMmsi);
		this.text = text;
	}

	public final String getText() {
		return text;
	}

	@Override
	public void fillMap(Map<String, Object> res) {
		if (text != null) res.put("text", text);
	}
}
