package de.uniol.inf.is.odysseus.wrapper.kafka.physicaloperator.access;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import kafka.javaapi.producer.Producer;
import kafka.producer.ProducerConfig;

/**
 * Transport handler to push data to Kafka as well as to consume data from
 * Kafka.
 *
 * @author Michael Brand
 * @version 0.10.1.0
 * @see <a href="https://kafka.apache.org">kafka.apache.org</a>
 */
public class KafkaTransportHandler extends AbstractTransportHandler {

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
	 * The default properties for Kafka producers.
	 */
	private static final Properties defaultProducerProperties = initProperties("producer.properties");

	/**
	 * The default properties for Kafka consumers.
	 */
	private static final Properties defaultConsumerProperties = initProperties("consumer.properties");

	/**
	 * Loads properties from a file.
	 *
	 * @param filename
	 *            Name of properties file relative to bundle root.
	 * @return The loaded properties or empty properties, if something went
	 *         wrong.
	 */
	private static Properties initProperties(String filename) {
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(new File(filename)));
		} catch (IOException e) {
			log.error("Could not load Kafka properties from file ' {}'!", filename);
		}
		return properties;
	}

	/**
	 * Supported message types for kafka.
	 */
	private static enum MessageType {
		string, bytearray;
	}

	/**
	 * The Kafka topic to publish in.
	 */
	private String topic;

	/**
	 * The Kafka producer, if this transport handler is used to send data.
	 */
	private Optional<Producer<String, ?>> producer = Optional.empty();

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
	private KafkaTransportHandler(final IProtocolHandler<?> protocolHandler, final OptionMap options) {
		super(protocolHandler, options);
		init(options);
	}

	/**
	 * Initializes the properties for Kafka producer/consumer, topic and message
	 * type.
	 *
	 * @param options
	 *            The options set by the user. Used to exchange default settings
	 *            for Kafka.
	 * @throws IllegalArgumentException
	 *             if options does not contain {@link #topicKey} or
	 *             {@link #messageTypeKey}. If value of {@link #messageTypeKey}
	 *             is not in {@link MessageType}.
	 * @throws NullPointerException
	 *             if options is null.
	 */
	private void init(final OptionMap options) throws IllegalArgumentException, NullPointerException {
		Objects.requireNonNull(options);
		options.checkRequiredException(topicKey, messageTypeKey);

		// load default properties
		Properties properties = new Properties();
		properties.putAll(defaultProducerProperties);
		properties.putAll(defaultConsumerProperties);

		// replace default properties with options set by user
		options.getKeySet().stream().filter(key -> properties.containsKey(key))
				.forEach(key -> properties.put(key, options.get(key)));

		// set topic
		topic = options.get(topicKey);

		// create producer: may throw Nullpointer or IllegalArgument, if message
		// type is not correct
		producer = Optional.of(createProducer(new ProducerConfig(properties),
				MessageType.valueOf(options.get(messageTypeKey).toLowerCase())));
	}

	/**
	 * Creates either a string producer or a byte array producer.
	 */
	private static Producer<String, ?> createProducer(final ProducerConfig config, MessageType messageType) {
		switch (messageType) {
		case bytearray:
			return new Producer<String, byte[]>(config);
		case string:
			return new Producer<String, String>(config);
		default:
			throw new IllegalArgumentException("Unknown message type!");
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
		// Nothing to do
	}

	@Override
	public void processOutClose() throws IOException {
		if (producer.isPresent()) {
			producer.get().close();
		}
	}

	@Override
	public void send(byte[] message) throws IOException {
		// TODO Auto-generated method stub

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
	public OutputStream getOutputStream() {
		throw new IllegalArgumentException("Not a pulling transport handler in output mode");
	}

	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler other) {
		// TODO To be implemented
		return false;
	}

}