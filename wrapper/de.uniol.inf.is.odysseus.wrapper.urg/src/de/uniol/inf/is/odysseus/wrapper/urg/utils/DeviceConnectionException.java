package de.uniol.inf.is.odysseus.wrapper.urg.utils;

public class DeviceConnectionException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1644797716690874451L;
	
	private String message;
	public DeviceConnectionException(String message) {
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
}
