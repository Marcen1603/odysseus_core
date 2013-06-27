package de.uniol.inf.is.odysseus.wsenrich.exceptions;

public class OperationNotFoundException extends Exception {
	
	/**
	 * Serialisation:
	 */
	private static final long serialVersionUID = 9214109279723471319L;

	public OperationNotFoundException() {
		
		super("The operation you defined was not found in the Wsdl-File");
		
	}

}

