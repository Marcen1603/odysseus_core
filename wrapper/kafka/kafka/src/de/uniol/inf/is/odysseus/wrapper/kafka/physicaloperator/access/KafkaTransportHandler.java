package de.uniol.inf.is.odysseus.wrapper.kafka.physicaloperator.access;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.WakeupException;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

/**
 * Transport handler to push data to Kafka as well as to consume data from
 * Kafka.
 *
 * @author Michael Brand
 * @version 0.10.1.0
 * @see <a href="https://kafka.apache.org">kafka.apache.org</a>
 */
public class KafkaTransportHandler extends AbstractTransportHandler implements BundleActivator {

	/**
	 * The logger for this class
	 */
	private static final Logger log = LoggerFactory.getLogger("KafkaTransportHandler");

	/**
	 * The name of the transport handler for usage in a query language.
	 */
	private static final String name = "Kafka";

	/**
	 * The options key of the Kafka topic.
	 */
	private static final String topicKey = "topic";

	/**
	 * The options key of the message type (String or byte[]).
	 */
	private static final String messageTypeKey = "messagetype";

	/**
	 * The Kafka config key for the key serializer.
	 */
	private static final String keySerializerKey = "key.serializer";

	/**
	 * The Kafka config key for the value serializer.
	 */
	private static final String valueSerializerKey = "value.serializer";

	/**
	 * The Kafka config key for the key deserializer.
	 */
	private static final String keyDeserializerKey = "key.deserializer";

	/**
	 * The Kafka config key for the value deserializer.
	 */
	private static final String valueDeserializerKey = "value.deserializer";

	/**
	 * The options key of the poll timeout (how long to wait between polling)
	 * [ms].
	 */
	private static final String pollTimeoutKey = "poll.timeout";

	/**
	 * The default poll timeout (how long to wait between polling) [ms].
	 */
	private static final long defaultPollTimeout = 10000l;

	/**
	 * The default properties for Kafka producers.
	 */
	private static Properties defaultProducerProperties;

	/**
	 * The default properties for Kafka consumers.
	 */
	private static Properties defaultConsumerProperties;

	/**
	 * Loads properties from a file.
	 *
	 * @param resource
	 *            URL of properties file relative to bundle root.
	 * @return The loaded properties or empty properties, if something went
	 *         wrong.
	 */
	private static Properties initProperties(URL resource) {
		Properties properties = new Properties();
		try {
			properties.load(resource.openStream());
		} catch (IOException e) {
			log.error("Could not load Kafka properties from file ' {}'!", resource);
		}
		return properties;
	}

	@Override
	public void start(BundleContext context) throws Exception {
		defaultProducerProperties = initProperties(context.getBundle().getResource("producer.properties"));
		defaultConsumerProperties = initProperties(context.getBundle().getResource("consumer.properties"));
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		// Nothing to do
	}

	/**
	 * Supported message types for kafka.
	 */
	private static enum MessageType {
		string, bytearray;
	}

	/**
	 * The Kafka producer, if in output mode.
	 */
	private Optional<KafkaProducer<String, ?>> producer = Optional.empty();

	/**
	 * The thread to pull from Kafka and to push to process handler.
	 */
	private Optional<KafkaConsumerRunner<?>> consumerRunner = Optional.empty();

	/**
	 * The Kafka topic to publish in.
	 */
	private String topic;

	/**
	 * The message type to publish with.
	 */
	private MessageType messageType;

	/**
	 * The poll timeout (how long to wait between polling) [ms].
	 */
	private long pollTimeout;

	/**
	 * Default constructor for OSGi service.
	 */
	public KafkaTransportHandler() {
		super();
	}

	/**
	 * Creates a new Kafka transport handler.
	 *
	 * @param protocolHandler
	 *            The protocol handler to use.
	 * @param options
	 *            The options set by the user. Used to exchange default settings
	 *            for Kafka.
	 */
	protected KafkaTransportHandler(final IProtocolHandler<?> protocolHandler, final OptionMap options) {
		super(protocolHandler, options);
		init(protocolHandler, options);
	}

