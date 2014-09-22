package de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.exceptions;


public class UnsupportedPayloadType extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4597802923042519912L;

	public UnsupportedPayloadType(Integer messageType) {
		super("The message type: " + messageType.toString() + " is unsupported!");
	}

}
