package de.uniol.inf.is.odysseus.wrapper.xovis.physicaloperator.access;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramSocket;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.infoservice.InfoService;
import de.uniol.inf.is.odysseus.core.infoservice.InfoServiceFactory;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.wrapper.xovis.communication.util.XovisCommunicationHandler;

/**
 * 
 * This wrapper uses a TCP-Socket- or UDP-connection to register at Xovis stereocameras to access event-stream and object-stream data. 
 * It handles the incoming data by transforming google protocol buffer input into whatever needed protocol
 * 
 * @author Jan Benno Meyer zu Holte
 *
 */
public class XovisTransportHandler extends AbstractTransportHandler {

	public enum XOVISSTREAMTYPE {
		EVENTSTREAM, OBJECTSTREAM
	}
	
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(XovisTransportHandler.class);

	private XovisCommunicationHandler communicator;

	public static final String NAME = "Xovis";
	// The host name
	public static final String HOST = "host";
	// The port
	public static final String PORT = "port";	
	// The incoming xovis stream type
	public static final String STREAMTYPE = "streamtype";
	
	private String streamType = "event";
	private XOVISSTREAMTYPE type;
	private String host = "";
	private int port = 5001;

	/** In and output for data transfer */
	// Pseudo stream delivering extracted HDLC frames from snetclient listener for corresponding handler
	private InputStream in;

	static final InfoService infoService = InfoServiceFactory
			.getInfoService(XovisTransportHandler.class.getName());

	public XovisTransportHandler() {
		super();
	}

	public XovisTransportHandler(final IProtocolHandler<?> protocolHandler,
			final OptionMap options) {
		super(protocolHandler, options);
		this.init();
	}

	@Override
	public ITransportHandler createInstance(
			final IProtocolHandler<?> protocolHandler, final OptionMap options) {
		final XovisTransportHandler handler = new XovisTransportHandler(
				protocolHandler, options);
		return handler;
	}

	@Override
	public InputStream getInputStream() {
		// return unprocessed hdlc frames
		return in;
	}

	@Override
	public String getName() {
		return XovisTransportHandler.NAME;
	}

	@Override
	public OutputStream getOutputStream() {
		// In only handler
		return null;
	}

	@Override
	public boolean isSemanticallyEqualImpl(final ITransportHandler o) {
		if (!(o instanceof XovisTransportHandler)) {
			return false;
		}
		final XovisTransportHandler other = (XovisTransportHandler) o;
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
		this.communicator = null;
	}

	@Override
	public void processInOpen() throws UnknownHostException, IOException {
		if(this.communicator == null){
			communicator = new XovisCommunicationHandler(this.host, this.port, this.type);
			// System.err.println("Communicator running on port " + this.port);
			try {
				in = communicator.getInputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		this.communicator.start();
		this.communicator.connect();
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
		
		if (!options.containsKey(STREAMTYPE)) {
			streamType = "event";
		} else if (options.containsKey(STREAMTYPE)) {
			streamType = options.get(STREAMTYPE);
		}

		switch (streamType) {
		case "event":
			this.type = XOVISSTREAMTYPE.EVENTSTREAM;
			break;
		case "object":
			this.type = XOVISSTREAMTYPE.OBJECTSTREAM;
		}
		
	}	
	
	public DatagramSocket getInputSocket(){
		return communicator.getuDPClientSocket();
	}
}
