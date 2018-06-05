package de.uniol.inf.is.odysseus.rcp.evaluation;

public class EvaluationException extends RuntimeException {

	private static final long serialVersionUID = -6588201850723948490L;
	
	public EvaluationException(String string) {
		super(string);
	}
	
	public EvaluationException(String string, Throwable t) {
		super(string, t);
	}


}
