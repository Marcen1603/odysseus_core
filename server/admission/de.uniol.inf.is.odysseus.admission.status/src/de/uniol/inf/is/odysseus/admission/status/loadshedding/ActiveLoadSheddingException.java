package de.uniol.inf.is.odysseus.admission.status.loadshedding;

/**
 * 
 * This Exception is thrown, if the User tries to change the settings while a Load Shedding Component is active.
 *
 */
public class ActiveLoadSheddingException extends RuntimeException {

	private static final long serialVersionUID = -389098802980852689L;
	
	protected ActiveLoadSheddingException() {
		super("The Load Shedding settings cannot be changed, while a Load Shedding Component is active.");
	}

}
