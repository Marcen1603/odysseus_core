package de.uniol.inf.is.odysseus.cep.epa.exceptions;

/**
 * Diese Exception drückt einen Fehler bei der Evaluierung einer
 * Transitionsbedingung an.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class ConditionEvaluationException extends RuntimeException {

	private static final long serialVersionUID = -4803057129604533863L;

	public ConditionEvaluationException() {
	}

	public ConditionEvaluationException(String arg0) {
		super(arg0);
	}

	public ConditionEvaluationException(Throwable arg0) {
		super(arg0);
	}

	public ConditionEvaluationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
