package de.uniol.inf.is.odysseus.badast;

/**
 * BaDaSt exceptions indicate that something went wrong within the BaDaSt
 * application.
 * 
 * @author Michael Brand
 *
 */
public class BaDaStException extends Exception {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = 3140565339912497967L;

	/**
	 * Creates a new BaDaSt exception with a given error message.
	 * 
	 * @param message
	 *            An error message for the user.
	 */
	public BaDaStException(String message) {
		super(message);
	}

	/**
	 * Creates a new BaDaSt exception with a given error message and a given
	 * cause.
	 * 
	 * @param message
	 *            An error message for the user.
	 * @param cause
	 *            Another {@link Throwable}, which caused the BaDaSt exception.
	 */
	public BaDaStException(String message, Throwable cause) {
		super(message, cause);
	}

}