	/**
	 * Initializes topic and message type.
	 *
	 * @param protocolHandler
	 *            The protocol handler to use.
	 * @param options
	 *            The options set by the user.
	 * @throws IllegalArgumentException
	 *             if options does not contain {@link #topicsKey} or
	 *             {@link #messageTypeKey}. If value of {@link #messageTypeKey}
	 *             is not in {@link MessageType}.
	 * @throws NullPointerException
	 *             if options is null.
	 */
	private void init(final IProtocolHandler<?> protocolHandler, final OptionMap options)
			throws IllegalArgumentException, NullPointerException {
		Objects.requireNonNull(options);
		options.checkRequiredException(topicKey, messageTypeKey);

		// load default properties
		Properties properties = new Properties();
		if (protocolHandler.getDirection() == ITransportDirection.OUT) {
			properties.putAll(defaultProducerProperties);
		} else {
			properties.putAll(defaultConsumerProperties);
		}

		// replace default properties with options set by user
		options.getKeySet().stream().forEach(key -> properties.put(key, options.get(key)));

		// set topic
		topic = options.get(topicKey);

		// may throw Nullpointer or IllegalArgument, if message
		// type is not correct
		messageType = MessageType.valueOf(options.get(messageTypeKey).toLowerCase());

		if (protocolHandler.getDirection() == ITransportDirection.OUT) {
			initProducer(properties);
		} else {
			initConsumer(properties, options.getLong(pollTimeoutKey, defaultPollTimeout));
		}

		if (log.isDebugEnabled()) {
			log.debug("++++++++++++++++++++++++++++++++++++++");
			log.debug("+ Kafka Transport Handler Properties +");
			log.debug("++++++++++++++++++++++++++++++++++++++");
			log.debug("Topic: {}", topic);
			log.debug("Message Type: {}", messageType);
			log.debug("Kafka Producer/Consumer Properties:");
			properties.keySet().stream().forEach(key -> log.debug("{}: {}", key, properties.get(key)));
		}
	}

	private void initProducer(final Properties properties) {
		String serializer;
		switch (messageType) {
		case bytearray:
			serializer = "org.apache.kafka.common.serialization.ByteArraySerializer";
			properties.put(KafkaTransportHandler.keySerializerKey, serializer);
			properties.put(KafkaTransportHandler.valueSerializerKey, serializer);
			producer = Optional.of(new KafkaProducer<String, byte[]>(properties));
			break;
		case string:
			serializer = "org.apache.kafka.common.serialization.StringSerializer";
			properties.put(KafkaTransportHandler.keySerializerKey, serializer);
			properties.put(KafkaTransportHandler.valueSerializerKey, serializer);
			producer = Optional.of(new KafkaProducer<String, String>(properties));
			break;
		default:
			throw new IllegalArgumentException("Unsupported message type!");
		}
	}

	private void initConsumer(final Properties properties, final long pollTimeout) {
		String deserializer;
		switch (messageType) {
		case bytearray:
			deserializer = "org.apache.kafka.common.serialization.ByteArrayDeserializer";
			properties.put(KafkaTransportHandler.keyDeserializerKey, deserializer);
			properties.put(KafkaTransportHandler.valueDeserializerKey, deserializer);
			KafkaConsumer<String, byte[]> byteArrayConsumer = new KafkaConsumer<String, byte[]>(properties);
			byteArrayConsumer.subscribe(Collections.singletonList(topic));
			consumerRunner = Optional.of(new KafkaConsumerRunner<byte[]>(byteArrayConsumer));
			break;
		case string:
			deserializer = "org.apache.kafka.common.serialization.StringDeserializer";
			properties.put(KafkaTransportHandler.keyDeserializerKey, deserializer);
			properties.put(KafkaTransportHandler.valueDeserializerKey, deserializer);
			KafkaConsumer<String, String> stringConsumer = new KafkaConsumer<String, String>(properties);
			stringConsumer.subscribe(Collections.singletonList(topic));
			consumerRunner = Optional.of(new KafkaConsumerRunner<String>(stringConsumer));
			break;
		default:
			throw new IllegalArgumentException("Unsupported message type!");
		}
	}

