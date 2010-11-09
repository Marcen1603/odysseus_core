package de.uniol.inf.is.odysseus.usermanagement;

public class NullUserException extends RuntimeException {

	private static final long serialVersionUID = 5726505543238627415L;

	public NullUserException(String message) {
		super(message);
	}
}
