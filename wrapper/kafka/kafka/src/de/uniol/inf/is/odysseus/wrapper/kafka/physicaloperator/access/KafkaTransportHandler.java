package de.uniol.inf.is.odysseus.wrapper.kafka.physicaloperator.access;

import java.io.IOException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractPushTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class KafkaTransportHandler extends AbstractPushTransportHandler {

	// TODO add available properties
	public static final String NAME = "Kafka";
	public static final String BROKERLIST = "metadata.broker.list";
	public static final String REQUIRED_ACKS = "request.required.acks";
	public static final String PRODUCER_TYPE = "producer.type";
	public static final String SERIALIZER_CLASS = "serializer.class";
	public static final String PARTITIONER_CLASS = "partitioner.class";
	public static final String KEYNAME = "keyname";

	// own properties
	public static final String TOPIC = "topicName";
	public static final String SHARE_PRODUCER = "shareProducer";
	public static final String SAMPLE_SIZE = "sampleSize";

	private String topicName;
	private boolean shareProducer;
	private long sampleSize;
	private Properties props;
	private Producer<String, String> producer;
	private ProducerConfig config;
	private String keyname;
	private Map<String, List<KeyedMessage<String, String>>> messages = new HashMap<String, List<KeyedMessage<String, String>>>();

	public KafkaTransportHandler() {
		super();
	}

	public KafkaTransportHandler(final IProtocolHandler<?> protocolHandler, OptionMap options) {
		super(protocolHandler, options);
		init(options);
	}

	@Override
	public ITransportHandler createInstance(final IProtocolHandler<?> protocolHandler, final OptionMap options) {
		final KafkaTransportHandler handler = new KafkaTransportHandler(protocolHandler, options);
		return handler;
	}

	private void init(final OptionMap options) {
		props = new Properties();

		if (options.containsKey(BROKERLIST)) {
			props.put(BROKERLIST, options.get(BROKERLIST));
		}
		if (options.containsKey(REQUIRED_ACKS)) {
			props.put(REQUIRED_ACKS, options.get(REQUIRED_ACKS));
		}
		if (options.containsKey(PRODUCER_TYPE)) {
			props.put(PRODUCER_TYPE, options.get(PRODUCER_TYPE));
		}
		if (options.containsKey(SERIALIZER_CLASS)) {
			props.put(SERIALIZER_CLASS, options.get(SERIALIZER_CLASS));
		}
		if (options.containsKey(PARTITIONER_CLASS)) {
			props.put(PARTITIONER_CLASS, options.get(PARTITIONER_CLASS));
		}
		this.keyname = options.get(KEYNAME, null);
		this.sampleSize = options.getLong(SAMPLE_SIZE, 1);
		this.shareProducer = options.getBoolean(SHARE_PRODUCER, false);
		this.topicName = options.get(TOPIC);
	}

	@Override
	public ITransportExchangePattern getExchangePattern() {
		return ITransportExchangePattern.OutOnly;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void processOutOpen() throws IOException {
		if (this.shareProducer) {
			producer = KafkaProducerRegistry.getInstance().getSemanticallyEqualProducer(this);
			if (this.producer == null) {
				config = new ProducerConfig(props);
				producer = new Producer<String, String>(config);
			}
			KafkaProducerRegistry.getInstance().addProducer(this, producer);
		} else {
			config = new ProducerConfig(props);
			producer = new Producer<String, String>(config);
		}
	}

	@Override
	public void processOutClose() throws IOException {
		if (this.shareProducer) {
			KafkaProducerRegistry.getInstance().removeProducer(this);
		} else {
			producer.close();
		}
	}

	@Override
	public void send(byte[] message) throws IOException {
		String msg = new String(message);

		JsonParser parser = new JsonParser();
		JsonObject jsonO = (JsonObject) parser.parse(msg);

		String key = jsonO.get(keyname).getAsString();
		KeyedMessage<String, String> data = new KeyedMessage<String, String>(this.topicName, key, msg);
		List<KeyedMessage<String, String>> list = messages.get(key);
		if (list == null) {
			messages.put(key, new ArrayList<KeyedMessage<String, String>>());
			list = messages.get(key);
		}
		list.add(data);

		if (list.size() >= this.sampleSize) {
			producer.send(list);
			list.clear();
		}

	}

	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler other) {
		if (!(other instanceof KafkaTransportHandler))
			return false;
		KafkaTransportHandler handler = (KafkaTransportHandler) other;
		for (Enumeration<?> p = this.props.propertyNames(); p.hasMoreElements();) {
			String propertyName = p.nextElement().toString();
			if (!this.props.get(propertyName).toString().equals(handler.props.get(propertyName).toString()))
				return false;
		}
		for (Enumeration<?> p = handler.props.propertyNames(); p.hasMoreElements();) {
			String propertyName = p.nextElement().toString();
			if (!this.props.get(propertyName).toString().equals(handler.props.get(propertyName).toString()))
				return false;
		}
		return true;
	}

	@Override
	public void processInOpen() throws IOException {

	}

	@Override
	public void processInClose() throws IOException {

	}

}
