package de.uniol.inf.is.odysseus.wrapper.iec60870_5_104.client;

import java.io.IOException;

import org.openmuc.j60870.ASdu;
import org.openmuc.j60870.Connection;
import org.openmuc.j60870.ConnectionEventListener;

import de.uniol.inf.is.odysseus.wrapper.iec60870_5_104.AbstractIEC104TransportHandler;
import de.uniol.inf.is.odysseus.wrapper.iec60870_5_104.util.ASDUConverter;

/**
 * Listener implementation for a new IEC60870-5-104 connection. <br />
 * It waits for new ASDUs and handles them as follows. <br />
 * First, it confirms every received ASDU. Second, it converts an ASDU to a
 * tuple (see {@link ASDUConverter#asduToTuple(ASdu)}. Finally, it sends the
 * tuple to the next operators.
 *
 * @author Michael Brand (michael.brand@uol.de)
 *
 */
public class IEC104ClientListener implements ConnectionEventListener {

	/**
	 * The transport handler object.
	 */
	private IEC104ClientTransportHandler transportHandler;

	/**
	 * The opened connection to the sever.
	 */
	private Connection connection;

	/**
	 * Creates a new client listener.
	 *
	 * @param transportHandler
	 *            The calling transport handler object.
	 */
	public IEC104ClientListener(IEC104ClientTransportHandler transportHandler, Connection connection) {
		this.transportHandler = transportHandler;
		this.connection = connection;
	}

	@Override
	public void connectionClosed(IOException e) {
		AbstractIEC104TransportHandler.log.error("Received connection closed signal!", e);
	}

	@Override
	public void newASdu(ASdu asdu) {
		// confirm everything
		try {
			connection.sendConfirmation(asdu);
		} catch (IOException e) {
			AbstractIEC104TransportHandler.log.error("Will quit listening for commands on connection because of error",
					e);
			return;
		}

		// Convert ASDU to tuple and send it
		transportHandler.fireProcess(ASDUConverter.asduToTuple(asdu));
	}

}