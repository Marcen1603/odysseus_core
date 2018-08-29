package de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.decoded;

import java.io.Serializable;
import java.util.Map;

import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.AISSentence;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.AISMessageType;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.MMSI;

public abstract class DecodedAISPayload extends AISSentence implements Serializable {
	
	/**
	 * This is the base class of all different types of decoded payloadMessages.
	 */
	private static final long serialVersionUID = -7146996338054507695L;
	
	private final AISMessageType messageType;
	private final Integer repeatIndicator;
	private final MMSI sourceMmsi;
	
	protected DecodedAISPayload(String originalNmea, AISMessageType messageType, Integer repeatIndicator, MMSI sourceMmsi) {
		super(originalNmea);
		this.messageType = messageType;
		this.repeatIndicator = repeatIndicator;
		this.sourceMmsi = sourceMmsi;
	}

	public final AISMessageType getMessageType() {
		return messageType;
	}

	public final Integer getRepeatIndicator() {
		return repeatIndicator;
	}

	public final MMSI getSourceMmsi() {
		return sourceMmsi;
	}
	
	//this method should be implemented by all subclasses
	@Override
	public abstract void fillMap(Map<String, Object> res);

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DecodedAISMessage [messageType=").append(messageType)
				.append(", repeatIndicator=").append(repeatIndicator)
				.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((messageType == null) ? 0 : messageType.hashCode());
		result = prime * result
				+ ((repeatIndicator == null) ? 0 : repeatIndicator.hashCode());
		result = prime * result
				+ ((sourceMmsi == null) ? 0 : sourceMmsi.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof DecodedAISPayload))
			return false;
		DecodedAISPayload other = (DecodedAISPayload) obj;
		if (messageType != other.messageType)
			return false;
		if (repeatIndicator == null) {
			if (other.repeatIndicator != null)
				return false;
		} else if (!repeatIndicator.equals(other.repeatIndicator))
			return false;
		if (sourceMmsi == null) {
			if (other.sourceMmsi != null)
				return false;
		} else if (!sourceMmsi.equals(other.sourceMmsi))
			return false;
		return true;
	}
}
