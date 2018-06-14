package de.uniol.inf.is.odysseus.recovery.badast.util;

import de.uniol.inf.is.odysseus.badast.subscriber.ISubscriber;
import de.uniol.inf.is.odysseus.badast.subscriber.ISubscriberController;

/**
 * Provider for a bound {@link ISubscriberController}.
 * 
 * @author Michael Brand
 *
 */
public class SubscriberControllerProvider {

	/**
	 * The bound {@link ISubscriberController}.
	 */
	private static ISubscriberController subController;

	/**
	 * Binds an instance of {@link ISubscriberController}.
	 */
	public static void bindController(ISubscriberController controller) {
		subController = controller;
	}

	/**
	 * Binds an instance of {@link ISubscriberController}.
	 */
	public static void unbindController(ISubscriberController controller) {
		if (controller == subController) {
			subController = null;
		}
	}

	/**
	 * Creates a new subscriber controller.
	 * 
	 * @param stream
	 *            Stream to read from (source name).
	 * @param partition
	 *            Partition to read from (default 0).
	 * @param subscriber
	 *            The subscriber to be updated while reading.
	 * @param offset
	 *            The offset from where to read.
	 */
	public static ISubscriberController newInstance(String stream, int partition, ISubscriber subscriber, long offset) {
		return subController.newInstance(stream, partition, subscriber, offset);
	}

}