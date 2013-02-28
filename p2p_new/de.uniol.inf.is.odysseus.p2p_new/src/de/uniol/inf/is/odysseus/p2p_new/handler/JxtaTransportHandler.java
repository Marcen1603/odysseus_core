package de.uniol.inf.is.odysseus.p2p_new.handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.document.AdvertisementFactory;
import net.jxta.id.ID;
import net.jxta.peergroup.PeerGroupID;
import net.jxta.pipe.PipeID;
import net.jxta.pipe.PipeService;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.socket.JxtaServerSocket;
import net.jxta.socket.JxtaSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.p2p_new.P2PNewPlugIn;

public class JxtaTransportHandler extends AbstractTransportHandler {

	private static final int ADVERTISEMENT_CALL_INTERVAL = 3000;

	private static final Logger LOG = LoggerFactory.getLogger(JxtaTransportHandler.class);

	private static final String PIPE_NAME = "Odysseus Pipe";
	private static final String PIPEID_TAG = "pipeid";

	private final List<JxtaConnection> connections = Lists.newArrayList();

	private JxtaServerSocket socket;
	private JxtaSocket clientSocket;
	
	private JxtaConnectionAccepter accepter;
	private JxtaConnection toServerConnection;
	
	private PipeID pipeID;

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

	public void acceptNewClientConnection(Socket clientSocket) {
		JxtaConnection handler = new JxtaConnection(clientSocket, this);
		handler.start();

		synchronized (connections) {
			connections.add(handler);
		}
	}

	@Override
	public void send(byte[] message) throws IOException {
		synchronized (connections) {
			for (JxtaConnection connection : connections) {
				connection.send(message);
			}
		}
	}

	public final void receive(byte[] data) {
		ByteBuffer bb = ByteBuffer.wrap(data);
		fireProcess(bb);
	}

	@Override
	public InputStream getInputStream() {
		if( clientSocket != null ) {
			try {
				return clientSocket.getInputStream();
			} catch (IOException ex) {
				LOG.error("Could not get inputstream of jxta client socket", ex);
				return null;
			}
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public OutputStream getOutputStream() {
		if( clientSocket != null ) {
			try {
				return clientSocket.getOutputStream();
			} catch (IOException ex) {
				LOG.error("Could not get outputstream of jxta client socket", ex);
				return null;
			}
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public String getName() {
		return "JXTA";
	}

	@Override
	public void processInOpen() throws IOException {
		PipeAdvertisement pipeAdvertisement = getPipeAdvertisement(pipeID);
		clientSocket = new JxtaSocket(P2PNewPlugIn.getOwnPeerGroup(), null, pipeAdvertisement, 30000);
		
		toServerConnection = new JxtaConnection(clientSocket, this);
		toServerConnection.start();
	}

	@Override
	public void processOutOpen() throws IOException {
		socket = createNewJxtaServerSocket(pipeID);
		accepter = new JxtaConnectionAccepter(socket, this);

		accepter.start();
	}

	@Override
	public void processInClose() throws IOException {
		toServerConnection.stopRunning();
		clientSocket.close();
	}

	@Override
	public void processOutClose() throws IOException {
		accepter.stopRunning();
		tryConnectionsClose(connections);
		trySocketClose(socket);
	}

	protected void processOptions(Map<String, String> options) {
		String id = options.get(PIPEID_TAG);
		if (!Strings.isNullOrEmpty(id)) {
			pipeID = convertToPipeID(id);
		}
	}

	private static JxtaServerSocket createNewJxtaServerSocket(PipeID pipeID) {
		try {
			JxtaServerSocket socket = new JxtaServerSocket(P2PNewPlugIn.getOwnPeerGroup(), createPipeAdvertisement(P2PNewPlugIn.getOwnPeerGroup().getPeerGroupID(), pipeID));
			socket.setSoTimeout(0);
			return socket;
		} catch (IOException ex) {
			LOG.error("Could not create JxtaServerSocket", ex);
			return null;
		}
	}

	private static PipeAdvertisement createPipeAdvertisement(PeerGroupID group, ID pipeID) {
		PipeAdvertisement advertisement = (PipeAdvertisement) AdvertisementFactory.newAdvertisement(PipeAdvertisement.getAdvertisementType());
		advertisement.setName(PIPE_NAME);
		advertisement.setPipeID(pipeID);
		advertisement.setType(PipeService.UnicastSecureType);
		return advertisement;
	}

	private static PipeAdvertisement getPipeAdvertisement(PipeID pipeID) {
		try {
			while( true ) {
				Enumeration<Advertisement> advertisements = P2PNewPlugIn.getDiscoveryService().getLocalAdvertisements(DiscoveryService.ADV, PipeAdvertisement.IdTag, pipeID.toString());
				while( advertisements.hasMoreElements() ) {
					Advertisement advertisement = advertisements.nextElement();
					if( advertisement instanceof PipeAdvertisement ) {
						return (PipeAdvertisement)advertisement;
					}
				}
				
				trySleep();
			}
		} catch (IOException ex) {
			LOG.error("Could not determine pipe advertisement with id {}", pipeID);
			return null;
		}
	}

	private static void trySleep() {
		try {
			Thread.sleep(ADVERTISEMENT_CALL_INTERVAL);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}
	
	private static PipeID convertToPipeID(String text) {
		try {
			URI id = new URI(text);
			return PipeID.create(id);
		} catch (URISyntaxException | ClassCastException ex) {
			LOG.error("Could not transform to pipeid: {}", text, ex);
			return null;
		}
	}

	private static void tryConnectionsClose(List<JxtaConnection> connections) {
		synchronized (connections) {
			for (JxtaConnection connection : connections) {
				connection.stopRunning();
				trySocketClose(connection.getSocket());
			}

			connections.clear();
		}
	}

	private static void trySocketClose(Socket socket) {
		try {
			socket.close();
		} catch (IOException ex) {
		}
	}

	private static void trySocketClose(JxtaServerSocket socket) {
		try {
			socket.close();
		} catch (IOException ex) {
		}
	}

}
