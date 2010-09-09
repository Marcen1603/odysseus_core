package de.uniol.inf.is.odysseus.cep.epa.exceptions;

/**
 * Diese Exception zeigt an, dass ein Event nicht verarbeitet werden kann, da es
 * entweder von einem ungültigen Datentyp oder null ist.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class InvalidEventException extends RuntimeException {

	private static final long serialVersionUID = 1721896077184608208L;

	public InvalidEventException() {
	}

	public InvalidEventException(String message) {
		super(message);
	}

	public InvalidEventException(Throwable cause) {
		super(cause);
	}

	public InvalidEventException(String message, Throwable cause) {
		super(message, cause);
	}

}
