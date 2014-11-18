package de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.buffer;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
@Deprecated
/***
 * Interface for Tuple Buffer
 * Deprecated because Subscriptions can be suspended.
 * @author Carsten Cordes
 *
 */
public interface ILoadBalancingBuffer {

	/**
	 * Tuple will be saved at the end of the queue
	 * @param tuple Tuple to save in the queue.
	 */
	public void saveTuple(IStreamObject<? extends ITimeInterval> tuple);
	
	/**
	 * Returns the next (oldest) tuple and deletes it from the queue.
	 * @return The next tuple.
	 */
	public IStreamObject<? extends ITimeInterval> getNextTuple();
	
}
