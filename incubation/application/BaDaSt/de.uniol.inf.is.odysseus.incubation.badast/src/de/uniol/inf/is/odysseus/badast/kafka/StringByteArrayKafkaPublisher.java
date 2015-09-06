package de.uniol.inf.is.odysseus.badast.kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.zookeeper.KeeperException.BadArgumentsException;

import de.uniol.inf.is.odysseus.badast.IPublisher;
import de.uniol.inf.is.odysseus.badast.Record;

/**
 * A {@code StringByteArrayKafkaPublisher} uses a {@code KafkaProducer} to
 * publish a given ByteArray message with a given String topic to a Kafka
 * server.
 * 
 * @author Michael Brand
 */
@SuppressWarnings(value = { "nls" })
public class StringByteArrayKafkaPublisher implements IPublisher<String, byte[]> {

	/**
	 * The used Kafka producer.
	 */
	protected final KafkaProducer<String, byte[]> mProducer;

	/**
	 * Creates a new publisher for Kafka.
	 * 
	 * @param id
	 *            The id for the Kafka producer.
	 */
	public StringByteArrayKafkaPublisher(String id) {
		Properties cfg = KafkaConfiguration.getProducerConfig();
		cfg.put("client.id", id);
		cfg.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
		this.mProducer = new KafkaProducer<>(cfg);
	}

	@Override
	public void publish(Record<String, byte[]> record) throws BadArgumentsException {
		this.mProducer.send(new ProducerRecord<String, byte[]>(record.getTopic(), record.getMessage()));
	}

	@Override
	public void close() throws Exception {
		this.mProducer.close();
	}

}