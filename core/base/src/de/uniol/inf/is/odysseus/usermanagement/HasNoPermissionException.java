package de.uniol.inf.is.odysseus.usermanagement;

public class HasNoPermissionException extends RuntimeException{

	private static final long serialVersionUID = -701006083841346101L;

	public HasNoPermissionException(String message) {
		super(message);
	}
}
