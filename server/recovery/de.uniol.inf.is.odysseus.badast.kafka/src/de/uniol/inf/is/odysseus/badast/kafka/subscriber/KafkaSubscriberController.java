package de.uniol.inf.is.odysseus.badast.kafka.subscriber;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.badast.subscriber.ISubscriber;
import de.uniol.inf.is.odysseus.badast.subscriber.ISubscriberController;
import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import kafka.api.FetchRequest;
import kafka.api.FetchRequestBuilder;
import kafka.api.OffsetRequest;
import kafka.api.PartitionOffsetRequestInfo;
import kafka.common.ErrorMapping;
import kafka.common.TopicAndPartition;
import kafka.javaapi.FetchResponse;
import kafka.javaapi.OffsetResponse;
import kafka.javaapi.consumer.SimpleConsumer;
import kafka.message.MessageAndOffset;

/**
 * Consumes data, which is stored on a Kafka server. <br />
 * <br />
 * Create a new subscriber and call {@link #start()} afterwards. <br />
 * <br />
 * Based on example from
 * https://cwiki.apache.org/confluence/display/KAFKA/0.8.0+SimpleConsumer+
 * Example
 *
 * @author Michael Brand
 *
 */
public class KafkaSubscriberController extends Thread implements ISubscriberController {

	/**
	 * The logger for this class.
	 */
	static final Logger LOG = LoggerFactory.getLogger(KafkaSubscriberController.class);

	/**
	 * Host name of the Broker.
	 */
	static final String HOST = OdysseusConfiguration.instance.get("kafka.host.name", "localhost");

	/**
	 * Port of the Broker.
	 */
	static final int PORT = OdysseusConfiguration.instance.getInt("kafka.clientPort", 9092);

	/**
	 * The socket timeout threshold for the {@link SimpleConsumer}.
	 */
	static final int TIMEOUT = 100000;

	/**
	 * The buffer size for the {@link SimpleConsumer}.
	 */
	static final int BUFFERSIZE = 64 * 1024;

	/**
	 * The buffer size for the {@link FetchRequest}. <br />
	 * Note: this fetchSize of 100000 might need to be increased if large
	 * batches are written to Kafka.
	 */
	static final int FETCHSIZE = 100000;

	/**
	 * Topic to read from.
	 */
	String topic;

	/**
	 * Partition to read from.
	 */
	int partition;

	/**
	 * True, if the consumption from Kafka server shall continue. Call
	 * {@link #interrupt()} to set it to false.
	 */
	boolean continueConsumption;

	/**
	 * The listener to be updated while reading.
	 */
	ISubscriber listener;

	/**
	 * The offset from where to read.
	 */
	long offset;

	@Override
	public void interrupt() {
		this.continueConsumption = false;
		super.interrupt();
	}

	@Override
	public ISubscriberController newInstance(String stream, int partition, ISubscriber subscriber, long offset) {
		KafkaSubscriberController instance = new KafkaSubscriberController();
		instance.setName("Consumer_" + stream + "_" + partition);
		instance.topic = stream;
		instance.partition = partition;
		instance.listener = subscriber;
		instance.offset = Math.max(offset, instance.getEarliestOffset());
		return instance;
	}

	@Override
	public void run() {
		this.continueConsumption = true;

		// XXX KafkaComsumerThread: Don't know why, but an extra thread is
		// needed. Without that
		// thread, the algorithm fails by FetchResponse fetchResponse =
		// consumer.fetch(req) with some obscure exception from Kafka.
		new Thread(getName() + "_") {

			@Override
			public void run() {
				SimpleConsumer consumer = new SimpleConsumer(HOST, PORT, TIMEOUT, BUFFERSIZE, getName());
				int errorsInARow = 0;
				long readOffset = KafkaSubscriberController.this.offset;
				while (KafkaSubscriberController.this.continueConsumption) {
					try {
						if (consumer == null) {
							consumer = new SimpleConsumer(HOST, PORT, TIMEOUT, BUFFERSIZE, getName());
						}
						FetchRequest req = new FetchRequestBuilder().clientId(getName())
								.addFetch(KafkaSubscriberController.this.topic,
										KafkaSubscriberController.this.partition, readOffset, FETCHSIZE)
								.build();
						FetchResponse fetchResponse = consumer.fetch(req);

						if (fetchResponse.hasError()) {
							short errorCode = fetchResponse.errorCode(KafkaSubscriberController.this.topic,
									KafkaSubscriberController.this.partition);
							throw ErrorMapping.exceptionFor(errorCode);
						}

						long numRead = 0;
						for (MessageAndOffset messageAndOffset : fetchResponse.messageSet(
								KafkaSubscriberController.this.topic, KafkaSubscriberController.this.partition)) {
							long currentOffset = messageAndOffset.offset();
							if (currentOffset < readOffset) {
								LOG.error("Found an old offset: " + currentOffset + " Expecting: " + readOffset);
								continue;
							}
							readOffset = messageAndOffset.nextOffset();
							KafkaSubscriberController.this.listener.onNewMessage(messageAndOffset.message().payload(),
									messageAndOffset.offset());
							numRead++;
						}

						errorsInARow = Math.max(--errorsInARow, 0);
						if (numRead == 0) {
							// Wait for new messages
							KafkaSubscriberController.sleep();
						}
					} catch (Throwable t) {
						// Don't know what error may arise from Kafka. Try to
						// continue
						// but shut down for continuous errors.
						LOG.error("Error while consuming from Kafka. Error counter = "
								+ ++errorsInARow, t);
						if (consumer != null) {
							consumer.close();
							consumer = null;
						}
						if (errorsInARow == 10) {
							KafkaSubscriberController.this.continueConsumption = false;
						}
					}
				}
				if (consumer != null) {
					consumer.close();
				}
			}
		}.start();
	}

	/**
	 * {@link Thread#sleep(long)} for one second.
	 */
	static void sleep() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	/**
	 * Gets the earliest available offset from Kafka.
	 *
	 * @return The earliest offset to be used. May be higher than 0, if older
	 *         elements are already deleted from Kafka server.
	 */
	private long getEarliestOffset() {
		SimpleConsumer consumer = new SimpleConsumer(HOST, PORT, TIMEOUT, BUFFERSIZE, getName());
		long offset = -1;
		try {
			TopicAndPartition topicAndPartition = new TopicAndPartition(this.topic, this.partition);
			Map<TopicAndPartition, PartitionOffsetRequestInfo> requestInfo = new HashMap<>();
			requestInfo.put(topicAndPartition, new PartitionOffsetRequestInfo(OffsetRequest.EarliestTime(), 1));
			kafka.javaapi.OffsetRequest request = new kafka.javaapi.OffsetRequest(requestInfo,
					kafka.api.OffsetRequest.CurrentVersion(), getName());
			OffsetResponse response = consumer.getOffsetsBefore(request);
			if (response.hasError()) {
				short errorCode = response.errorCode(this.topic, this.partition);
				throw ErrorMapping.exceptionFor(errorCode);
			}
			offset = response.offsets(this.topic, this.partition)[0];
		} catch (Throwable t) {
			LOG.error("Error while reading earliest offset from Kafka!", t);
		} finally {
			consumer.close();
		}
		return offset;
	}

}