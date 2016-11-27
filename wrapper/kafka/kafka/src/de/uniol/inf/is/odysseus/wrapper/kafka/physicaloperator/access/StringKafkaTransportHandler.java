package de.uniol.inf.is.odysseus.wrapper.kafka.physicaloperator.access;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

/**
 * Transport handler to be used as delegate by {@link KafkaTransportHandler} to
 * push string data to Kafka as well as to consume string data from Kafka.
 *
 * @author Michael Brand
 * @version 0.10.1.0
 * @see <a href="https://kafka.apache.org">kafka.apache.org</a>
 */
public class StringKafkaTransportHandler extends AbstractTransportHandler {

	/**
	 * The sample (list of elements to be as a sample).
	 */
	private List<KeyedMessage<String, String>> sample = new ArrayList<>();

	/**
	 * The Kafka producer.
	 */
	private Producer<String, String> producer;

	/**
	 * The Kafka topic to publish in.
	 */
	private String topic;

	/**
	 * The sample size (how many messages to sample before sending them).
	 */
	private int sampleSize;

	/**
	 * Creates a new Kafka transport handler for byte array messages.
	 *
	 * @param protocolHandler
	 *            The protocol handler to use.
	 * @param options
	 *            The options set by the user. Used to exchange default settings
	 *            for Kafka.
	 * @param topic
	 *            The Kafka topic to publish in.
	 * @param sampleSize
	 *            The sample size (how many messages to sample before sending
	 *            them).
	 * @param producerConfig
	 *            The config for the producer
	 */
	public StringKafkaTransportHandler(final IProtocolHandler<?> protocolHandler, final OptionMap options,
			final String topic, final int sampleSize, final ProducerConfig producerConfig) {
		super(protocolHandler, options);
		this.topic = topic;
		this.sampleSize = sampleSize;
		this.producer = new Producer<String, String>(producerConfig);
	}

	@Override
	public ITransportHandler createInstance(final IProtocolHandler<?> protocolHandler, final OptionMap options) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void processOutOpen() throws IOException {
		sample.clear();
	}

	@Override
	public void processOutClose() throws IOException {
		if (!sample.isEmpty()) {
			producer.send(sample);
			sample.clear();
		}
		producer.close();
	}

	@Override
	public void send(byte[] message) throws IOException {
		sample.add(new KeyedMessage<String, String>(topic, new String(message)));
		if (sample.size() == sampleSize) {
			producer.send(sample);
			if (KafkaTransportHandler.log.isDebugEnabled()) {
				KafkaTransportHandler.log.debug("++++++++++++++++++++++++");
				KafkaTransportHandler.log.debug("+ Sent sample to Kafka +");
				KafkaTransportHandler.log.debug("++++++++++++++++++++++++");
				KafkaTransportHandler.log.debug("Topic: {}", topic);
				sample.stream().forEach(m -> KafkaTransportHandler.log.debug(m.message()));
			}
			sample.clear();
		}
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
	public boolean isSemanticallyEqualImpl(ITransportHandler other) {
		// TODO To be implemented
		return false;
	}

	@Override
	public String getName() {
		throw new UnsupportedOperationException();
	}

	@Override
	public InputStream getInputStream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OutputStream getOutputStream() {
		// TODO Auto-generated method stub
		return null;
	}

}