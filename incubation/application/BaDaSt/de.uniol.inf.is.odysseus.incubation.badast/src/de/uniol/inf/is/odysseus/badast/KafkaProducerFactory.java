package de.uniol.inf.is.odysseus.badast;

import java.io.IOException;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;

/**
 * Factory to create {@link KafkaProducer}s.
 * 
 * @author Michael Brand
 *
 */
@SuppressWarnings(value = { "nls" })
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
		Properties cfg = BaDaStConfiguration.getProducerConfig();
		cfg.put("client.id", clientId);
		cfg.put("value.serializer",
				"org.apache.kafka.common.serialization.ByteArraySerializer");
		return new KafkaProducer<>(cfg);
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
		Properties cfg = BaDaStConfiguration.getProducerConfig();
		cfg.put("client.id", clientId);
		cfg.put("value.serializer",
				"org.apache.kafka.common.serialization.StringSerializer");
		return new KafkaProducer<>(cfg);
	}

}