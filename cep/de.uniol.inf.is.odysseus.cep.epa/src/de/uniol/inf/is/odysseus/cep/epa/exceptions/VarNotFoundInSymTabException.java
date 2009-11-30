package de.uniol.inf.is.odysseus.cep.epa.exceptions;

/**
 * Diese Exception wird geworfen, wenn der eine Variable nicht in der
 * Symboltabelle gefunden werden konnte. Diese Exception wird zur Zeit nicht
 * mehr verwendet. Stattdessen kommt
 * {@link UndeterminableVariableValueException} zum Einsatz.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class VarNotFoundInSymTabException extends RuntimeException {

	private static final long serialVersionUID = -8310992005925700683L;

	public VarNotFoundInSymTabException() {
	}

	public VarNotFoundInSymTabException(String message) {
		super(message);
	}

	public VarNotFoundInSymTabException(Throwable cause) {
		super(cause);
	}

	public VarNotFoundInSymTabException(String message, Throwable cause) {
		super(message, cause);
	}

}
