package de.uniol.inf.is.odysseus.fastflowerdelivery.io;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;

/**
 * Interface to provide basic functionality for processing received tuples.
 * Also delivers the Schema as list of types as strings.
 *
 * @author Weert Stamm
 * @version 1.0
 */
public interface ISinkEventHandler {

	/**
	 * Delivers a list of types in the same order and amount as used in the schema in odysseus' sink.
	 * Types are given as String (using the same names odysseus uses)
	 * @return the list of types
	 */
	public List<String> getSchema();
	
	/**
	 * Processes a tuple received by an event receiver
	 * @param tuple
	 * 			the tuple received by an event receiver
	 */
	@SuppressWarnings("rawtypes")
	public void processTuple(Tuple tuple);
	
}
