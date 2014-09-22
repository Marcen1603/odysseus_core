package de.uniol.inf.is.odysseus.peer.recovery.tuplestore;

import de.uniol.inf.is.odysseus.core.collection.Tuple;

@SuppressWarnings("rawtypes")
public interface IRecoveryTupleStore {

	/**
	 * Tuple will be saved at the end of the queue
	 * @param tuple Tuple to save in the queue.
	 */
	public void saveTuple(Tuple tuple);
	
	/**
	 * Returns the next (oldest) tuple and deletes it from the queue.
	 * @return The next tuple.
	 */
	public Tuple getNextTuple();
	
}
