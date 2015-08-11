package de.uniol.inf.is.odysseus.recovery.incomingelements.badastrecorder;

import java.util.HashMap;
import java.util.Map;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;

/**
 * Consumes data, which is stored on a Kafka server. <br />
 * <br />
 * In the context of this recovery component, this are stored elements of a
 * certain data stream (source name = topic). <br />
 * <br />
 * Create a new consumer and call {@link #start()} afterwards. <br />
 * <br />
 * Based on example from
 * https://cwiki.apache.org/confluence/display/KAFKA/0.8.0+SimpleConsumer+
 * Example
 * 
 * @author Michael Brand
 *
 */
public class KafkaConsumerAccess extends Thread {

	/**
	 * The logger for this class.
	 */
	private static final Logger cLog = LoggerFactory
			.getLogger(KafkaConsumerAccess.class);

	/**
	 * Host name of the Broker.
	 */
	private static final String cHost = OdysseusConfiguration.get(
			"kafka.host.name", "localhost");

	/**
	 * Port of the Broker.
	 */
	private static final int cPort = OdysseusConfiguration.getInt(
			"kafka.clientPort", 9092);

	/**
	 * The socket timeout threshold for the {@link SimpleConsumer}.
	 */
	private static final int cTimeOut = 100000;

	/**
	 * The buffer size for the {@link SimpleConsumer}.
	 */
	private static final int cBufferSize = 64 * 1024;

	/**
	 * The buffer size for the {@link FetchRequest}. <br />
	 * Note: this fetchSize of 100000 might need to be increased if large
	 * batches are written to Kafka.
	 */
	private static final int cFetchSize = 100000;

	/**
	 * Topic to read from.
	 */
	private final String mTopic;

	/**
	 * Partition to read from.
	 */
	private final int mPartition;

	/**
	 * True, if the consumption from Kafka server shall continue. Call
	 * {@link #interrupt()} to set it to false.
	 */
	private boolean mContinueConsumption;

	/**
	 * The listener to be updated while reading.
	 */
	private final IKafkaConsumer mListener;

	/**
	 * The offset from where to read.
	 */
	private long mOffset;

	@Override
	public void interrupt() {
		this.mContinueConsumption = false;
		super.interrupt();
	}

	/**
	 * Creates a new Kafka consumer access.
	 * 
	 * @param topic
	 *            Topic to read from (source name).
	 * @param partition
	 *            Partition to read from (default 0).
	 * @param consumer
	 *            The consumer to be updated while reading.
	 * @param offset
	 *            The offset from where to read.
	 */
	public KafkaConsumerAccess(String topic, int partition,
			IKafkaConsumer consumer, long offset) {
		super("Consumer_" + topic + "_" + partition);
		this.mTopic = topic;
		this.mPartition = partition;
		this.mListener = consumer;
		this.mOffset = offset;
	}

	/**
	 * Creates a new Kafka consumer with default partition.
	 * 
	 * @param topic
	 *            Topic to read from (source name).
	 * @param consumer
	 *            The consumer to be updated while reading.
	 * @param offset
	 *            The offset from where to read.
	 */
	public KafkaConsumerAccess(String topic, IKafkaConsumer consumer,
			long offset) {
		this(topic, 0, consumer, offset);
	}

	/**
	 * Creates a new Kafka consumer access with earliest possible offset to use.
	 * 
	 * @param topic
	 *            Topic to read from (source name).
	 * @param partition
	 *            Partition to read from (default 0).
	 * @param consumer
	 *            The consumer to be updated while reading.
	 */
	public KafkaConsumerAccess(String topic, int partition,
			IKafkaConsumer consumer) {
		this(topic, partition, consumer, -1);
		this.mOffset = getEarliestOffset();
	}

	/**
	 * Creates a new Kafka consumer with default partition and earliest possible
	 * offset to use.
	 * 
	 * @param topic
	 *            Topic to read from (source name).
	 * @param consumer
	 *            The consumer to be updated while reading.
	 */
	public KafkaConsumerAccess(String topic, IKafkaConsumer consumer) {
		this(topic, 0, consumer);
	}

