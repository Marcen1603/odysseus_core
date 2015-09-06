package de.uniol.inf.is.odysseus.badast.internal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.badast.BaDaStConfiguration;
import de.uniol.inf.is.odysseus.badast.BaDaStException;
import de.uniol.inf.is.odysseus.badast.kafka.KafkaSystem;

// XXX Replace Kafka source code with Kafka jar file. First try to do it resulted in run-time errors for the Kafka server.
/**
 * BaDaSt - Backup of Data Streams application. <br />
 * <br />
 * Starts first a publish subscribe system and a BaDaSt server. <br />
 * <br />
 * Type help in the OSGi console to see all BaDaSt commands.
 * 
 * @author Michael Brand
 *
 */
@SuppressWarnings(value = { "nls" })
public class BaDaStApplication implements IApplication {

	/**
	 * The logger for this class.
	 */
	static final Logger cLog = LoggerFactory.getLogger(BaDaStApplication.class);

	/**
	 * The publish subscribe system to use.
	 */
	private Thread mPubSubSystem;

	/**
	 * The BaDaSt server.
	 */
	private Thread mBaDaStServer = new Thread("BaDaSt Server") {

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
			try (ServerSocket serverSocket = new ServerSocket(Integer.parseInt((String) config.get("clientPort")))) {
				while (!this.mToBeInterrupted) {
					try (Socket connectionSocket = serverSocket.accept();
							DataInputStream inFromClient = new DataInputStream(connectionSocket.getInputStream());
							DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream())) {
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

	};

	@Override
	public void stop() {
		if (this.mPubSubSystem != null) {
			this.mPubSubSystem.interrupt();
		}
		this.mBaDaStServer.interrupt();
	}

	@Override
	public Object start(IApplicationContext context) throws Exception {
		context.applicationRunning();
		PropertyConfigurator.configure(BaDaStApplication.class.getClassLoader().getResource("log4j.properties"));
		this.mPubSubSystem = new KafkaSystem();
		this.mPubSubSystem.start();
		this.mBaDaStServer.start();
		return IApplicationContext.EXIT_ASYNC_RESULT;
	}

}