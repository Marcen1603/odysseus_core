package de.uniol.inf.is.odysseus.badast;

import de.uniol.inf.is.odysseus.badast.kafka.ByteArrayKafkaPublisher;
import de.uniol.inf.is.odysseus.badast.kafka.StringKafkaPublisher;

/**
 * Factory to create {@code IPublishers}.
 * 
 * @author Michael Brand
 *
 */
public class PublisherFactory {

	/**
	 * Creates an {@code IPublisher} to publish String messages with String
	 * topics to the used publish subscribe system.
	 * 
	 * @param id
	 *            The id for the publisher.
	 * @return A new created {@code IPublisher}.
	 */
	public static IPublisher<String> createStringStringPublisher(String id) {
		return new StringKafkaPublisher(id);
	}

	/**
	 * Creates an {@code IPublisher} to publish ByteArray messages with String
	 * topics to the used publish subscribe system.
	 * 
	 * @param id
	 *            The id for the publisher.
	 * @return A new created {@code IPublisher}.
	 */
	public static IPublisher<byte[]> createStringByteArrayPublisher(String id) {
		return new ByteArrayKafkaPublisher(id);
	}

}