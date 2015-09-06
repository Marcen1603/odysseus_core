package de.uniol.inf.is.odysseus.badast;

import java.io.Serializable;

/**
 * An {@code IRecord} is used by {@code IPublishers} for the used publish
 * subscribe system to publish a given message with a given topic.
 * 
 * @author Michael Brand
 *
 * @param <Topic>
 *            The type of the topic.
 * @param <Message>
 *            The type of the message.
 */
public class Record<Topic, Message> implements Serializable {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = 3473628855701983271L;

	/**
	 * The topic for the used publish subscribe system.
	 */
	private final Topic mTopic;

	/**
	 * The message for the used publish subscribe system.
	 */
	private final Message mMessage;

	/**
	 * Creates a new record.
	 * 
	 * @param topic
	 *            The topic for the used publish subscribe system.
	 * @param message
	 *            The message for the used publish subscribe system.
	 */
	public Record(Topic topic, Message message) {
		this.mTopic = topic;
		this.mMessage = message;
	}

	/**
	 * Gets the topic.
	 * 
	 * @return The topic for the used publish subscribe system.
	 */
	public Topic getTopic() {
		return this.mTopic;
	}

	/**
	 * Gets the message.
	 * 
	 * @return The message for the used publish subscribe system.
	 */
	public Message getMessage() {
		return this.mMessage;
	}

}