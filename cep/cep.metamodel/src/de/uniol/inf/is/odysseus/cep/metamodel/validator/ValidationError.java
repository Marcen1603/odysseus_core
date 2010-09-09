package de.uniol.inf.is.odysseus.cep.metamodel.validator;

public abstract class ValidationError extends ValidationException {
	
	public ValidationError() {
		super();
	}

	/**
	 * Gibt eine textuelle Beschreibung des Fehlers zurück.
	 * @return Fehlermeldung.
	 */
	public abstract String getErrorMessage();
	
}
