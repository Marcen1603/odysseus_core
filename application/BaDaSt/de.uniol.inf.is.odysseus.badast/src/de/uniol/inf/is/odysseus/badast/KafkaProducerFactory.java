package de.uniol.inf.is.odysseus.badast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;

/**
 * Factory to create {@link KafkaProducer}s.
 * 
 * @author Michael Brand
 *
 */
public class KafkaProducerFactory {

	/**
	 * Creates a new producer with a String topic and byte array values.
	 * 
	 * @param clientId
	 *            The id/name for the producer.
	 * @return A {@link KafkaProducer} with properties from
	 *         config/producer.properties.
	 * @throws IOException
	 *             if any error occurs while loading the properties from file.
	 */
	public static KafkaProducer<String, byte[]> createKafkaProducerByteArray(
			String clientId) throws IOException {
		Properties cfg = new Properties();
		cfg.put("client.id", clientId);
		cfg.put("value.serializer",
				"org.apache.kafka.common.serialization.ByteArraySerializer");
		try (InputStream stream = KafkaProducerFactory.class.getClassLoader()
				.getResourceAsStream("config/producer.properties")) {
			cfg.load(stream);
			return new KafkaProducer<>(cfg);
		}
	}

	/**
	 * Creates a new producer with a String topic and String values.
	 * 
	 * @param clientId
	 *            The id/name for the producer.
	 * @return A {@link KafkaProducer} with properties from
	 *         config/producer.properties.
	 * @throws IOException
	 *             if any error occurs while loading the properties from file.
	 */
	public static KafkaProducer<String, String> createKafkaProducerString(
			String clientId) throws IOException {
		Properties cfg = new Properties();
		cfg.put("client.id", clientId);
		cfg.put("value.serializer",
				"org.apache.kafka.common.serialization.StringSerializer");
		try (InputStream stream = KafkaProducerFactory.class.getClassLoader()
				.getResourceAsStream("config/producer.properties")) {
			cfg.load(stream);
			return new KafkaProducer<>(cfg);
		}
	}

}