	@Override
	public void run() {
		this.mContinueConsumption = true;

		// XXX KafkaComsumerThread: Don't know why, but an extra thread is
		// needed. Without that
		// thread, the algorithm fails by FetchResponse fetchResponse =
		// consumer.fetch(req) with some obscure exception from Kafka.
		new Thread(getName() + "_") {

			@Override
			public void run() {
				SimpleConsumer consumer = new SimpleConsumer(cHost, cPort,
						cTimeOut, cBufferSize, getName());
				int errorsInARow = 0;
				long readOffset = KafkaConsumerAccess.this.mOffset;
				while (KafkaConsumerAccess.this.mContinueConsumption) {
					try {
						if (consumer == null) {
							consumer = new SimpleConsumer(cHost, cPort,
									cTimeOut, cBufferSize, getName());
						}
						FetchRequest req = new FetchRequestBuilder()
								.clientId(getName())
								.addFetch(KafkaConsumerAccess.this.mTopic,
										KafkaConsumerAccess.this.mPartition,
										readOffset, cFetchSize).build();
						FetchResponse fetchResponse = consumer.fetch(req);

						if (fetchResponse.hasError()) {
							short errorCode = fetchResponse.errorCode(
									KafkaConsumerAccess.this.mTopic,
									KafkaConsumerAccess.this.mPartition);
							throw ErrorMapping.exceptionFor(errorCode);
						}

						long numRead = 0;
						for (MessageAndOffset messageAndOffset : fetchResponse
								.messageSet(KafkaConsumerAccess.this.mTopic,
										KafkaConsumerAccess.this.mPartition)) {
							long currentOffset = messageAndOffset.offset();
							if (currentOffset < readOffset) {
								cLog.error("Found an old offset: "
										+ currentOffset + " Expecting: "
										+ readOffset);
								continue;
							}
							readOffset = messageAndOffset.nextOffset();
							KafkaConsumerAccess.this.mListener.onNewMessage(
									messageAndOffset.message().payload(),
									messageAndOffset.offset());
							numRead++;
						}

						errorsInARow = Math.max(--errorsInARow, 0);
						if (numRead == 0) {
							// Wait for new messages
							KafkaConsumerAccess.sleep();
						}
					} catch (Throwable t) {
						// Don't know what error may arise from Kafka. Try to
						// continue
						// but shut down for continuous errors.
						cLog.error("Error while consuming from Kafka. Reason: "
								+ t.getMessage() + ". Error counter = "
								+ ++errorsInARow);
						consumer.close();
						consumer = null;
						if (errorsInARow == 10) {
							KafkaConsumerAccess.this.mContinueConsumption = false;
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
	private static void sleep() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			cLog.error(e.getMessage(), e);
		}
	}

	/**
	 * Gets the earliest available offset from Kafka.
	 * 
	 * @return The earliest offset to be used. May be higher than 0, if older
	 *         elements are already deleted from Kafka server.
	 */
	private long getEarliestOffset() {
		SimpleConsumer consumer = new SimpleConsumer(cHost, cPort, cTimeOut,
				cBufferSize, getName());
		long offset = -1;
		try {
			TopicAndPartition topicAndPartition = new TopicAndPartition(
					this.mTopic, this.mPartition);
			Map<TopicAndPartition, PartitionOffsetRequestInfo> requestInfo = new HashMap<TopicAndPartition, PartitionOffsetRequestInfo>();
			requestInfo.put(topicAndPartition, new PartitionOffsetRequestInfo(
					OffsetRequest.EarliestTime(), 1));
			kafka.javaapi.OffsetRequest request = new kafka.javaapi.OffsetRequest(
					requestInfo, kafka.api.OffsetRequest.CurrentVersion(),
					getName());
			OffsetResponse response = consumer.getOffsetsBefore(request);
			if (response.hasError()) {
				short errorCode = response.errorCode(this.mTopic,
						this.mPartition);
				throw ErrorMapping.exceptionFor(errorCode);
			}
			offset = response.offsets(this.mTopic, this.mPartition)[0];
		} catch (Throwable t) {
			cLog.error("Error while reading earliest offset from Kafka. Reason: "
					+ t.getMessage() + ".");
		} finally {
			consumer.close();
		}
		return offset;
	}

}