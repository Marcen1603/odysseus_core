package de.uniol.inf.is.odysseus.badast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;

/**
 * Helper class to send commands to BaDaSt per TCP. Host and port are set in
 * badast.conf.
 *
 * @author Michael Brand
 *
 */
public class BaDaStSender {

	/**
	 * The logger for this class.
	 */
	private static final Logger cLog = LoggerFactory.getLogger(BaDaStSender.class);

	/**
	 * The host name of the BaDaSt server.
	 */
	private static String cHost = OdysseusConfiguration.instance.get("badast.host.name", "localhost");

	/**
	 * The port number of the BaDaSt server.
	 */
	private static int cPort = OdysseusConfiguration.instance.getInt("badast.clientPort", 6789);

	/**
	 * Checks, if the BaDaSt application is available.
	 *
	 * @return True, if the server is up.
	 */
	public static boolean isBaDaStServerAvailable() {
		try (Socket clientSocket = new Socket(cHost, cPort);
				DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
				DataInputStream inFromServer = new DataInputStream(clientSocket.getInputStream())) {
			outToServer.writeUTF("command=lsRecorders");
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * Sends a command to the BaDaSt application.
	 *
	 * @param command
	 *            The command to send.
	 * @return The answer from the BaDaSt server.
	 */
	public static String sendComand(String command) {
		try (Socket clientSocket = new Socket(cHost, cPort);
				DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
				DataInputStream inFromServer = new DataInputStream(clientSocket.getInputStream())) {
			outToServer.writeUTF(command);
			String answer = inFromServer.readUTF();
			cLog.debug("Info from BaDaSt: {}", answer);
			return answer;
		} catch (IOException e) {
			cLog.error("Could not send command " + command + " to BaDaSt at " + cHost + ":" + cPort, e);
			return null;
		}
	}

	/**
	 * Sends a command to the BaDaSt application in order to create a recorder
	 * for a data source.
	 *
	 * @param config
	 *            The configuration for the BaDaSt recorder.
	 * @return The name of the created BaDaSt recorder.
	 */
	public static String sendCreateCommand(Properties config) {
		String answer = sendComand(createCreateCommand(config));
		if (answer != null && answer.startsWith("Created BaDaSt recorder")) {
			String[] split = answer.split(" ");
			return split[split.length - 1];
		}
		cLog.error("Could not create BaDaSt recorder: {}", answer);
		return null;
	}

	/**
	 * Creates a command for the BaDaSt application in order to create a
	 * recorder for a data source.
	 *
	 * @param config
	 *            The configuration for the BaDaSt recorder.
	 * @return A string to be sent to BaDaSt.
	 */
	private static String createCreateCommand(Properties config) {
		StringBuffer out = new StringBuffer("command=createRecorder ");
		Iterator<String> propsIter = config.stringPropertyNames().iterator();
		while (propsIter.hasNext()) {
			String key = propsIter.next();
			out.append(key + "=" + config.getProperty(key));
			if (propsIter.hasNext()) {
				out.append(" ");
			}
		}
		return out.toString();
	}

	/**
	 * Sends a command to the BaDaSt application in order to start a recorder
	 * for a data source.
	 *
	 * @param recorder
	 *            The name of the BaDaSt recorder.
	 */
	public static void sendStartCommand(String recorder) {
		sendComand(createStartCommand(recorder));
	}

	/**
	 * Creates a command for the BaDaSt application in order to start the
	 * recorder a the data source.
	 *
	 * @param recorder
	 *            The name of the BaDaSt recorder.
	 * @return A string to be sent to BaDaSt.
	 */
	private static String createStartCommand(String recorder) {
		StringBuffer out = new StringBuffer("command=startRecorder ");
		out.append("name=" + recorder);
		return out.toString();
	}

	/**
	 * Sends a command to the BaDaSt application in order to close a recorder
	 * for a data source.
	 *
	 * @param recorder
	 *            The name of the BaDaSt recorder.
	 */
	public static void sendCloseCommand(String recorder) {
		sendComand(createCloseCommand(recorder));
	}

	/**
	 * Creates a command for the BaDaSt application in order to close the
	 * recorder a the data source.
	 *
	 * @param recorder
	 *            The name of the BaDaSt recorder.
	 * @return A string to be sent to BaDaSt.
	 */
	private static String createCloseCommand(String recorder) {
		StringBuffer out = new StringBuffer("command=closeRecorder ");
		out.append("name=" + recorder);
		return out.toString();
	}

	/**
	 * Sends a command to the BaDaSt application in order to list all existing
	 * recorders.
	 *
	 * @return A set of all existing recorders, with the following pattern for
	 *         each set entry: <br />
	 *         recordertype_sourcename.
	 */
	public static Set<String> sendLsRecordersComand() {
		Set<String> out = new HashSet<>();
		String[] lines = sendComand("command=lsRecorders").split("\n");
		for (String line : lines) {
			// pattern for each line: name = xyz, type = xyz
			int equalityIndex = line.indexOf('=');
			int commaIndex = line.indexOf(',');
			if (equalityIndex != -1 && commaIndex > equalityIndex) {
				out.add(line.substring(equalityIndex + 1, commaIndex).trim());
			}
		}
		return out;
	}

}