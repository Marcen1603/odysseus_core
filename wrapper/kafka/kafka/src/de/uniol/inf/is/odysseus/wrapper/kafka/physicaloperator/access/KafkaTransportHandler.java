package de.uniol.inf.is.odysseus.wrapper.kafka.physicaloperator.access;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Objects;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractTransportHandler;
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
	public static final Logger log = LoggerFactory.getLogger("KafkaTransportHandler");

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
	public static final String keySerializerKey = "key.serializer";

	/**
	 * The Kafka config key for the value serializer.
	 */
	public static final String valueSerializerKey = "value.serializer";

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
	 * The Kafka producer.
	 */
	private KafkaProducer<String, ?> producer;

	/**
	 * The Kafka topic to publish in.
	 */
	private String topic;

	/**
	 * The message type to publish with.
	 */
	private MessageType messageType;

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
	 *             if options does not contain {@link #topicKey} or
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
		properties.putAll(defaultProducerProperties);
		properties.putAll(defaultConsumerProperties);

		// replace default properties with options set by user
		options.getKeySet().stream().forEach(key -> properties.put(key, options.get(key)));

		// set topic
		topic = options.get(topicKey);

		// may throw Nullpointer or IllegalArgument, if message
		// type is not correct
		messageType = MessageType.valueOf(options.get(messageTypeKey).toLowerCase());
		switch (messageType) {
		case bytearray:
			// delegate = new ByteArrayKafkaTransportHandler(protocolHandler,
			// options, topic, properties);
			properties.put(KafkaTransportHandler.keySerializerKey,
					"org.apache.kafka.common.serialization.ByteArraySerializer");
			properties.put(KafkaTransportHandler.valueSerializerKey,
					"org.apache.kafka.common.serialization.ByteArraySerializer");
			this.producer = new KafkaProducer<String, byte[]>(properties);
			break;
		case string:
			// delegate = new StringKafkaTransportHandler(protocolHandler,
			// options, topic, properties);
			properties.put(KafkaTransportHandler.keySerializerKey,
					"org.apache.kafka.common.serialization.StringSerializer");
			properties.put(KafkaTransportHandler.valueSerializerKey,
					"org.apache.kafka.common.serialization.StringSerializer");
			this.producer = new KafkaProducer<String, String>(properties);
			break;
		default:
			throw new IllegalArgumentException("Unsupported message type!");
		}

		if (log.isDebugEnabled()) {
			log.debug("++++++++++++++++++++++++++++++++++++++");
			log.debug("+ Kafka Transport Handler Properties +");
			log.debug("++++++++++++++++++++++++++++++++++++++");
			log.debug("Topic: {}", topic);
			log.debug("Message Type: {}", messageType);
			log.debug("Kafka Producer Properties:");
			properties.keySet().stream().forEach(key -> log.debug("{}: {}", key, properties.get(key)));
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
		producer.close();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void send(byte[] message) throws IOException {
		switch (messageType) {
		case bytearray:
			((KafkaProducer<String, byte[]>) producer).send(new ProducerRecord<String, byte[]>(topic, message));
			break;
		case string:
			((KafkaProducer<String, String>) producer)
					.send(new ProducerRecord<String, String>(topic, new String(message)));
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
		// TODO To be implemented
	}

	@Override
	public void processInClose() throws IOException {
		// TODO To be implemented
	}

	@Override
	public InputStream getInputStream() {
		// TODO To be implemented
		return null;
	}

	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler other) {
		// TODO To be implemented
		return false;
	}

}