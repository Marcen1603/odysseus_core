package de.uniol.inf.is.odysseus.p2p_new.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import net.jxta.protocol.PipeAdvertisement;
import net.jxta.socket.JxtaServerSocket;
import de.uniol.inf.is.odysseus.p2p_new.P2PNewPlugIn;

public class ServerJxtaConnection extends AbstractJxtaConnection {

	private JxtaServerSocket serverSocket;

	private Socket socket;

	public ServerJxtaConnection(PipeAdvertisement pipeAdvertisement) {
		super(pipeAdvertisement);
	}

	@Override
	public void connect() throws IOException {

		serverSocket = new JxtaServerSocket(P2PNewPlugIn.getOwnPeerGroup(), getPipeAdvertisement());
		serverSocket.setSoTimeout(0);

		socket = serverSocket.accept();
		socket.setSoTimeout(0);

		super.connect();
	}

	@Override
	public void disconnect() {
		super.disconnect();

		if (serverSocket != null) {
			tryClose(serverSocket);
		}
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
		} catch (final IOException ex) {
		}
	}

}
