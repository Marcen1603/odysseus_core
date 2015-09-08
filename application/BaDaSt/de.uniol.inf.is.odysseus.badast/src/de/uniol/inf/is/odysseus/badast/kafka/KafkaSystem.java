package de.uniol.inf.is.odysseus.badast.kafka;

import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.ZooKeeperServerMain;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kafka.server.KafkaConfig;
import kafka.server.KafkaServerStartable;

/**
 * Kafka as publish subscribe system to be used for BaDaSt.
 * 
 * @author Michael Brand
 *
 */
@SuppressWarnings(value = { "nls" })
public class KafkaSystem extends Thread {

	/**
	 * The logger for this class.
	 */
	static final Logger cLog = LoggerFactory.getLogger(KafkaSystem.class);

	/**
	 * The Kafka server.
	 */
	private KafkaServerStartable mKafkaServer;

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
			cLog.error("Kafka failed!", e);
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
			cLog.error("ZooKeeper failed!", e);
			return;
		}
	}

	/**
	 * Starts a Kafka server.
	 */
	private void startKafkaServer() {
		try {
			KafkaConfig config = new KafkaConfig(KafkaConfiguration.getKafkaConfig());
			this.mKafkaServer = new KafkaServerStartable(config);
			this.mKafkaServer.startup();
		} catch (Exception e) {
			cLog.error("Kafka failed!", e);
			return;
		}
	}

	@Override
	public void interrupt() {
		this.mKafkaServer.shutdown();
		super.interrupt();
	}

}