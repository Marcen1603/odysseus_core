package de.uniol.inf.is.odysseus.badast.kafka;

import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.ZooKeeperServerMain;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.badast.IPubSubSystem;
import kafka.server.KafkaConfig;
import kafka.server.KafkaServerStartable;

/**
 * Kafka as publish subscribe system to be used for BaDaSt.
 * 
 * @author Michael Brand
 *
 */
public class KafkaSystem extends Thread implements IPubSubSystem {

	/**
	 * The logger for this class.
	 */
	static final Logger LOG = LoggerFactory.getLogger(KafkaSystem.class);

	/**
	 * The Kafka server.
	 */
	private KafkaServerStartable kafkaServer;

	@Override
	public void run() {
		new Thread("ZooKeeper Server") {

			@Override
			public void run() {
				startZooKeeperServer();
			}

		}.start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			LOG.error("Kafka failed!", e);
			return;
		}

		startKafkaServer();
	}

	/**
	 * Starts a ZooKeeper server.
	 */
	static void startZooKeeperServer() {
		try {
			QuorumPeerConfig qpConfig = new QuorumPeerConfig();
			qpConfig.parseProperties(KafkaConfiguration.getZooKeeperConfig());
			ServerConfig sConfig = new ServerConfig();
			sConfig.readFrom(qpConfig);
			ZooKeeperServerMain server = new ZooKeeperServerMain();
			server.runFromConfig(sConfig);
		} catch (Exception e) {
			LOG.error("ZooKeeper failed!", e);
			return;
		}
	}

	/**
	 * Starts a Kafka server.
	 */
	private void startKafkaServer() {
		try {
			KafkaConfig config = new KafkaConfig(KafkaConfiguration.getKafkaConfig());
			this.kafkaServer = new KafkaServerStartable(config);
			this.kafkaServer.startup();
		} catch (Exception e) {
			LOG.error("Kafka failed!", e);
			return;
		}
	}

	@Override
	public void interrupt() {
		this.kafkaServer.shutdown();
		super.interrupt();
	}

	@Override
	public IPubSubSystem newInstance() {
		return new KafkaSystem();
	}

}