package de.uniol.inf.is.odysseus.wrapper.iec60870_5_104.client;

import java.io.IOException;

import org.openmuc.j60870.ASdu;
import org.openmuc.j60870.Connection;
import org.openmuc.j60870.ConnectionEventListener;

import de.uniol.inf.is.odysseus.wrapper.iec60870_5_104.ASDUConverter;
import de.uniol.inf.is.odysseus.wrapper.iec60870_5_104.AbstractIEC104TransportHandler;

// TODO javaDoc
public class IEC104ClientListener implements ConnectionEventListener {

	private IEC104ClientTransportHandler transportHandler;

	private Connection connection;

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