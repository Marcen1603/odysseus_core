package de.uniol.inf.is.odysseus.peer.distribute.partition.survey.model;

public class CouldNotPartitionException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6829335904951079049L;

	public CouldNotPartitionException(String reason) {
		super(reason);
	}
}
