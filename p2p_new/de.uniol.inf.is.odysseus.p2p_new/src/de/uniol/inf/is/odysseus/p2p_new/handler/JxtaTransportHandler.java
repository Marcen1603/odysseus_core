package de.uniol.inf.is.odysseus.p2p_new.handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.Map;

import net.jxta.document.AdvertisementFactory;
import net.jxta.pipe.PipeID;
import net.jxta.pipe.PipeService;
import net.jxta.protocol.PipeAdvertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.p2p_new.P2PNewPlugIn;
import de.uniol.inf.is.odysseus.p2p_new.util.AbstractJxtaConnection;
import de.uniol.inf.is.odysseus.p2p_new.util.ClientJxtaConnection;
import de.uniol.inf.is.odysseus.p2p_new.util.IJxtaConnectionListener;
import de.uniol.inf.is.odysseus.p2p_new.util.ServerJxtaConnection;

public class JxtaTransportHandler extends AbstractTransportHandler implements IJxtaConnectionListener {

	public static final String NAME = "JXTA";
	public static final String PIPEID_TAG = "pipeid";

	private static final Logger LOG = LoggerFactory.getLogger(JxtaTransportHandler.class);

	private static final String PIPE_NAME = "Odysseus Pipe";

	private PipeID pipeID;

	private AbstractJxtaConnection connection;

	// for transportFactory
	public JxtaTransportHandler() {
		super();
	}

	public JxtaTransportHandler(IProtocolHandler<?> protocolHandler, Map<String, String> options) {
		super(protocolHandler);
		processOptions(options);
	}

	@Override
	public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, Map<String, String> options) {
		return new JxtaTransportHandler(protocolHandler, options);
	}

	@Override
	public InputStream getInputStream() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public OutputStream getOutputStream() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void onConnect(AbstractJxtaConnection sender) {
		LOG.debug("Connected to {}", sender.getPipeAdvertisement().getPipeID());
	}

	@Override
	public void onDisconnect(AbstractJxtaConnection sender) {
		LOG.debug("Disconnected from {}", sender.getPipeAdvertisement().getPipeID());
	}

	@Override
	public void onReceiveData(AbstractJxtaConnection sender, byte[] data) {
		LOG.debug("Got message");

		final ByteBuffer bb = ByteBuffer.allocate(data.length);
		bb.put(data);
		JxtaTransportHandler.this.fireProcess(bb);
	}

	@Override
	public void processInClose() throws IOException {
		tryDisconnectAsync();
	}

	@Override
	public void processInOpen() throws IOException {
		LOG.info("Process In Open");
		final PipeAdvertisement pipeAdvertisement = createPipeAdvertisement(pipeID);
		P2PNewPlugIn.getDiscoveryService().publish(pipeAdvertisement); // needed?

		connection = new ClientJxtaConnection(pipeAdvertisement);
		tryConnectAsync();
	}

	@Override
	public void processOutClose() throws IOException {
		tryDisconnectAsync();
	}

	@Override
	public void processOutOpen() throws IOException {
		LOG.info("Process Out Open");

		final PipeAdvertisement pipeAdvertisement = createPipeAdvertisement(pipeID);
		connection = new ServerJxtaConnection(pipeAdvertisement);

		tryConnectAsync();
	}

	@Override
	public void send(byte[] message) throws IOException {
		if (connection != null && connection.isConnected()) {
			connection.send(message);
		}
	}
	
	protected void processOptions(Map<String, String> options) {
		final String id = options.get(PIPEID_TAG);
		if (!Strings.isNullOrEmpty(id)) {
			pipeID = convertToPipeID(id);
		}
	}

	private void tryConnectAsync() {

		connection.addListener(this);
		final Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					connection.connect();
				} catch (final IOException ex) {
					LOG.error("Could not connect", ex);
					connection = null;
				}
			}
		});
		t.setName("Connect thread for " + connection.getPipeAdvertisement().getPipeID());
		t.setDaemon(true);
		t.start();
	}

	private void tryDisconnectAsync() {
		connection.removeListener(this);
		final Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				connection.disconnect();
				connection = null;
			}
		});
		t.setName("Discconnect thread for " + connection.getPipeAdvertisement().getPipeID());
		t.setDaemon(true);
		t.start();
	}

	private static PipeID convertToPipeID(String text) {
		try {
			final URI id = new URI(text);
			return PipeID.create(id);
		} catch (URISyntaxException | ClassCastException ex) {
			LOG.error("Could not transform to pipeid: {}", text, ex);
			return null;
		}
	}

	private static PipeAdvertisement createPipeAdvertisement(PipeID pipeID) {
		final PipeAdvertisement advertisement = (PipeAdvertisement) AdvertisementFactory.newAdvertisement(PipeAdvertisement.getAdvertisementType());
		advertisement.setName(PIPE_NAME);
		advertisement.setPipeID(pipeID);
		advertisement.setType(PipeService.UnicastType);
		LOG.info("Pipe Advertisement with id = {}", pipeID);
		return advertisement;
	}
}
