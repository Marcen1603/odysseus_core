package de.uniol.inf.is.odysseus.wsenrich.exceptions;

public class DatafieldNotFoundException extends Exception {
	
	/**
	 * Serialisierung:
	 */
	private static final long serialVersionUID = 9214109279723471319L;

	public DatafieldNotFoundException() {
		
		super("The give Datafield was not found in the Document");
		
	}

}
