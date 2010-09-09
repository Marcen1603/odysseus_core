package de.uniol.inf.is.odysseus.cep.epa.exceptions;

/**
 * Diese Exception wird geworfen, wenn ein undefinierter Consumption Mode
 * verarbeitet werden soll.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class UndefinedConsumptionModeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1411725414862146250L;

	public UndefinedConsumptionModeException() {
	}

	public UndefinedConsumptionModeException(String message) {
		super(message);
	}

	public UndefinedConsumptionModeException(Throwable cause) {
		super(cause);
	}

	public UndefinedConsumptionModeException(String message, Throwable cause) {
		super(message, cause);
	}

}
