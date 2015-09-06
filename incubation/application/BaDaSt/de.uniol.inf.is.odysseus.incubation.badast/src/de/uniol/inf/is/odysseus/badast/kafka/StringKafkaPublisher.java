package de.uniol.inf.is.odysseus.badast.kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import de.uniol.inf.is.odysseus.badast.BaDaStException;
import de.uniol.inf.is.odysseus.badast.IPublisher;
import de.uniol.inf.is.odysseus.badast.Record;

/**
 * A {@code StringStringKafkaPublisher} uses a {@code KafkaProducer} to publish
 * a given String message with a given topic to a Kafka server.
 * 
 * @author Michael Brand
 */
@SuppressWarnings(value = { "nls" })
public class StringKafkaPublisher implements IPublisher<String> {

	/**
	 * The used Kafka producer.
	 */
	protected final KafkaProducer<String, String> mProducer;

	/**
	 * Creates a new publisher for Kafka.
	 * 
	 * @param id
	 *            The id for the Kafka producer.
	 */
	public StringKafkaPublisher(String id) {
		Properties cfg = KafkaConfiguration.getProducerConfig();
		cfg.put("client.id", id);
		cfg.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		this.mProducer = new KafkaProducer<>(cfg);
	}

	@Override
	public void publish(Record<String> record) throws BaDaStException {
		this.mProducer.send(new ProducerRecord<String, String>(record.getTopic(), record.getMessage()));
	}

	@Override
	public void close() throws Exception {
		this.mProducer.close();
	}

}