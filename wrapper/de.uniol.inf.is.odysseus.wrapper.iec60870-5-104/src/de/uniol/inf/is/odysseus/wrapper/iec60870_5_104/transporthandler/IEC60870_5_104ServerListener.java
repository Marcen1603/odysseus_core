package de.uniol.inf.is.odysseus.wrapper.iec60870_5_104.transporthandler;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.openmuc.j60870.ASdu;
import org.openmuc.j60870.Connection;
import org.openmuc.j60870.ConnectionEventListener;
import org.openmuc.j60870.ServerEventListener;

import de.uniol.inf.is.odysseus.wrapper.iec60870_5_104.util.ASDUConverter;

/**
 * Listener implementation for new IEC60870-5-104 clients. <br />
 * It starts the data transfer, when a new client connects, and uses an internal
 * {@link ConnectionEventListener} implementation for new ASDUs from the client
 * as follows. <br />
 * First, it confirms every received ASDU. Second, it converts an ASDU to a
 * tuple (see {@link ASDUConverter#asduToTuple(ASdu)}. Finally, it sends the
 * tuple to the next operators.
 *
 * @author Michael Brand (michael.brand@uol.de)
 *
 */
public class IEC60870_5_104ServerListener implements ServerEventListener {

	/**
	 * The transport handler object.
	 */
	private IEC60870_5_104TransportHandler transportHandler;

	/**
	 * Amount of connected clients
	 */
	private int connectionCounter = 0;

	/**
	 * Creates a new server listener.
	 *
	 * @param transportHandler
	 *            The calling transport handler object.
	 */
	public IEC60870_5_104ServerListener(IEC60870_5_104TransportHandler transportHandler) {
		this.transportHandler = transportHandler;
	}

	@Override
	public void connectionAttemptFailed(IOException arg0) {
		IEC60870_5_104TransportHandler.log.error("Connection attempt failed!", arg0);
	}

	@Override
	public void connectionIndication(Connection connection) {
		int connectionId = connectionCounter++;
		IEC60870_5_104TransportHandler.log.debug(
				"A client has connected using TCP/IP. Will listen for a StartDT request. Connection ID: {}",
				connectionId);

		try {
			connection.waitForStartDT(new ConnectionListener(connection, connectionId), transportHandler.getTimeout());
		} catch (IOException e) {
			IEC60870_5_104TransportHandler.log
					.error("Connection (" + connectionId + ") interrupted while waiting for StartDT. Will quit.", e);
			return;
		} catch (TimeoutException e) {
			IEC60870_5_104TransportHandler.log
					.error("Timeout of connection (" + connectionId + ") while waiting for StartDT. Will quit.", e);
			return;
		}

		IEC60870_5_104TransportHandler.log
				.debug("Started data transfer on connection ({}) Will listen for incoming commands.", connectionId);
	}

	@Override
	public void serverStoppedListeningIndication(IOException e) {
		IEC60870_5_104TransportHandler.log.error("Server has stopped listening for new connections! Will quit.", e);
	}

	/**
	 * Listener implementation for new ASDUs. <br />
	 * First, it confirms every received ASDU. Second, it converts an ASDU to a
	 * tuple (see {@link ASDUConverter#asduToTuple(ASdu)}. Finally, it sends the
	 * tuple to the next operators.
	 *
	 * @author Michael Brand (michael.brand@uol.de)
	 *
	 */
	private class ConnectionListener implements ConnectionEventListener {

		private final Connection connection;
		private final int connectionId;

		public ConnectionListener(Connection connection, int connectionId) {
			this.connection = connection;
			this.connectionId = connectionId;
		}

		@Override
		public void connectionClosed(IOException e) {
			IEC60870_5_104TransportHandler.log.error("Connection (" + connectionId + ") was closed!", e);
		}

		@Override
		public void newASdu(ASdu asdu) {
			// confirm everything
			try {
				connection.sendConfirmation(asdu);
			} catch (IOException e) {
				IEC60870_5_104TransportHandler.log.error(
						"Will quit listening for commands on connection (" + connectionId + ") because of error", e);
				return;
			}

			// Convert ASDU to tuple and send it
			transportHandler.fireProcess(ASDUConverter.asduToTuple(asdu));
		}

	}

}