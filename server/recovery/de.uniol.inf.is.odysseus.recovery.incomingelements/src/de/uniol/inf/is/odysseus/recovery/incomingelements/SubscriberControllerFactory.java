package de.uniol.inf.is.odysseus.recovery.incomingelements;

import de.uniol.inf.is.odysseus.recovery.incomingelements.kafka.KafkaSubscriberController;

/**
 * Factory to create {@code ISubscriberController}.
 * 
 * @author Michael Brand
 *
 */
public class SubscriberControllerFactory {

	/**
	 * Creates a new controller.
	 * 
	 * @param topic
	 *            Topic to read from (source name).
	 * @param partition
	 *            Partition to read from (default 0).
	 * @param consumer
	 *            The consumer to be updated while reading.
	 * @param offset
	 *            The offset from where to read.
	 */
	public static ISubscriberController createController(String topic, int partition, ISubscriber consumer,
			long offset) {
		return new KafkaSubscriberController(topic, partition, consumer, offset);
	}

	/**
	 * Creates a new controller with default partition.
	 * 
	 * @param topic
	 *            Topic to read from (source name).
	 * @param consumer
	 *            The consumer to be updated while reading.
	 * @param offset
	 *            The offset from where to read.
	 */
	public static ISubscriberController createController(String topic, ISubscriber consumer, long offset) {
		return new KafkaSubscriberController(topic, consumer, offset);
	}

	/**
	 * Creates a new controller access with earliest possible offset to use.
	 * 
	 * @param topic
	 *            Topic to read from (source name).
	 * @param partition
	 *            Partition to read from (default 0).
	 * @param consumer
	 *            The consumer to be updated while reading.
	 */
	public static ISubscriberController createController(String topic, int partition, ISubscriber consumer) {
		return new KafkaSubscriberController(topic, partition, consumer);
	}

	/**
	 * Creates a new controller with default partition and earliest possible
	 * offset to use.
	 * 
	 * @param topic
	 *            Topic to read from (source name).
	 * @param consumer
	 *            The consumer to be updated while reading.
	 */
	public static ISubscriberController createController(String topic, ISubscriber consumer) {
		return new KafkaSubscriberController(topic, consumer);
	}

}