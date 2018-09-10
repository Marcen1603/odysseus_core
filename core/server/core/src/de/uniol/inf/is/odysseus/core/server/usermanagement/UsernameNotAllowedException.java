package de.uniol.inf.is.odysseus.core.server.usermanagement;

public class UsernameNotAllowedException extends RuntimeException {

	private static final long serialVersionUID = -495551366872643974L;

	public UsernameNotAllowedException(String arg0) {
		super(arg0);
	}

	public UsernameNotAllowedException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public UsernameNotAllowedException(String arg0, Throwable arg1,
			boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
