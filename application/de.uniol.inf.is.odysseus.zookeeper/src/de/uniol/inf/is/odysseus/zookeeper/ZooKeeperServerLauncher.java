package de.uniol.inf.is.odysseus.zookeeper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.ZooKeeperServerMain;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Application to start a ZooKeeper server.
 * 
 * @author Michael Brand
 *
 */
public class ZooKeeperServerLauncher extends Thread {

	/**
	 * The logger for this class.
	 */
	private static final Logger cLog = LoggerFactory
			.getLogger(ZooKeeperServerLauncher.class);

	/**
	 * The configuration for the ZooKeeper server.
	 */
	private final ServerConfig mConfig;

	/**
	 * Creates a new thread to start a ZooKeeper server with a given
	 * {@link ServerConfig}.
	 * 
	 * @param config
	 *            The configuration for the ZooKeeper server.
	 */
	public ZooKeeperServerLauncher(ServerConfig config) {
		this.mConfig = config;
	}

	/**
	 * Creates a new thread to start a ZooKeeper server with a given
	 * {@link QuorumPeerConfig}.
	 * 
	 * @param config
	 *            The configuration for the ZooKeeper server.
	 */
	public ZooKeeperServerLauncher(QuorumPeerConfig config) {
		this(initializeConfig(config));
	}

	/**
	 * Creates the configuration for the ZooKeeper server based on a given
	 * {@link QuorumPeerConfig}.
	 * 
	 * @param qpConfig
	 *            The base for the configuration.
	 * @return The configuration for the ZooKeeper server.
	 */
	private static ServerConfig initializeConfig(QuorumPeerConfig qpConfig) {
		ServerConfig config = new ServerConfig();
		config.readFrom(qpConfig);
		return config;
	}

	/**
	 * Creates a new thread to start a ZooKeeper server with a given
	 * {@link Properties}.
	 * 
	 * @param config
	 *            The configuration for the ZooKeeper server.
	 */
	public ZooKeeperServerLauncher(Properties config) throws Exception {
		this(initializeConfig(config));
	}

	/**
	 * Creates the configuration for the ZooKeeper server based on a given
	 * {@link Properties}.
	 * 
	 * @param props
	 *            The base for the configuration.
	 * @return The configuration for the ZooKeeper server.
	 */
	private static ServerConfig initializeConfig(Properties props)
			throws Exception {
		QuorumPeerConfig qpConfig = new QuorumPeerConfig();
		qpConfig.parseProperties(props);
		return initializeConfig(qpConfig);
	}

	/**
	 * Creates a new thread to start a ZooKeeper server with the default
	 * configuration.
	 * 
	 * @see config/zookeeper.properties
	 */
	public ZooKeeperServerLauncher() throws Exception {
		this(initializeConfig(getDefaultProperties()));
	}

	/**
	 * Creates the configuration for the ZooKeeper server based on the default
	 * configuration.
	 * 
	 * @see config/zookeeper.properties
	 * @return The configuration for the ZooKeeper server.
	 */
	private static Properties getDefaultProperties() throws IOException {
		Properties props = new Properties();
		try (InputStream stream = ZooKeeperServerLauncher.class
				.getClassLoader().getResourceAsStream("zookeeper.properties")) {
			if (stream != null) {
				props.load(stream);
			}
		}
		return props;
	}

	@Override
	public void run() {
		try {
			new ZooKeeperServerMain().runFromConfig(this.mConfig);
		} catch (IOException e) {
			cLog.error("ZooKeeper failed!", e);
			return;
		}
	}

	/**
	 * Starts a ZooKeeper server with default configuration.
	 * 
	 * @param args
	 *            No effects.
	 */
	public static void main(String[] args) {
		// configure logging framework
		PropertyConfigurator.configure(ZooKeeperServerLauncher.class
				.getClassLoader().getResource("log4j.properties"));
		// start ZooKeeper server
		try {
			new ZooKeeperServerLauncher().start();
		} catch (Exception e) {
			cLog.error("ZooKeeper failed!", e);
			return;
		}
	}

}