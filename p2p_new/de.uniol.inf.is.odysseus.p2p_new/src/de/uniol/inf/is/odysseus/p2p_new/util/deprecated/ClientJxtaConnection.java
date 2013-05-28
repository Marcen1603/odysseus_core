package de.uniol.inf.is.odysseus.p2p_new.util.deprecated;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketTimeoutException;

import net.jxta.protocol.PipeAdvertisement;
import net.jxta.socket.JxtaSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.dictionary.impl.P2PDictionary;

@Deprecated
public class ClientJxtaConnection extends AbstractJxtaConnection {

	private static final Logger LOG = LoggerFactory.getLogger(ClientJxtaConnection.class);

	private JxtaSocket clientSocket;

	public ClientJxtaConnection(PipeAdvertisement pipeAdvertisement) {
		super(pipeAdvertisement);
	}

	@Override
	public void connect() throws IOException {
		clientSocket = determineJxtaClientSocket(getPipeAdvertisement());
		clientSocket.setPerformancePreferences(0, 1, 2);
		super.connect();
	}

	@Override
	public void disconnect() {
		super.disconnect();

		if (clientSocket != null) {
			tryClose(clientSocket);
		}
	}

	public final JxtaSocket getJxtaSocket() {
		return clientSocket;
	}

	@Override
	protected InputStream getInputStream() throws IOException {
		return clientSocket.getInputStream();
	}

	@Override
	protected OutputStream getOutputStream() throws IOException {
		return clientSocket.getOutputStream();
	}

	private static JxtaSocket determineJxtaClientSocket(PipeAdvertisement pipeAdvertisement) throws IOException {
		JxtaSocket clientSocket = null;
		while (clientSocket == null) {
			try {
				clientSocket = new JxtaSocket(P2PDictionary.getInstance().getLocalPeerGroup(), pipeAdvertisement, 5000);
				clientSocket.setSoTimeout(0);
				LOG.debug("Client socket created: {}", clientSocket);
			} catch (final SocketTimeoutException ex) {
				// ignore... try again
				// timeout of zero does not work here...
			}
		}
		return clientSocket;
	}

	private static void tryClose(JxtaSocket socket) {
		try {
			socket.close();
		} catch (final IOException ex) {
		}
	}
}
