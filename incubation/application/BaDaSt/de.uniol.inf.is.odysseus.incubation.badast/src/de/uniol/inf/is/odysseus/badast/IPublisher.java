package de.uniol.inf.is.odysseus.badast;

import org.apache.zookeeper.KeeperException.BadArgumentsException;

/**
 * An {@code IPublisher} uses {@code IRecords} to publish a given message with a
 * given topic to the used publish subscribe system.
 * 
 * @author Michael Brand
 *
 * @param <Topic>
 *            The type of the topic.
 * @param <Message>
 *            The type of the message.
 */
public interface IPublisher<Topic, Message> extends AutoCloseable {

	/**
	 * Publishes a given record.
	 * 
	 * @param record
	 *            The record to publish.
	 * @throws BaDaStException
	 *             if any error occurs.
	 */
	public void publish(Record<Topic, Message> record) throws BadArgumentsException;

}