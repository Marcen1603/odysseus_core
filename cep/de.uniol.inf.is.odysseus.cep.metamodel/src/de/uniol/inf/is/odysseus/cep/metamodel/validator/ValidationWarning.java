package de.uniol.inf.is.odysseus.cep.metamodel.validator;

public abstract class ValidationWarning extends ValidationException {
	
	public ValidationWarning() {
		super();
	}
	
	/**
	 * Gibt eine textuelle Beschreibung der Warnung zurück.
	 * @return Warnungsmeldung.
	 */
	public abstract String getWarningMessage();

}
