package de.uniol.inf.is.odysseus.cep.epa.exceptions;

/**
 * Diese Exception besagt, dass eine nicht definierte Aktion vom EPA ausgeführt
 * werden sollte.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class UndefinedActionException extends RuntimeException {

	private static final long serialVersionUID = -1201159013987998180L;

	public UndefinedActionException() {
	}

	public UndefinedActionException(String message) {
		super(message);
	}

	public UndefinedActionException(Throwable cause) {
		super(cause);
	}

	public UndefinedActionException(String message, Throwable cause) {
		super(message, cause);
	}

}
