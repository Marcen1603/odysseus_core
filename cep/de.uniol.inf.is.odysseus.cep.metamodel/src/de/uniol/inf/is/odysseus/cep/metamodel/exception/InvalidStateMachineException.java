package de.uniol.inf.is.odysseus.cep.metamodel.exception;

import de.uniol.inf.is.odysseus.cep.metamodel.validator.ValidationResult;

/**
 * Diese Exception zeigt an, dass ein geprüfter Automat fehlerhaft ist. Das
 * Exception-Objekt kann das Ergebnis der Überprüfung vom Typ
 * {@link ValidationResult} enthalten.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class InvalidStateMachineException extends Exception {


	private static final long serialVersionUID = -5916531519309876391L;
	/**
	 * Speichert das Validierungsergebnis, das zu dieser Exception geführt hat
	 */
	private ValidationResult validationResult;

	/**
	 * Erzeugt eine neue Exception.
	 * 
	 * @param validationResult
	 *            Das Ergebnis der Atomaten-Valididierung, das zu dieser
	 *            Exception geführt hat.
	 */
	public InvalidStateMachineException(ValidationResult validationResult) {
		this.validationResult = validationResult;
	}

	/**
	 * Liefert das Ergebnis der Atomaten-Valididierung, das zu dieser Exception
	 * geführt hat.
	 * 
	 * @return Das Validierungsergebnis oder null, falls kein Ergebnis an die
	 *         Exception angehängt wurde.
	 */
	public ValidationResult getValidationResult() {
		return this.validationResult;
	}

}
