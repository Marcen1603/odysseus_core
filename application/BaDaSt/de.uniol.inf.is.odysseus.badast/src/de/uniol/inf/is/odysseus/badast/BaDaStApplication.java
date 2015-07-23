package de.uniol.inf.is.odysseus.badast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import kafka.server.KafkaConfig;
import kafka.server.KafkaServerStartable;

import org.apache.log4j.PropertyConfigurator;
import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.ZooKeeperServerMain;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BaDaSt - Backup of Data Streams application. <br />
 * <br />
 * Starts first a ZooKeeper server and afterwards a Kafka server.
 * 
 * @author Michael Brand
 *
 */
public class BaDaStApplication {

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
	 * @return The application.
	 */
	public static BaDaStApplication getInstance() {
		if(cInstance == null) {
			cInstance = new BaDaStApplication();
		}
		return cInstance;
	}

	/**
	 * Starts first a ZooKeeper server and afterwards a Kafka server.
	 * 
	 * @param args
	 *            No functionality.
	 */
	public static void main(String[] args) {
		getInstance().run();
	}
	
	/**
	 * The host name.
	 */
	private String mHost = "localhost";
	
	/**
	 * Gets the host.
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
	 * @return The port number of the Kafka server.
	 */
	public int getPort() {
		return this.mPort;
	}

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
		final Properties props = new Properties();
		try (InputStream stream = BaDaStApplication.class.getClassLoader()
				.getResourceAsStream("zookeeper.properties")) {
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
				.getResourceAsStream("kafka.properties")) {
			props.load(stream);
		} catch (IOException e) {
			cLog.error("Kafka failed!", e);
			return;
		}
		
		if(props.containsKey("host.name")) {
			this.mHost = props.getProperty("host.name");
		}
		if(props.containsKey("port")) {
			this.mPort = Integer.parseInt(props.getProperty("port"));
		}

		new Thread("Kafka Server") {

			@Override
			public void run() {
				try {
					KafkaConfig config = new KafkaConfig(props);
					KafkaServerStartable kafkaServer = new KafkaServerStartable(
							config);
					kafkaServer.startup();
				} catch (Exception e) {
					cLog.error("Kafka failed!", e);
					return;
				}
			}

		}.start();
	}

}