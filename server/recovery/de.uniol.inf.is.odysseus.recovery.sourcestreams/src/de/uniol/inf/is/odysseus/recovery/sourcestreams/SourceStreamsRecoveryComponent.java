package de.uniol.inf.is.odysseus.recovery.sourcestreams;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.badast.BaDaStConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.recovery.IRecoveryComponent;
import de.uniol.inf.is.odysseus.recovery.systemlog.ISysLogEntry;

/**
 * The source stream recovery component handles the backup and recovery of
 * incoming data streams of given source. <br />
 * <br />
 * The component uses the Backup of Data Streams (BaDaSt) application.
 * 
 * @author Michael Brand
 *
 */
public class SourceStreamsRecoveryComponent implements IRecoveryComponent {

	/**
	 * The logger for this class.
	 */
	private static final Logger cLog = LoggerFactory.getLogger(SourceStreamsRecoveryComponent.class);

	@Override
	public String getName() {
		return "Source Streams";
	}

	@Override
	public void recover(List<Integer> queryIds, ISession caller, List<ISysLogEntry> log) throws Exception {
		// TODO implement SourceStreamsRecoveryComponent.recover
	}

	@Override
	public void activateBackup(List<Integer> queryIds, ISession caller) {
		// TODO implement SourceStreamsRecoveryComponent.activateBackup
	}

	@Override
	public IRecoveryComponent newInstance(Properties config) {
		SourceStreamsRecoveryComponent component = new SourceStreamsRecoveryComponent();
		component.configureBaDaSt(config);
		return component;
	}

	/**
	 * Sends a command to the BaDaSt application in order to create a recorder
	 * for the data source.
	 * 
	 * @param config
	 *            The configuration for the BaDaSt recorder.
	 */
	private void configureBaDaSt(Properties config) {
		Properties badastCfg = BaDaStConfiguration.getBaDaStConfig();
		String command = createCreateCommand(config);
		String host = badastCfg.getProperty("host.name");
		int port = Integer.parseInt(badastCfg.getProperty("clientPort"));
		try (Socket clientSocket = new Socket(host, port);
				DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
				DataInputStream inFromServer = new DataInputStream(clientSocket.getInputStream())) {
			outToServer.writeUTF(command);
			String answer = inFromServer.readUTF();
			cLog.debug("Info from BaDaSt: {}", answer);
		} catch (IOException e) {
			cLog.error("Could not send command " + command + " to BaDaSt at " + host + ":" + port);
		}
	}

	/**
	 * Creates a command for the BaDaSt application in order to create a
	 * recorder for the data source.
	 * 
	 * @param config
	 *            The configuration for the BaDaSt recorder.
	 * @param A
	 *            string to be sent to BaDaSt.
	 */
	private String createCreateCommand(Properties config) {
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

}