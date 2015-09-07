package de.uniol.inf.is.odysseus.recovery.incomingelements;

import de.uniol.inf.is.odysseus.core.collection.Resource;
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
	 * @param source
	 *            Source to read from.
	 * @param partition
	 *            Partition to read from (default 0).
	 * @param consumer
	 *            The consumer to be updated while reading.
	 * @param offset
	 *            The offset from where to read.
	 */
	public static ISubscriberController createController(Resource source, int partition, ISubscriber consumer,
			long offset) {
		return new KafkaSubscriberController(source.toString(), partition, consumer, offset);
	}

	/**
	 * Creates a new controller with default partition.
	 * 
	 * @param source
	 *            Source to read from.
	 * @param consumer
	 *            The consumer to be updated while reading.
	 * @param offset
	 *            The offset from where to read.
	 */
	public static ISubscriberController createController(Resource source, ISubscriber consumer, long offset) {
		return new KafkaSubscriberController(source.toString(), consumer, offset);
	}

	/**
	 * Creates a new controller access with earliest possible offset to use.
	 * 
	 * @param source
	 *            Source to read from.
	 * @param partition
	 *            Partition to read from (default 0).
	 * @param consumer
	 *            The consumer to be updated while reading.
	 */
	public static ISubscriberController createController(Resource source, int partition, ISubscriber consumer) {
		return new KafkaSubscriberController(source.toString(), partition, consumer);
	}

	/**
	 * Creates a new controller with default partition and earliest possible
	 * offset to use.
	 * 
	 * @param source
	 *            Source to read from.
	 * @param consumer
	 *            The consumer to be updated while reading.
	 */
	public static ISubscriberController createController(Resource source, ISubscriber consumer) {
		return new KafkaSubscriberController(source.toString(), consumer);
	}

}