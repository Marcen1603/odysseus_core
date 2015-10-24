package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.transmissionhandler.odyload;



/**
 * Listener Interface for a single Query Transmission between Pers.
 * @author Carsten Cordes
 *
 */
public interface IQueryTransmissionListener {
	/**
	 * Called if locking of Local Peer failed
	 * @param transmission
	 */
	public void localLockFailed(QueryTransmission transmission);
	/**
	 * Called if Transmission failed.
	 * @param transmission
	 */
	public void tranmissionFailed(QueryTransmission transmission);
	/**
	 * Called if Transmission succeeded.
	 * @param transmission
	 */
	public void transmissionSuccessful(QueryTransmission transmission);
}
