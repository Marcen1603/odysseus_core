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
	 * Creates a new producer.
	 * 
	 * @return A {@link KafkaProducer} with properties from
	 *         config/producer.properties.
	 * @throws IOException
	 *             if any error occurs while loading the properties from file.
	 */
	public static <K, V> KafkaProducer<K, V> createKafkaProducer() throws IOException {
		Properties cfg = new Properties();
		try (InputStream stream = KafkaProducerFactory.class.getClassLoader()
				.getResourceAsStream("config/producer.properties")) {
			cfg.load(stream);
			return new KafkaProducer<>(cfg);
		}
	}

}