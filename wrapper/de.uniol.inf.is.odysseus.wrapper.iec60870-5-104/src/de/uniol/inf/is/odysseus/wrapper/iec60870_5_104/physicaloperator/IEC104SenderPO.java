package de.uniol.inf.is.odysseus.wrapper.iec60870_5_104.physicaloperator;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import org.openmuc.j60870.ASdu;
import org.openmuc.j60870.ClientConnectionBuilder;
import org.openmuc.j60870.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.wrapper.iec60870_5_104.transporthandler.IEC60870_5_104ClientListener;
import de.uniol.inf.is.odysseus.wrapper.iec60870_5_104.util.ASDUConverter;

//TODO This operator exists only for test purposes.
public class IEC104SenderPO extends AbstractPipe<Tuple<IMetaAttribute>, Tuple<IMetaAttribute>> {

	/**
	 * The logger instance for the IEC60870-5-104 wrapper.
	 */
	static final Logger log = LoggerFactory.getLogger("IEC60870-5-104");

	/**
	 * The j60870 connection, if the transport handler is in client mode
	 * (sending telegrams)
	 */
	private Connection clientConnection;

	private InetAddress host;

	private int port;

	private int timeout;

	public IEC104SenderPO(String host, int port, int timeout) {
		try {
			this.host = InetAddress.getByName(host);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.port = port;
		this.timeout = timeout;
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation, port);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
		try {
			startClient();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void process_close() {
		super.process_close();
		stopClient();
	}

	@Override
	protected void process_next(Tuple<IMetaAttribute> object, int port) {
		transfer(object, port);

		final ASdu asdu = ASDUConverter.TupleToASDU(object);
		try {
			clientConnection.send(asdu);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Starts a new j60870 {@link #clientConnection}.
	 *
	 * @throws IOException
	 *             may be thrown by
	 *             {@link Connection#startDataTransfer(org.openmuc.j60870.ConnectionEventListener, int)}.
	 */
	private void startClient() throws IOException {
		clientConnection = new ClientConnectionBuilder(host).setPort(port).connect();
		try {
			clientConnection.startDataTransfer(new IEC60870_5_104ClientListener(), timeout);
			log.debug("j60870 client successfully connected");
		} catch (TimeoutException e) {
			log.error("Connection closed for the following reason", e);
		}
	}

	/**
	 * Stops a running j60870 {@link #clientConnection}.
	 */
	private void stopClient() {
		if (clientConnection != null) {
			clientConnection.close();
			log.debug("j60870 client successfully disconnected");
		}
	}

}