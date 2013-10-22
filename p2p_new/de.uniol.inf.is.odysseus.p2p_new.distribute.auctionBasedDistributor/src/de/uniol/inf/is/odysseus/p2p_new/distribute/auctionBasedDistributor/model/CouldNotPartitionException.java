package de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.model;

public class CouldNotPartitionException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6829335904951079049L;

	public CouldNotPartitionException(String reason) {
		super(reason);
	}
}
