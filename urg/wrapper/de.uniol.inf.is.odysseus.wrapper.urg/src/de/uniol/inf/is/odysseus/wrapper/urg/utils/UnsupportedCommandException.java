package de.uniol.inf.is.odysseus.wrapper.urg.utils;

public class UnsupportedCommandException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8286524829743785595L;
	
	private String message;
	public UnsupportedCommandException(String message) {
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
}
