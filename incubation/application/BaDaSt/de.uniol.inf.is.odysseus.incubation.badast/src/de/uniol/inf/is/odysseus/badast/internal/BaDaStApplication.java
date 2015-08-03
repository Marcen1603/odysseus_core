package de.uniol.inf.is.odysseus.badast.internal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.ZooKeeperServerMain;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.badast.BaDaStConfiguration;
import de.uniol.inf.is.odysseus.badast.BaDaStException;
import kafka.server.KafkaConfig;
import kafka.server.KafkaServerStartable;

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
public class BaDaStApplication {

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
	 * The Kafka Server.
	 */
	private KafkaServerStartable mKafkaServer;

	/**
	 * Starts first a ZooKeeper server and afterwards a Kafka server.
	 */
	public void start() {
		configureLogging();
		startZooKeeperServer();
		wait_();
		startKafkaServer();
		wait_();
		startBaDaStServer();
	}

	/**
	 * Configures the slf4j logging framework with properties from
	 * /config/log4j.properties.
	 */
	private void configureLogging() {
		PropertyConfigurator.configure(BaDaStApplication.class.getClassLoader().getResource("log4j.properties"));
	}

	/**
	 * Starts a ZooKeeper server in a new Thread.
	 */
	private void startZooKeeperServer() {
		new Thread("ZooKeeper Server") {

			@Override
			public void run() {
				try {
					QuorumPeerConfig qpConfig = new QuorumPeerConfig();
					qpConfig.parseProperties(BaDaStConfiguration.getZooKeeperConfig());
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
	 * Starts a Kafka server in a new Thread.
	 */
	private void startKafkaServer() {
		new Thread("Kafka Server") {

			@Override
			public void run() {
				try {
					KafkaConfig config = new KafkaConfig(BaDaStConfiguration.getKafkaConfig());
					BaDaStApplication.this.mKafkaServer = new KafkaServerStartable(config);
					BaDaStApplication.this.mKafkaServer.startup();
				} catch (Exception e) {
					cLog.error("Kafka failed!", e);
					return;
				}
			}

		}.start();
	}

	/**
	 * Starts a BaDaSt server in a new Thread.
	 */
	private void startBaDaStServer() {
		new Thread("BaDaSt Server") {

			/**
			 * True, if the server shall be interrupted.
			 */
			private boolean mToBeInterrupted = false;

			@Override
			public void interrupt() {
				this.mToBeInterrupted = true;
				super.interrupt();
			}

			@Override
			public void run() {
				this.mToBeInterrupted = false;
				Properties config = BaDaStConfiguration.getBaDaStConfig();
				try (ServerSocket serverSocket = new ServerSocket(
						Integer.parseInt((String) config.get("clientPort")))) {
					while (!this.mToBeInterrupted) {
						try (Socket connectionSocket = serverSocket.accept();
								DataInputStream inFromClient = new DataInputStream(connectionSocket.getInputStream());
								DataOutputStream outToClient = new DataOutputStream(
										connectionSocket.getOutputStream())) {
							handleCommandFromClient(inFromClient, outToClient);
						}
					}

				} catch (Exception e) {
					cLog.error("BaDaSt failed!", e);
					return;
				}
			}

			/**
			 * Handles an incoming command: parsing, executing, answering.
			 * 
			 * @param inFromClient
			 *            The incoming stream.
			 * @param outToClient
			 *            The outgoing stream.
			 * @throws IOException
			 *             if any error occurs.
			 */
			private void handleCommandFromClient(DataInputStream inFromClient, DataOutputStream outToClient)
					throws IOException {
				String message = inFromClient.readUTF();
				String answer = null;
				try {
					Properties args = BaDaStCommandProvider.parse(message);
					String command = (String) args.remove(BaDaStCommandProvider.COMMAND_CONFIG);
					answer = BaDaStCommandProvider.execute(command, args);
				} catch (BaDaStException e) {
					answer = "Could not execute " + message + "! " + e.getMessage();
				} finally {
					outToClient.writeUTF(answer);
				}
			}

		}.start();
		cLog.info("BaDaSt server started.");
	}

	/**
	 * Stopps the application.
	 */
	public void stop() {
		if (this.mKafkaServer != null) {
			this.mKafkaServer.shutdown();
		}
		if (cInstance != null && cInstance == this) {
			cInstance = null;
		}
	}

}