package de.uniol.inf.is.odysseus.datamining.clustering.builder;

public class InvalidThresholdValueException extends Exception {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8007243308092775640L;

	@Override
	public String getMessage() {
		return "The threshold value has to be greater than zero";
	}
}
