package de.uniol.inf.is.odysseus.badast;

import de.uniol.inf.is.odysseus.core.server.recovery.badast.BaDaStException;

/**
 * An {@code IPublisher} uses {@code IRecords} to publish a given message with a
 * given topic to the used publish subscribe system.
 * 
 * @author Michael Brand
 * 
 * @param <Message>
 *            The type of the message.
 */
public interface IPublisher<Message> extends AutoCloseable {

	/**
	 * Publishes a given record.
	 * 
	 * @param record
	 *            The record to publish.
	 * @throws BaDaStException
	 *             if any error occurs.
	 */
	public void publish(Record<Message> record) throws BaDaStException;

}