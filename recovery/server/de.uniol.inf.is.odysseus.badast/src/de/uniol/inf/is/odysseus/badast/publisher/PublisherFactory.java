package de.uniol.inf.is.odysseus.badast.publisher;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory to create {@code IPublishers}.
 * 
 * @author Michael Brand
 *
 */
public class PublisherFactory {

	/**
	 * All bound publishers mapped to their message type.
	 */
	private static final Map<Class<?>, IPublisher<?>> publishers = new HashMap<>();

	/**
	 * Binds a new publisher. <br />
	 * Already bound publisher with the same message type will be overridden.
	 */
	public static void bindPublisher(IPublisher<?> publisher) {
		publishers.put(publisher.getMessageType(), publisher);
	}

	/**
	 * Unbinds a publisher.
	 */
	public static void unbindPublisher(IPublisher<?> publisher) {
		if (publishers.containsValue(publisher)) {
			publishers.remove(publisher.getMessageType());
		}
	}

	/**
	 * Creates an {@code IPublisher} to publish messages with String topics to
	 * the used publish subscribe system.
	 * 
	 * @param messageType
	 *            The message type for the publisher.
	 * @param id
	 *            The id for the publisher.
	 * @return A new created {@code IPublisher}.
	 */
	@SuppressWarnings("unchecked")
	public static <Message> IPublisher<Message> createPublisher(Class<?> messageType, String id) {
		return (IPublisher<Message>) publishers.get(messageType).newInstance(id);
	}

}