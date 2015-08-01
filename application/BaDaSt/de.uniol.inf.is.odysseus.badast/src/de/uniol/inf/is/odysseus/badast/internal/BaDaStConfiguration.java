package de.uniol.inf.is.odysseus.badast.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.config.OdysseusBaseConfiguration;

/**
 * Access to the configuration of the BaDaSt application
 * (OdysseusHome/badast.conf"). <br />
 * <br />
 * This includes a ZooKeeper server configuration, a Kafka server configuration
 * and basic Kafka producer settings.
 * 
 * @author Michael Brand
 *
 */
public class BaDaStConfiguration {

	/**
	 * The logger for this class.
	 */
	private static final Logger cLog = LoggerFactory
			.getLogger(BaDaStConfiguration.class);

	/**
	 * The name of the configuration file.
	 */
	private static final String cFileName = OdysseusBaseConfiguration
			.getHomeDir() + "badast.conf";

	/**
	 * The BaDaSt configuration.
	 */
	private static Properties cConfig = new Properties();

	// Load the configuration once.
	static {
		loadConfiguration(cFileName);
	}

	/**
	 * Loads the default configuration and overrides it with properties in the
	 * badast configuration file. Saves it to the file.
	 * 
	 * @param fileName
	 *            The name of the badast configuration file.
	 */
	private static void loadConfiguration(String fileName) {
		setZooKeeperDefaults();
		setKafkaDefaults();
		setProducerDefaults();

		File file = new File(fileName);
		try {
			if (file.createNewFile()) {
				cLog.debug("Created new file " + file.getAbsolutePath());
				saveConfiguration(fileName);
				return;
			}
		} catch (IOException e) {
			cLog.error("Could not create file " + file.getAbsolutePath(), e);
		}

		// File already existed
		FileInputStream stream = null;
		try {
			stream = new FileInputStream(file);
			cConfig.loadFromXML(stream);
		} catch (IOException e) {
			try {
				cConfig.load(stream);
			} catch (IOException e1) {
				cLog.error("Could not load BaDaSt config from file " + fileName);
			}
		} finally {
			saveConfiguration(fileName);
		}
	}

	/**
	 * Gets a part of the configuration either for ZooKeeper server, Kafka
	 * server or Kafka producer.
	 * 
	 * @param prefix
	 *            "zookeeper.", "kafka." or "producer.".
	 * @return All configurations for the wanted entity (keys without the
	 *         prefix).
	 */
	private static Properties getConfig(String prefix) {
		Properties out = new Properties();
		for (Object key : cConfig.keySet()) {
			String strKey = (String) key;
			if (strKey.startsWith(prefix)) {
				out.put(strKey.substring(prefix.length()), cConfig.get(strKey));
			}
		}
		return out;
	}

	/**
	 * Sets all default values for a ZooKeeper server. All keys will begin with
	 * "zookeeper.".
	 */
	private static void setZooKeeperDefaults() {
		// the directory where the snapshot is stored.
		cConfig.put("zookeeper.dataDir", "badast/zookeeper-data");
		// the name of the zookeeper host
		cConfig.put("zookeeper.host.name", "localhost");
		// the port at which the clients will connect
		cConfig.put("zookeeper.clientPort", "2180");
		// disable the per-ip limit on the number of connections since this is a
		// non-production config
		cConfig.put("zookeeper.maxClientCnxns", "0");
	}

	/**
	 * Gets the ZooKeeper configuration.
	 * 
	 * @return All values for a ZooKeeper server.
	 */
	public static Properties getZooKeeperConfig() {
		return getConfig("zookeeper.");
	}

