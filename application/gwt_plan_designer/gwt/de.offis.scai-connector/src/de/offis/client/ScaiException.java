package de.offis.client;

import java.io.Serializable;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Wrapper around one or more Scai-XML-Exceptions.
 *
 * @author Alexander Funk
 * 
 */
public class ScaiException extends Exception implements Serializable {
	private static final long serialVersionUID = 8728689360050985319L;
	
	private Map<String, String> texts;
	private Map<String, Integer> errorCodes;

	public ScaiException(String message) {
		super(message);
		Logger.getLogger("ScaiConnector").log(Level.INFO, message);
	}
	
	public ScaiException(Throwable throwable) {
		super(throwable);
		Logger.getLogger("ScaiConnector").log(Level.INFO, throwable.toString());
	}
	
	public ScaiException() {
		// serilizable
	}
	
	public ScaiException(String operationId, String text, Integer errorCode) {
		super("Found exception in Scai: " + text + "(" + errorCode + ")");
		Logger.getLogger("ScaiConnector").log(Level.INFO, "Found exception in Scai: " + text + "(" + errorCode + ")" );
	}
	
	public ScaiException(Map<String, String> texts, Map<String, Integer> errorCodes) {
		super("One or more ScaiException were returned.");
		this.texts = texts;
		this.errorCodes = errorCodes;
		Logger.getLogger("ScaiConnector").log(Level.INFO, texts.toString());
	}

	public String getMessage(String operationId) {		
		return texts.get(operationId);
	}
	
	public Integer getErrorCode(String operationId) {		
		return errorCodes.get(operationId);
	}
}
