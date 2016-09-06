/**
 * 
 */
package de.uniol.inf.is.odysseus.wrapper.kafka.physicaloperator.access;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.LoggerFactory;

import kafka.javaapi.producer.Producer;

/**
 * @author Dennis
 *
 */
public class KafkaProducerRegistry {

	private static KafkaProducerRegistry INSTANCE;

	Map<KafkaTransportHandler, Producer<String, String>> producers;

	private KafkaProducerRegistry() {
		producers = new HashMap<KafkaTransportHandler, Producer<String, String>>();
	}

	protected Producer<String, String> getSemanticallyEqualProducer(KafkaTransportHandler handler) {
		for (KafkaTransportHandler h : producers.keySet()) {
			if (handler.isSemanticallyEqualImpl(h)) {
				LoggerFactory.getLogger(getClass()).info("Found sematically equal kafka producer.");
				return producers.get(h);
			}
		}
		LoggerFactory.getLogger(getClass()).info("No sematically equal kafka producer found.");
		return null;
	}

	protected void addProducer(KafkaTransportHandler handler, Producer<String, String> producer) {
		producers.put(handler, producer);
	}

	protected void removeProducer(KafkaTransportHandler handler) {
		Producer<String, String> removed = this.producers.remove(handler);
		if (!producers.containsValue(removed)) {
			removed.close();
		}
	}

	public static KafkaProducerRegistry getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new KafkaProducerRegistry();
		}
		return INSTANCE;
	}
}
