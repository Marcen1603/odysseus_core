package de.uniol.inf.is.odysseus.cep.metamodel.validator;

import java.util.LinkedList;

public class ValidationResult {

	/**
	 * Gibt an, ob der Automat gültig ist.
	 */
	private boolean valid;
	/**
	 * Liste der gefundenen Fehler.
	 */
	private LinkedList<ValidationError> errorList;
	/**
	 * Liste der gefundenen Warnungen.
	 */
	private LinkedList<ValidationWarning> warningList;

	public ValidationResult() {
		this.valid = true;
		this.errorList = new LinkedList<ValidationError>();
		this.warningList = new LinkedList<ValidationWarning>();
	}

	public boolean isValid() {
		return valid;
	}

	/**
	 * Fügt einen Fehler zum Validierungs-Ergebnis hinzu und setzt die
	 * Gültigkeit auf false.
	 * 
	 * @param error
	 *            Fehler, der dem Ergebnis hinzugefügt werden soll.
	 */
	public void addValidationError(ValidationError error) {
		if (error != null) {
			this.errorList.add(error);
			this.valid = false;
		}
	}

	/**
	 * Fügt einen Warnung zum Validierungs-Ergebnis hinzu.
	 * 
	 * @param error
	 *            Fehler, der dem Ergebnis hinzugefügt werden soll.
	 */
	public void addValidationWarning(ValidationWarning warning) {
		if (warning != null)
			this.warningList.add(warning);
	}

	/**
	 * Fügt dem Ergebnis eine beliebige Ausnahme hinzu.
	 * 
	 * @param exception
	 */
	public void addValidationException(ValidationException exception) {
		if (exception != null) {
			if (exception instanceof ValidationError) {
				this.errorList.add((ValidationError) exception);
				this.valid = false;
			} else if (exception instanceof ValidationWarning) {
				this.warningList.add((ValidationWarning) exception);
			}
		}
	}

	/**
	 * Gibt die Liste der Fehler zurück, die bei der Überprüfung festgestellt
	 * wurden.
	 * 
	 * @return Liste der festgestellten Fehler. Sollte nicht direkt verändert
	 *         werden. Zum hinzufügen von Fehlern muss die Methode
	 *         {@link addValidationError} genutzt werden.
	 */
	public LinkedList<ValidationError> getErrorList() {
		return errorList;
	}

	/**
	 * Gibt die Liste der Warnungen zurück, die bei der Überprüfung festgestellt
	 * wurden.
	 * 
	 * @return Liste der festgestellten Warnungen. Sollte nicht direkt verändert
	 *         werden. Zum hinzufügen von Warnungen muss die Methode
	 *         {@link addValidationWarning} genutz werden.
	 */
	public LinkedList<ValidationWarning> getWarningList() {
		return this.warningList;
	}

}
