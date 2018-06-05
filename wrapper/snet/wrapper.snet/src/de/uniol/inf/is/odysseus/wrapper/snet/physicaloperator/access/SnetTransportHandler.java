package de.uniol.inf.is.odysseus.wrapper.snet.physicaloperator.access;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fraunhofer.iis.kom.wsn.messages.WSNMessageDefault;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.infoservice.InfoService;
import de.uniol.inf.is.odysseus.core.infoservice.InfoServiceFactory;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.wrapper.snet.communication.SNetCommunicationHandler;

/**
 * 
 * This wrapper uses a TCP-Socket-connection to register at a
 * Fraunhofer-S-Net-Gateway using the wsn technology. It handles
 * the communication to the wsn-publish-subscribe gateway and registers for the
 * required services.
 * 
 * @author Jan Benno Meyer zu Holte
 *
 */
public class SnetTransportHandler extends AbstractTransportHandler {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(SnetTransportHandler.class);

	private SNetCommunicationHandler communicator;

	public static final String NAME = "SNetClient";
	// The host name
	public static final String HOST = "host";
	// The port
	public static final String PORT = "port";
	// The services that will be registered
	public static final String SERVICES = "services";
	// The pull update interval
	public static final String UPDATE_INTERVAL = "update_interval";

	private String host = "";
	private int port = 5001;
	private List<Integer> services = new LinkedList<Integer>();

	static final InfoService infoService = InfoServiceFactory
			.getInfoService(SnetTransportHandler.class.getName());

	public SnetTransportHandler() {
		super();
	}

	public SnetTransportHandler(final IProtocolHandler<?> protocolHandler,
			final OptionMap options) {
		super(protocolHandler, options);
		this.init();
	}

	@Override
	public ITransportHandler createInstance(
			final IProtocolHandler<?> protocolHandler, final OptionMap options) {
		final SnetTransportHandler handler = new SnetTransportHandler(
				protocolHandler, options);
		return handler;
	}

	@Override
	public InputStream getInputStream() {
		// Need to transfer the messages
		return null;
	}

	@Override
	public String getName() {
		return SnetTransportHandler.NAME;
	}

	@Override
	public OutputStream getOutputStream() {
		// In only handler
		return null;
	}

	@Override
	public boolean isSemanticallyEqualImpl(final ITransportHandler o) {
		if (!(o instanceof SnetTransportHandler)) {
			return false;
		}
		final SnetTransportHandler other = (SnetTransportHandler) o;
		if (!this.host.equals(other.host)) {
			return false;
		} else if (this.port == other.port) {
			return false;
		}
		return true;
	}

	@Override
	public void processInClose() throws IOException {
		this.communicator.close();
	}

	@Override
	public void processInOpen() throws UnknownHostException, IOException {
		if(communicator == null){
			communicator = new SNetCommunicationHandler(this.getName(), this.host, (short) this.port);
		}
		this.communicator.initializeListener();
		this.communicator.start();
		this.communicator.connect();
		this.communicator.register();
		this.communicator.subscribeForServices(services);
	}

	@Override
	public void processOutClose() throws IOException {
		// In only handler
	}

	@Override
	public void processOutOpen() throws UnknownHostException, IOException {
		// In only handler
	}

	@Override
	public void send(final byte[] message) throws IOException {
		// In only
	}

	private void init() {
		final OptionMap options = this.getOptionsMap();

		if (!options.containsKey(HOST)) {
			host = "127.0.0.1";
		} else {
			if (options.containsKey(HOST)) {
				host = options.get(HOST);
			}
		}

		if (!options.containsKey(PORT)) {
			port = 8080;
		} else {
			if (options.containsKey(PORT)) {
				port = options.getInt(PORT, -1);
			}
		}
		
		if(communicator == null){
			communicator = new SNetCommunicationHandler(this.getName(), this.host, (short) this.port);
		}
	}

	public ArrayList<WSNMessageDefault> getInputMessages() {
		return communicator.getMessages();
	}
}
