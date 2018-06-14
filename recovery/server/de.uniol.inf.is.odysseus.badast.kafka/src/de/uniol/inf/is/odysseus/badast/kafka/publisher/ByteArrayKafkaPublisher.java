package de.uniol.inf.is.odysseus.badast.kafka.publisher;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import de.uniol.inf.is.odysseus.badast.BaDaStException;
import de.uniol.inf.is.odysseus.badast.kafka.KafkaConfiguration;
import de.uniol.inf.is.odysseus.badast.publisher.IPublisher;
import de.uniol.inf.is.odysseus.badast.publisher.Record;

/**
 * A {@code ByteArrayKafkaPublisher} uses a {@code KafkaProducer} to publish a
 * given ByteArray message with a given topic to a Kafka server.
 * 
 * @author Michael Brand
 */
public class ByteArrayKafkaPublisher implements IPublisher<byte[]> {

	/**
	 * The used Kafka producer.
	 */
	private KafkaProducer<String, byte[]> producer;
	
	/**
	 * Initializes the used Kafka producer.
	 */
	void init(String id) {
		Properties cfg = KafkaConfiguration.getProducerConfig();
		cfg.put("client.id", id);
		cfg.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
		this.producer = new KafkaProducer<>(cfg);
	}

	@Override
	public void publish(Record<byte[]> record) throws BaDaStException {
		this.producer.send(new ProducerRecord<String, byte[]>(record.getTopic(), record.getMessage()));
	}

	@Override
	public void close() throws Exception {
		this.producer.close();
	}

	@Override
	public IPublisher<byte[]> newInstance(String id) {
		ByteArrayKafkaPublisher instance = new ByteArrayKafkaPublisher();
		instance.init(id);
		return instance;
	}

	@Override
	public Class<Byte> getMessageType() {
		return Byte.class;
	}

}