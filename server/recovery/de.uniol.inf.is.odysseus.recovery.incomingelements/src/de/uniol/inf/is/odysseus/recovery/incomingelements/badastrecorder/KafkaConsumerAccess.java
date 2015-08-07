package de.uniol.inf.is.odysseus.recovery.incomingelements.badastrecorder;

import java.util.List;

import kafka.api.FetchRequest;
import kafka.api.FetchRequestBuilder;
import kafka.javaapi.FetchResponse;
import kafka.javaapi.consumer.SimpleConsumer;
import kafka.message.MessageAndOffset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

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
	 * Host name of the Broker.
	 */
	private final String mHost;

	/**
	 * Port of the Broker.
	 */
	private final int mPort;

	/**
	 * True, if the consumption from Kafka server shall continue. Call
	 * {@link #interrupt()} to set it to false.
	 */
	private boolean mContinueConsumption;

	/**
	 * The consumer to be updated while reading.
	 */
	private final IKafkaConsumer mConsumer;

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
	 */
	public KafkaConsumerAccess(String topic, int partition,
			List<String> brokers, IKafkaConsumer consumer) {
		super("Consumer_" + topic + "_" + partition);
		this.mTopic = topic;
		this.mPartition = partition;
		// TODO need to read the port out from BaDaStConfiguration
		this.mHost = "localhost";
		this.mPort = 9092;
		this.mConsumer = consumer;
	}

	/**
	 * Creates a new Kafka consumer with default values.
	 * 
	 * @param topic
	 *            Topic to read from (source name).
	 * @param consumer
	 *            The consumer to be updated while reading.
	 */
	public KafkaConsumerAccess(String topic, IKafkaConsumer consumer) {
		this(topic, 0, Lists.newArrayList("0"), consumer);
	}

	@Override
	public void run() {
		// TODO Always read from 0?
		long readOffset = 0;
		this.mContinueConsumption = true;
		SimpleConsumer consumer = new SimpleConsumer(this.mHost, this.mPort,
				cTimeOut, cBufferSize, getName());
		;
		while (this.mContinueConsumption) {
			FetchRequest req = new FetchRequestBuilder()
					.clientId(getName())
					.addFetch(this.mTopic, this.mPartition, readOffset,
							cFetchSize).build();
			FetchResponse fetchResponse = consumer.fetch(req);

			if (fetchResponse.hasError()) {
				// Something went wrong!
				cLog.error("Error fetching data from the Broker:" + this.mHost
						+ " Reason: "
						+ fetchResponse.errorCode(this.mTopic, this.mPartition)
						+ ". See ErrorMapping.java");
				consumer.close();
				break;
			}

			long numRead = 0;
			for (MessageAndOffset messageAndOffset : fetchResponse.messageSet(
					this.mTopic, this.mPartition)) {
				long currentOffset = messageAndOffset.offset();
				if (currentOffset < readOffset) {
					cLog.error("Found an old offset: " + currentOffset
							+ " Expecting: " + readOffset);
					continue;
				}
				readOffset = messageAndOffset.nextOffset();
				try {
					this.mConsumer.onNewMessage(messageAndOffset.message()
							.payload(), messageAndOffset.offset());
				} catch (Throwable t) {
					cLog.error("Error while calling Kafka consumer "
							+ this.mConsumer, t);
				}
				numRead++;
			}

			// TODO Always continue reading?
			if (numRead == 0) {
				sleep();
			}
		}
		if (consumer != null) {
			consumer.close();
		}
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

}