package de.uniol.inf.is.odysseus.usermanagement;

public class HasNoPermissionException extends Exception {

	private static final long serialVersionUID = -701006083841346101L;

	public HasNoPermissionException(Enum privenum, Object obj, String message) {
		super(message + " can't " + privenum + " on " + obj.toString());
	}

	public HasNoPermissionException(String message) {
		super(message + "is no admin");
	}
}
