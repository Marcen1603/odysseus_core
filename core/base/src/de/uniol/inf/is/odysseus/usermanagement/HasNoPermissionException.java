package de.uniol.inf.is.odysseus.usermanagement;

public class HasNoPermissionException extends Exception {

	private static final long serialVersionUID = -701006083841346101L;

	public HasNoPermissionException(String message) {
		super(message);
	}
}
