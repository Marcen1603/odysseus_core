package de.uniol.inf.is.odysseus.badast.publisher;

import de.uniol.inf.is.odysseus.badast.BaDaStException;

/**
 * An {@code IPublisher} uses {@code Records} to publish a given message with a
 * given topic to the used publish subscribe system.
 * 
 * @author Michael Brand
 * 
 * @param <Message>
 *            The type of the message.
 */
public interface IPublisher<Message> extends AutoCloseable {

	/**
	 * Creates an {@code IPublisher} to publish messages with String topics to
	 * the used publish subscribe system.
	 * 
	 * @param id
	 *            The id for the publisher.
	 * @return A new created {@code IPublisher}.
	 */
	public IPublisher<Message> newInstance(String id);

	/**
	 * Publishes a given record.
	 * 
	 * @param record
	 *            The record to publish.
	 * @throws BaDaStException
	 *             if any error occurs.
	 */
	public void publish(Record<Message> record) throws BaDaStException;

	/**
	 * Gets the emssage type.
	 */
	public Class<?> getMessageType();

}