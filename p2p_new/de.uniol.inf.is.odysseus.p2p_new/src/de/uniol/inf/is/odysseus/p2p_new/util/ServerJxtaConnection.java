package de.uniol.inf.is.odysseus.p2p_new.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import net.jxta.protocol.PipeAdvertisement;
import net.jxta.socket.JxtaServerSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.P2PNewPlugIn;

public class ServerJxtaConnection extends AbstractJxtaConnection {

	private static final Logger LOG = LoggerFactory.getLogger(ServerJxtaConnection.class);
	
	private JxtaServerSocket serverSocket;
	private Socket socket;

	public ServerJxtaConnection(PipeAdvertisement pipeAdvertisement) {
		super(pipeAdvertisement);
	}

	@Override
	public void connect() throws IOException {

		serverSocket = new JxtaServerSocket(P2PNewPlugIn.getOwnPeerGroup(), getPipeAdvertisement());
		serverSocket.setSoTimeout(0);
		Thread socketResolver = new Thread() {
			public void run() {
				try {
					socket = serverSocket.accept();
				} catch (IOException ex) {
					LOG.error("Could not create socket", ex);
				}
			};
		};
		socketResolver.setDaemon(true);
		socketResolver.setName("ServerSocket Resolver " +getPipeAdvertisement().getPipeID().toString());
		socketResolver.start();

		super.connect();
	}

	@Override
	public void disconnect() {
		if (serverSocket != null) {
			tryClose(serverSocket);
		}

		super.disconnect();
	}

	@Override
	protected InputStream getInputStream() throws IOException {
		return socket.getInputStream();
	}

	@Override
	protected OutputStream getOutputStream() throws IOException {
		return socket.getOutputStream();
	}

	private static void tryClose(JxtaServerSocket socket) {
		try {
			socket.close();
		} catch (IOException ex) {
		}
	}

}
