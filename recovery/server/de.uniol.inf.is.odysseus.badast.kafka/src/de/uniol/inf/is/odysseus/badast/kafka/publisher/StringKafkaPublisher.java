package de.uniol.inf.is.odysseus.badast.kafka.publisher;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import de.uniol.inf.is.odysseus.badast.BaDaStException;
import de.uniol.inf.is.odysseus.badast.kafka.KafkaConfiguration;
import de.uniol.inf.is.odysseus.badast.publisher.IPublisher;
import de.uniol.inf.is.odysseus.badast.publisher.Record;

/**
 * A {@code StringKafkaPublisher} uses a {@code KafkaProducer} to publish a
 * given String message with a given topic to a Kafka server.
 * 
 * @author Michael Brand
 */
public class StringKafkaPublisher implements IPublisher<String> {

	/**
	 * The used Kafka producer.
	 */
	private KafkaProducer<String, String> producer;
	
	/**
	 * Initializes the used Kafka producer.
	 */
	void init(String id) {
		Properties cfg = KafkaConfiguration.getProducerConfig();
		cfg.put("client.id", id);
		cfg.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		this.producer = new KafkaProducer<>(cfg);
	}

	@Override
	public void publish(Record<String> record) throws BaDaStException {
		this.producer.send(new ProducerRecord<String, String>(record.getTopic(), record.getMessage()));
	}

	@Override
	public void close() throws Exception {
		this.producer.close();
	}

	@Override
	public IPublisher<String> newInstance(String id) {
		StringKafkaPublisher instance = new StringKafkaPublisher();
		instance.init(id);
		return instance;
	}

	@Override
	public Class<String> getMessageType() {
		return String.class;
	}

}