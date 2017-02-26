package de.uniol.inf.is.odysseus.admission.status.loadshedding.exceptions;

/**
 * This Exception is thrown, if the user tries to change the settings while a load shedding component is active.
 */
public class ActiveLoadSheddingException extends RuntimeException {

	private static final long serialVersionUID = -389098802980852689L;
	
	public ActiveLoadSheddingException() {
		super("The Load Shedding settings cannot be changed, while a Load Shedding Component is active.");
	}

}
