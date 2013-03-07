package de.uniol.inf.is.odysseus.p2p_new.handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.Map;

import net.jxta.document.AdvertisementFactory;
import net.jxta.endpoint.MessageElement;
import net.jxta.pipe.PipeID;
import net.jxta.pipe.PipeMsgEvent;
import net.jxta.pipe.PipeMsgListener;
import net.jxta.pipe.PipeService;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.socket.JxtaServerSocket;
import net.jxta.socket.JxtaSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.p2p_new.P2PNewPlugIn;
import de.uniol.inf.is.odysseus.p2p_new.util.RepeatingJobThread;

public class JxtaTransportHandler extends AbstractTransportHandler implements PipeMsgListener {

	public static final String NAME = "JXTA";
	public static final String PIPEID_TAG = "pipeid";

	private static final Logger LOG = LoggerFactory.getLogger(JxtaTransportHandler.class);

	private static final String PIPE_NAME = "Odysseus Pipe";

	// private InputPipe inputPipe;
	// private OutputPipe outputPipe;
	// private OutputPipeResolver outputPipeResolver;

	private JxtaServerSocket serverSocket;
	private Socket socket;
	private OutputStream socketOutputStream;

	private JxtaSocket clientSocket;
	private InputStream socketInputStream;

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

	@Override
	public void send(byte[] message) throws IOException {
		if (socketOutputStream != null) {
			LOG.info("Sending message");

			socketOutputStream.write(message);
			socketOutputStream.flush();
		}
	}

	@Override
	public InputStream getInputStream() {
		throw new UnsupportedOperationException();
	}

	@Override
	public OutputStream getOutputStream() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void processInOpen() throws IOException {
		LOG.info("Process In Open");
		final PipeAdvertisement pipeAdvertisement = createPipeAdvertisement(pipeID);
		P2PNewPlugIn.getDiscoveryService().publish(pipeAdvertisement); // needed?

		Thread resolverThread = new Thread() {
			@Override
			public void run() {
				try {
					while( true ) {
						try {
							clientSocket = new JxtaSocket(P2PNewPlugIn.getOwnPeerGroup(), pipeAdvertisement, 5000);
							clientSocket.setSoTimeout(0);
							LOG.debug("Client socket created: {}", clientSocket);
		
							socketInputStream = clientSocket.getInputStream();
							final byte[] buffer = new byte[1024];
							RepeatingJobThread readingDataThread = new RepeatingJobThread(0) {
								@Override
								public void doJob() {
									try {
										int bytesRead = socketInputStream.read(buffer);
										ByteBuffer bb = ByteBuffer.allocate(bytesRead);
										bb.put(buffer, 0, bytesRead);
										JxtaTransportHandler.this.fireProcess(bb);
									} catch (IOException ex) {
										LOG.error("Could not read bytes from input stream", ex);
										stopRunning();
									}
								}
							};
							readingDataThread.start();
						} catch (SocketTimeoutException ex) {
							// ignore... try again
						}
					}
				} catch( IOException ex ) {
					LOG.error("Could not get client socket", ex);
				}
			}
		};
		resolverThread.setDaemon(true);
		resolverThread.setName("ClientSocket resolver");
		resolverThread.start();

	}

	@Override
	public void processOutOpen() throws IOException {
		LOG.info("Process Out Open");

		final PipeAdvertisement pipeAdvertisement = createPipeAdvertisement(pipeID);

		serverSocket = new JxtaServerSocket(P2PNewPlugIn.getOwnPeerGroup(), pipeAdvertisement);
		serverSocket.setSoTimeout(0);
		Thread socketResolver = new Thread() {
			public void run() {
				try {
					socket = serverSocket.accept();

					socketOutputStream = socket.getOutputStream();

				} catch (IOException ex) {
					LOG.error("Could not create socket", ex);
				}
			};
		};
		socketResolver.setDaemon(true);
		socketResolver.setName("JxtaSocketResolver");
		socketResolver.start();
	}

	@Override
	public void processInClose() throws IOException {
		if (clientSocket != null) {
			clientSocket.close();
		}
	}

	@Override
	public void processOutClose() throws IOException {
		if (socket != null) {
			socket.close();
		}

		if (serverSocket != null) {
			serverSocket.close();
		}
	}

	@Override
	public void pipeMsgEvent(PipeMsgEvent event) {
		LOG.info("Got pipe event");

		MessageElement messageElement = event.getMessage().getMessageElement("DATA");
		byte[] data = messageElement.getBytes(false);

		ByteBuffer bb = ByteBuffer.allocate(data.length);
		bb.put(data);
		super.fireProcess(bb);
	}

	protected void processOptions(Map<String, String> options) {
		String id = options.get(PIPEID_TAG);
		if (!Strings.isNullOrEmpty(id)) {
			pipeID = convertToPipeID(id);
		}
	}

	private static PipeAdvertisement createPipeAdvertisement(PipeID pipeID) {
		PipeAdvertisement advertisement = (PipeAdvertisement) AdvertisementFactory.newAdvertisement(PipeAdvertisement.getAdvertisementType());
		advertisement.setName(PIPE_NAME);
		advertisement.setPipeID(pipeID);
		advertisement.setType(PipeService.UnicastType);
		LOG.info("Pipe Advertisement with id = {}", pipeID);
		return advertisement;
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
}
