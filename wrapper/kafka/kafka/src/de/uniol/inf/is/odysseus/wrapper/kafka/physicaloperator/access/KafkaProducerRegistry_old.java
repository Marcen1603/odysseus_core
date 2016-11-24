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
public class KafkaProducerRegistry_old {

	private static KafkaProducerRegistry_old INSTANCE;

	Map<KafkaTransportHandler_old, Producer<String, String>> producers;

	private KafkaProducerRegistry_old() {
		producers = new HashMap<KafkaTransportHandler_old, Producer<String, String>>();
	}

	protected Producer<String, String> getSemanticallyEqualProducer(KafkaTransportHandler_old handler) {
		for (KafkaTransportHandler_old h : producers.keySet()) {
			if (handler.isSemanticallyEqualImpl(h)) {
				LoggerFactory.getLogger(getClass()).info("Found sematically equal kafka producer.");
				return producers.get(h);
			}
		}
		LoggerFactory.getLogger(getClass()).info("No sematically equal kafka producer found.");
		return null;
	}

	protected void addProducer(KafkaTransportHandler_old handler, Producer<String, String> producer) {
		producers.put(handler, producer);
	}

	protected void removeProducer(KafkaTransportHandler_old handler) {
		Producer<String, String> removed = this.producers.remove(handler);
		if (!producers.containsValue(removed)) {
			removed.close();
		}
	}

	public static KafkaProducerRegistry_old getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new KafkaProducerRegistry_old();
		}
		return INSTANCE;
	}
}
