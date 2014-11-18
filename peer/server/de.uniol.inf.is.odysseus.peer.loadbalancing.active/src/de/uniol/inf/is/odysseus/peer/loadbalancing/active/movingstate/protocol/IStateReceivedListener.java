package de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.protocol;

/***
 * Listener for state Copies
 * 
 * @author Carsten Cordes
 *
 */
public interface IStateReceivedListener {

	/***
	 * Gets called when state was successfully received.
	 * 
	 * @param pipe
	 *            PipeId
	 */
	public void stateReceived(String pipe);
}
