package de.uniol.inf.is.odysseus.client.communication.rest;

/**
 * @author Tobias Brandt
 * @since 25.04.2015.
 */
public class RestException extends Exception {

	private static final long serialVersionUID = -2602141511765340112L;

	public RestException(String message) {
        super(message);
    }

}
