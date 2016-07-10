package de.uniol.inf.is.odysseus.badast.application;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.badast.BaDaStConfiguration;
import de.uniol.inf.is.odysseus.badast.BaDaStException;

/**
 * The BaDaSt server.
 * @author Michael Brand
 *
 */
public class BaDaStServer extends Thread {
	
	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(BaDaStServer.class);

	/**
	 * True, if the server shall be interrupted.
	 */
	private boolean toBeInterrupted = false;
	
	/**
	 * Creates a new BaDaSt server.
	 */
	public BaDaStServer() {
		super("BaDaSt Server");
	}

	@Override
	public void interrupt() {
		this.toBeInterrupted = true;
		super.interrupt();
	}

	@Override
	public void run() {
		this.toBeInterrupted = false;
		Properties config = BaDaStConfiguration.getBaDaStConfig();
		try (ServerSocket serverSocket = new ServerSocket(Integer.parseInt((String) config.get("clientPort")))) {
			while (!this.toBeInterrupted) {
				try (Socket connectionSocket = serverSocket.accept();
						DataInputStream inFromClient = new DataInputStream(connectionSocket.getInputStream());
						DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream())) {
					handleCommandFromClient(inFromClient, outToClient);
				}
			}

		} catch (Exception e) {
			LOG.error("BaDaSt failed!", e);
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
	
}