	@Override
	public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, OptionMap options) {
		return new KafkaTransportHandler(protocolHandler, options);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void processOutOpen() throws IOException {
		// Nothing to do.
	}

	@Override
	public void processOutClose() throws IOException {
		producer.ifPresent(KafkaProducer::close);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void send(byte[] message) throws IOException {
		switch (messageType) {
		case bytearray:
			producer.ifPresent(
					p -> ((KafkaProducer<String, byte[]>) p).send(new ProducerRecord<String, byte[]>(topic, message)));
			break;
		case string:
			producer.ifPresent(p -> ((KafkaProducer<String, String>) p)
					.send(new ProducerRecord<String, String>(topic, new String(message))));
			break;
		default:
			throw new IllegalArgumentException("Unsupported message type!");
		}
	}

	@Override
	public OutputStream getOutputStream() {
		throw new IllegalArgumentException("Not a pulling transport handler in output mode");
	}

	@Override
	public void processInOpen() throws IOException {
		this.consumerRunner.ifPresent(KafkaConsumerRunner::start);
	}

	@Override
	public void processInClose() throws IOException {
		this.consumerRunner.ifPresent(KafkaConsumerRunner::interrupt);
	}

	@Override
	public InputStream getInputStream() {
		throw new IllegalArgumentException("Not a pulling transport handler in input mode");
	}

	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler obj) {
		if (obj == null || !(obj instanceof KafkaTransportHandler)) {
			return false;
		}
		KafkaTransportHandler other = (KafkaTransportHandler) obj;
		if (producer.isPresent() && other.producer.isPresent()) {
			return producer.get().equals(other.producer.get());
		} else if (consumerRunner.isPresent() && other.consumerRunner.isPresent()) {
			return consumerRunner.get().consumer.equals(other.consumerRunner.get().consumer);
		}
		return false;
	}

	/**
	 * The Kafka consumer is NOT thread-safe. All network I/O happens in the
	 * thread of the application making the call. It is the responsibility of
	 * the user to ensure that multi-threaded access is properly synchronized.
	 * Un-synchronized access will result in ConcurrentModificationException.
	 * The only exception to this rule is wakeup(), which can safely be used
	 * from an external thread to interrupt an active operation. In this case, a
	 * WakeupException will be thrown from the thread blocking on the operation.
	 * This can be used to shutdown the consumer from another thread.
	 *
	 * @author Michael Brand
	 * @version 1.0
	 * @see <a href=
	 *      "https://kafka.apache.org/090/javadoc/index.html?org/apache/kafka/clients/consumer/KafkaConsumer.html">kafka.apache.org</a>
	 *
	 * @param <Type>
	 *            The message type to publish with.
	 */
	private class KafkaConsumerRunner<Type> extends Thread {

		private final AtomicBoolean closed = new AtomicBoolean(false);
		private final KafkaConsumer<String, Type> consumer;

		public KafkaConsumerRunner(final KafkaConsumer<String, Type> consumer) {
			this.consumer = consumer;
		}

		public void run() {
			try {
				while (!closed.get()) {

					ConsumerRecords<String, Type> consumerRecords = ((KafkaConsumer<String, Type>) consumer)
							.poll(pollTimeout);
					if (consumerRecords.isEmpty()) {
						continue;
					}
					Stream<Type> messageTyeStream = StreamSupport.stream(consumerRecords.spliterator(), false)
							.map(rec -> rec.value());
					switch (messageType) {
					case bytearray:
						messageTyeStream.forEach(message -> fireProcess(new String[] { new String((byte[]) message) }));
						break;
					case string:
						messageTyeStream.forEach(message -> fireProcess(new String[] { (String) message }));
						break;
					default:
						throw new IllegalArgumentException("Unsupported message type!");
					}
				}
			} catch (WakeupException e) {
				// Ignore exception if closing
				if (!closed.get())
					throw e;
			} finally {
				consumer.close();
			}
		}

		public void interrupt() {
			closed.set(true);
			consumer.wakeup();
		}

	}

}
