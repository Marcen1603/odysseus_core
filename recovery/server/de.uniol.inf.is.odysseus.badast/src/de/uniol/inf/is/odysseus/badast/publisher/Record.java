package de.uniol.inf.is.odysseus.badast.publisher;

import java.io.Serializable;

/**
 * A {@code Record} is used by {@code IPublishers} for the used publish
 * subscribe system to publish a given message with a given topic.
 * 
 * @author Michael Brand
 *
 * @param <Message>
 *            The type of the message.
 */
public class Record<Message> implements Serializable {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = 3473628855701983271L;

	/**
	 * The topic for the used publish subscribe system.
	 */
	private final String topic;

	/**
	 * The message for the used publish subscribe system.
	 */
	private final Message message;

	/**
	 * Creates a new record.
	 * 
	 * @param topic
	 *            The topic for the used publish subscribe system.
	 * @param message
	 *            The message for the used publish subscribe system.
	 */
	public Record(String topic, Message message) {
		this.topic = topic;
		this.message = message;
	}

	/**
	 * Gets the topic.
	 */
	public String getTopic() {
		return this.topic;
	}

	/**
	 * Gets the message.
	 */
	public Message getMessage() {
		return this.message;
	}

}