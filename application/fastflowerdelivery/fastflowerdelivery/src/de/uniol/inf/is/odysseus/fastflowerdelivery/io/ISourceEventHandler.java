package de.uniol.inf.is.odysseus.fastflowerdelivery.io;

import de.uniol.inf.is.odysseus.generator.DataTuple;

/**
 * Used to distinguish instances of event senders
 * and also serves the event to tuple conversion
 *
 * @author Weert Stamm
 * @version 1.0
 */
public interface ISourceEventHandler {

	/**
	 * Retrieve the tuple corresponding to this type
	 * @return the DataTuple
	 */
	public DataTuple getTuple();
	
}
