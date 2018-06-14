package de.uniol.inf.is.odysseus.badast.kafka;

import java.util.Properties;

import de.uniol.inf.is.odysseus.badast.BaDaStConfiguration;

/**
 * Access to the configuration of the ZooKeeper and Kafka servers
 * (OdysseusHome/badast.conf").
 * 
 * @author Michael Brand
 *
 */
public class KafkaConfiguration {

	// Set defaults if needed
	static {
		setDefaultsIfNeeded();
	}

	/**
	 * Checks, if defaults need to be set and does it, if necessary.
	 */
	private static void setDefaultsIfNeeded() {
		if (getZooKeeperConfig().isEmpty()) {
			setZooKeeperDefaults();
		}
		if (getKafkaConfig().isEmpty()) {
			setKafkaDefaults();
		}
		if (getProducerConfig().isEmpty()) {
			setProducerDefaults();
		}
	}

	/**
	 * Sets all default values for a ZooKeeper server. All keys will begin with
	 * "zookeeper.".
	 */
	private static void setZooKeeperDefaults() {
		// the directory where the snapshot is stored.
		BaDaStConfiguration.add("zookeeper.dataDir", "badast/zookeeper-data");
		// the name of the zookeeper host
		BaDaStConfiguration.add("zookeeper.host.name", "localhost");
		// the port at which the clients will connect
		BaDaStConfiguration.add("zookeeper.clientPort", "2180");
		// disable the per-ip limit on the number of connections since this is a
		// non-production config
		BaDaStConfiguration.add("zookeeper.maxClientCnxns", "0");
	}

	/**
	 * Gets the ZooKeeper configuration.
	 * 
	 * @return All values for a ZooKeeper server.
	 */
	public static Properties getZooKeeperConfig() {
		return BaDaStConfiguration.getConfig("zookeeper.");
	}

	/**
	 * Sets all default values for a Kafka server. All keys will begin with
	 * "kafka.".
	 */
	private static void setKafkaDefaults() {
		// The id of the broker. This must be set to a unique integer for each
		// broker.
		BaDaStConfiguration.add("kafka.broker.id", "0");
		// The port the socket server listens on
		BaDaStConfiguration.add("kafka.port", "9092");
		// Hostname the broker will bind to. If not set, the server will bind to
		// all interfaces
		BaDaStConfiguration.add("kafka.host.name", "localhost");
		// The number of threads handling network requests
		BaDaStConfiguration.add("kafka.num.network.threads", "3");
		// The number of threads doing disk I/O
		BaDaStConfiguration.add("kafka.num.io.threads", "8");
		// The send buffer (SO_SNDBUF) used by the socket server
		BaDaStConfiguration.add("kafka.socket.send.buffer.bytes", "102400");
		// The receive buffer (SO_RCVBUF) used by the socket server
		BaDaStConfiguration.add("kafka.socket.receive.buffer.bytes", "102400");
		// The maximum size of a request that the socket server will accept
		// (protection against OOM)
		BaDaStConfiguration.add("kafka.socket.request.max.bytes", "104857600");
		// A comma seperated list of directories under which to store log files
		BaDaStConfiguration.add("kafka.log.dirs", "badast/kafka-logs");
		// The default number of log partitions per topic. More partitions allow
		// greater
		// parallelism for consumption, but this will also result in more files
		// across
		// the brokers.
		BaDaStConfiguration.add("kafka.num.partitions", "1");
		// The number of threads per data directory to be used for log recovery
		// at startup and flushing at shutdown.
		// This value is recommended to be increased for installations with data
		// dirs located in RAID array.
		BaDaStConfiguration.add("kafka.num.recovery.threads.per.data.dir", "1");
		// The minimum age of a log file to be eligible for deletion
		BaDaStConfiguration.add("kafka.log.retention.hours", "168");
		// The maximum size of a log segment file. When this size is reached a
		// new log segment will be created.
		BaDaStConfiguration.add("kafka.log.segment.bytes", "1073741824");
		// The interval at which log segments are checked to see if they can be
		// deleted according
		// to the retention policies
		BaDaStConfiguration.add("kafka.log.retention.check.interval.ms", "300000");
		// Zookeeper connection string (see zookeeper docs for details).
		BaDaStConfiguration.add("kafka.zookeeper.connect", BaDaStConfiguration.get().get("zookeeper.host.name") + ":"
				+ BaDaStConfiguration.get().get("zookeeper.clientPort"));
		// Timeout in ms for connecting to zookeeper
		BaDaStConfiguration.add("kafka.zookeeper.connection.timeout.ms", "6000");
	}

	/**
	 * Gets the Kafka configuration.
	 * 
	 * @return All values for a Kafka server.
	 */
	public static Properties getKafkaConfig() {
		return BaDaStConfiguration.getConfig("kafka.");
	}

	/**
	 * Sets all default values for a Kafka producer. All keys will begin with
	 * "producer.".
	 */
	private static void setProducerDefaults() {
		// list of brokers used for bootstrapping knowledge about the rest of the cluster
		// format: host1:port1,host2:port2 ...
		BaDaStConfiguration.add("producer.metadata.broker.list", BaDaStConfiguration.get().get("kafka.host.name") + ":" + BaDaStConfiguration.get().get("kafka.port"));
		BaDaStConfiguration.add("producer.bootstrap.servers", (String) BaDaStConfiguration.get().get("producer.metadata.broker.list"));
		// specifies whether the messages are sent asynchronously (async) or synchronously (sync)
		BaDaStConfiguration.add("producer.producer.type", "sync");
		// specify the compression codec for all data generated: none, gzip, snappy, lz4.
		// the old config values work as well: 0, 1, 2, 3 for none, gzip, snappy, lz4, respectively
		BaDaStConfiguration.add("producer.compression.codec", "none");
		// message encoder
		BaDaStConfiguration.add("producer.serializer.class", "kafka.serializer.DefaultEncoder");
		BaDaStConfiguration.add("producer.key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
	}

	/**
	 * Gets the producer configuration.
	 * 
	 * @return All values for a Kafka producer.
	 */
	public static Properties getProducerConfig() {
		return BaDaStConfiguration.getConfig("producer.");
	}

}
