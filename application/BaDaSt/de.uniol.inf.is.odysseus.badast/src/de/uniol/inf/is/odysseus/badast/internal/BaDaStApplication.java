package de.uniol.inf.is.odysseus.badast.internal;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.ZooKeeperServerMain;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kafka.server.KafkaConfig;
import kafka.server.KafkaServerStartable;

/**
 * BaDaSt - Backup of Data Streams application. <br />
 * <br />
 * Starts first a ZooKeeper server and afterwards a Kafka server.
 * 
 * @author Michael Brand
 *
 */
public class BaDaStApplication implements BundleActivator {

	/**
	 * The logger for this class.
	 */
	private static final Logger cLog = LoggerFactory.getLogger(BaDaStApplication.class);

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
	 * The host name.
	 */
	private String mHost = "localhost";

	/**
	 * Gets the host.
	 * 
	 * @return The host name of the Kafka server.
	 */
	public String getHost() {
		return this.mHost;
	}

	/**
	 * The port number.
	 */
	private int mPort = 9092;

	/**
	 * Gets the port.
	 * 
	 * @return The port number of the Kafka server.
	 */
	public int getPort() {
		return this.mPort;
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
		PropertyConfigurator.configure(BaDaStApplication.class.getClassLoader().getResource("config/log4j.properties"));
	}

	/**
	 * Starts a ZooKeeper server in a new Thread with properties from
	 * /config/zookeeper.properties.
	 */
	private void startZooKeeperServer() {
		final Properties props = new Properties();
		try (InputStream stream = BaDaStApplication.class.getClassLoader()
				.getResourceAsStream("config/zookeeper.properties")) {
			props.load(stream);
		} catch (IOException e) {
			cLog.error("ZooKeeper failed!", e);
			return;
		}

		new Thread("ZooKeeper Server") {

			@Override
			public void run() {
				try {
					QuorumPeerConfig qpConfig = new QuorumPeerConfig();
					qpConfig.parseProperties(props);
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
		final Properties props = new Properties();
		try (InputStream stream = BaDaStApplication.class.getClassLoader()
				.getResourceAsStream("config/kafka.properties")) {
			props.load(stream);
		} catch (IOException e) {
			cLog.error("Kafka failed!", e);
			return;
		}

		if (props.containsKey("host.name")) {
			this.mHost = props.getProperty("host.name");
		}
		if (props.containsKey("port")) {
			this.mPort = Integer.parseInt(props.getProperty("port"));
		}

		new Thread("Kafka Server") {

			@Override
			public void run() {
				try {
					KafkaConfig config = new KafkaConfig(props);
					BaDaStApplication.this.mKafkaServer = new KafkaServerStartable(config);
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