	/**
	 * Sets all default values for a Kafka server. All keys will begin with
	 * "kafka.".
	 */
	private static void setKafkaDefaults() {
		// The id of the broker. This must be set to a unique integer for each
		// broker.
		cConfig.put("kafka.broker.id", "0");
		// The port the socket server listens on
		cConfig.put("kafka.port", "9092");
		// Hostname the broker will bind to. If not set, the server will bind to
		// all interfaces
		cConfig.put("kafka.host.name", "localhost");
		// The number of threads handling network requests
		cConfig.put("kafka.num.network.threads", "3");
		// The number of threads doing disk I/O
		cConfig.put("kafka.num.io.threads", "8");
		// The send buffer (SO_SNDBUF) used by the socket server
		cConfig.put("kafka.socket.send.buffer.bytes", "102400");
		// The receive buffer (SO_RCVBUF) used by the socket server
		cConfig.put("kafka.socket.receive.buffer.bytes", "102400");
		// The maximum size of a request that the socket server will accept
		// (protection against OOM)
		cConfig.put("kafka.socket.request.max.bytes", "104857600");
		// A comma seperated list of directories under which to store log files
		cConfig.put("kafka.log.dirs", "badast/kafka-logs");
		// The default number of log partitions per topic. More partitions allow
		// greater
		// parallelism for consumption, but this will also result in more files
		// across
		// the brokers.
		cConfig.put("kafka.num.partitions", "1");
		// The number of threads per data directory to be used for log recovery
		// at startup and flushing at shutdown.
		// This value is recommended to be increased for installations with data
		// dirs located in RAID array.
		cConfig.put("kafka.num.recovery.threads.per.data.dir", "1");
		// The number of messages to accept before forcing a flush of data to
		// disk
		cConfig.put("kafka.log.flush.interval.messages", "10000");
		// The maximum amount of time a message can sit in a log before we force
		// a flush
		cConfig.put("kafka.log.flush.interval.ms", "1000");
		// The minimum age of a log file to be eligible for deletion
		cConfig.put("kafka.log.retention.hours", "168");
		// The maximum size of a log segment file. When this size is reached a
		// new log segment will be created.
		cConfig.put("kafka.log.segment.bytes", "1073741824");
		// The interval at which log segments are checked to see if they can be
		// deleted according
		// to the retention policies
		cConfig.put("kafka.log.retention.check.interval.ms", "300000");
		// By default the log cleaner is disabled and the log retention policy
		// will default to just delete segments after their retention expires.
		// If log.cleaner.enable=true is set the cleaner will be enabled and
		// individual logs can then be marked for log compaction.
		cConfig.put("kafka.log.cleaner.enable", "false");
		// Zookeeper connection string (see zookeeper docs for details).
		cConfig.put(
				"kafka.zookeeper.connect",
				cConfig.get("zookeeper.host.name") + ":"
						+ cConfig.get("zookeeper.clientPort"));
		// Timeout in ms for connecting to zookeeper
		cConfig.put("kafka.zookeeper.connection.timeout.ms", "6000");
	}

	/**
	 * Gets the Kafka configuration.
	 * 
	 * @return All values for a Kafka server.
	 */
	public static Properties getKafkaConfig() {
		return getConfig("kafka.");
	}

	/**
	 * Sets all default values for a Kafka producer. All keys will begin with
	 * "producer.".
	 */
	private static void setProducerDefaults() {
		cConfig.put(
				"producer.bootstrap.servers",
				cConfig.get("kafka.host.name") + ":"
						+ cConfig.get("kafka.port"));
		cConfig.put("producer.key.serializer",
				"org.apache.kafka.common.serialization.StringSerializer");
	}

	/**
	 * Gets the producer configuration.
	 * 
	 * @return All values for a Kafka producer.
	 */
	public static Properties getProducerConfig() {
		return getConfig("producer.");
	}

	/**
	 * Saves {@link #cConfig} to a given file (xml).
	 * 
	 * @param fileName
	 *            The given file.
	 */
	private static void saveConfiguration(String fileName) {
		try (FileOutputStream out = new FileOutputStream(fileName)) {
			cConfig.storeToXML(out,
					"BaDaSt Property File edit only if you know what you are doing");
		} catch (Exception e) {
			cLog.error("Could not save BaDaSt config to file!", e);
		}
	}

}