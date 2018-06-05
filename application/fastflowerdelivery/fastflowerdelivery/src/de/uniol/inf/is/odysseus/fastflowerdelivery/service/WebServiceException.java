package de.uniol.inf.is.odysseus.fastflowerdelivery.service;

/**
 * An exception designed to deliver a message to the client.
 *
 * @author Weert Stamm
 * @version 1.0
 */
public class WebServiceException extends RuntimeException {
	public WebServiceException(String errorMessage) {
		super(errorMessage);
	}

	private static final long serialVersionUID = 254464833941944773L;

}
