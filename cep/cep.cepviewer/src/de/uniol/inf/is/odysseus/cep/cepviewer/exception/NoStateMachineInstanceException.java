package de.uniol.inf.is.odysseus.cep.cepviewer.exception;

@SuppressWarnings("serial")
public class NoStateMachineInstanceException extends RuntimeException {

	public NoStateMachineInstanceException() {
		super("There was no StateMachineInstance to add!");
	}
}
