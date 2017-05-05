package de.uniol.inf.is.odysseus.admission.status.loadshedding.exceptions;

/**
 * This Exception is thrown, if the user tries to change the status component to a component, which doesn't exist.
 */
public class NoSuchStatusComponentException extends RuntimeException {
	
	private static final long serialVersionUID = 481385463847361515L;

	public NoSuchStatusComponentException() {
		super("There exists no load shedding component with the given name.");
	}


}
