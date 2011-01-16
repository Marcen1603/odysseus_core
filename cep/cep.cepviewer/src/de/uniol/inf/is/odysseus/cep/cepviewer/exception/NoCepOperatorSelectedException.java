package de.uniol.inf.is.odysseus.cep.cepviewer.exception;

@SuppressWarnings("serial")
public class NoCepOperatorSelectedException extends RuntimeException {

	public NoCepOperatorSelectedException() {
		super("No CepOperator was selected!");
	}
}
