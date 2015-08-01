package de.uniol.inf.is.odysseus.badast.internal;

import kafka.server.KafkaConfig;
import kafka.server.KafkaServerStartable;

import org.apache.log4j.PropertyConfigurator;
import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.ZooKeeperServerMain;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BaDaSt - Backup of Data Streams application. <br />
 * <br />
 * Starts first a ZooKeeper server and afterwards a Kafka server. <br />
 * <br />
 * Type help in the OSGi console to see all BaDaSt commands.
 * 
 * @author Michael Brand
 *
 */
public class BaDaStApplication implements BundleActivator {

	/**
	 * The logger for this class.
	 */
	private static final Logger cLog = LoggerFactory
			.getLogger(BaDaStApplication.class);

	/**
	 * The only instance.
	 */
	private static BaDaStApplication cInstance;

	/**
	 * Gets the only instance.
	 * 
	 * @return The application.
	 */
	public static BaDaStApplication getInstance() {
		if (cInstance == null) {
			cInstance = new BaDaStApplication();
		}
		return cInstance;
	}

	/**
	 * The Kafka Server.
	 */
	private KafkaServerStartable mKafkaServer;

	/**
	 * Starts first a ZooKeeper server and afterwards a Kafka server.
	 */
	private void run() {
		configureLogging();
		startZooKeeperServer();
		wait_();
		startKafkaServer();
	}

	/**
	 * Configures the slf4j logging framework with properties from
	 * /config/log4j.properties.
	 */
	private void configureLogging() {
		PropertyConfigurator.configure(BaDaStApplication.class.getClassLoader()
				.getResource("log4j.properties"));
	}

	/**
	 * Starts a ZooKeeper server in a new Thread with properties from
	 * /config/zookeeper.properties.
	 */
	private void startZooKeeperServer() {
		new Thread("ZooKeeper Server") {

			@Override
			public void run() {
				try {
					QuorumPeerConfig qpConfig = new QuorumPeerConfig();
					qpConfig.parseProperties(BaDaStConfiguration
							.getZooKeeperConfig());
					ServerConfig sConfig = new ServerConfig();
					sConfig.readFrom(qpConfig);
					ZooKeeperServerMain server = new ZooKeeperServerMain();
					server.runFromConfig(sConfig);
				} catch (Exception e) {
					cLog.error("ZooKeeper failed!", e);
					return;
				}
			}

		}.start();
	}

	/**
	 * Waits one second.
	 */
	private void wait_() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			cLog.error("ZooKeeper/Kafka failed!", e);
			return;
		}
	}

	/**
	 * Starts a Kafka server in a new Thread with properties from
	 * /config/kafka.properties.
	 */
	private void startKafkaServer() {
		new Thread("Kafka Server") {

			@Override
			public void run() {
				try {
					KafkaConfig config = new KafkaConfig(
							BaDaStConfiguration.getKafkaConfig());
					BaDaStApplication.this.mKafkaServer = new KafkaServerStartable(
							config);
					BaDaStApplication.this.mKafkaServer.startup();
				} catch (Exception e) {
					cLog.error("Kafka failed!", e);
					return;
				}
			}

		}.start();
	}

	@Override
	public void start(BundleContext context) throws Exception {
		cInstance = this;
		run();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		if (this.mKafkaServer != null) {
			this.mKafkaServer.shutdown();
		}
		if (cInstance != null && cInstance == this) {
			cInstance = null;
		}
	}

}