package de.uniol.inf.is.odysseus.cep.epa.exceptions;

/**
 * Diese Exception wird geworfen, wenn der Wert einer Variablen nicht ermittelt
 * werden konnte. Ursache dafür kann auch das Fehlen der Variablen in der
 * Symboltabelle sein.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class UndeterminableVariableValueException extends RuntimeException {

	private static final long serialVersionUID = 6009994326063071102L;

	public UndeterminableVariableValueException(String msg) {
		super(msg);
	}
}
