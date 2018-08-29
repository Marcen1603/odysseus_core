package de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.decoded;

import java.util.Map;

import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.AISMessageType;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.MMSI;

/**
 * 
 */

public class DGNSSBinaryBroadcastMessage extends DecodedAISPayload {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2038894109094035078L;
	

	private final Float latitude;
	private final Float longitude;
	private final String binaryData;
	
	public DGNSSBinaryBroadcastMessage(String originalNmea, Integer repeatIndicator, MMSI sourceMmsi, Float latitude, Float longitude, String binaryData) {
		super(originalNmea, AISMessageType.DGNSSBinaryBroadcastMessage, repeatIndicator, sourceMmsi);
		this.latitude = latitude;
		this.longitude = longitude;
		this.binaryData = binaryData;
	}

	public final Float getLatitude() {
		return latitude;
	}

	public final Float getLongitude() {
		return longitude;
	}

	public final String getBinaryData() {
		return binaryData;
	}

	@Override
	public void fillMap(Map<String, Object> res) {
		if (latitude != null) res.put("latitude", latitude);
		if (longitude != null) res.put("longitude", longitude);
		if (binaryData != null) res.put("binaryData", binaryData);
	}
}
