package de.uniol.inf.is.odysseus.net.connect.socket;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.connect.AbstractOdysseusNodeConnection;
import de.uniol.inf.is.odysseus.net.connect.OdysseusNetConnectionException;

public class OdysseusNodeSocketConnection extends AbstractOdysseusNodeConnection implements ISocketReceiveThreadListener {

	private static final Logger LOG = LoggerFactory.getLogger(OdysseusNodeSocketConnection.class);
	
	private final SocketReceiveThread receiveThread;
	private final OutputStream outputStream;
	
	private final IOdysseusNode node;
	
	private final Socket asClientSocket;
	private final Socket asServerSocket;

	public OdysseusNodeSocketConnection(Socket asClientSocket, Socket asServerSocket, IOdysseusNode node) throws IOException {
		Preconditions.checkNotNull(asClientSocket, "asClientSocket must not be null!");
		Preconditions.checkNotNull(asServerSocket, "asServerSocket must not be null!");
		Preconditions.checkNotNull(node, "node must not be null!");

		this.node = node;
		this.outputStream = asServerSocket.getOutputStream();
		this.asClientSocket = asClientSocket;
		this.asServerSocket = asServerSocket;

		receiveThread = new SocketReceiveThread(asClientSocket, this);
		receiveThread.start();
		
		LOG.debug("Created OdysseusNodeConnection to {}", node);
	}

	@Override
	public IOdysseusNode getOdysseusNode() {
		return node;
	}

	@Override
	public void send(byte[] data) throws OdysseusNetConnectionException {
		try {
			outputStream.write(data);
			outputStream.flush();
		} catch (IOException e) {
			throw new OdysseusNetConnectionException("Could not write to socket for node " + node, e);
		}
	}

	// called from SocketReceiveThread
	@Override
	public void bytesReceived(byte[] data) {
		fireMessageReceivedEvent(data);
	}

	// called from SocketReceiveThread
	@Override
	public void socketDisconnected() {
		LOG.debug("DisconnectedEvent in OdysseusNodeConnection for {}", node);
		
		disconnect();
	}

	@Override
	public void disconnect() {
		LOG.debug("Disconnecting in OdysseusNodeConnection for {}", node);
		
		receiveThread.stopRunning();

		tryCloseSocket(asServerSocket);
		tryCloseSocket(asClientSocket);
		
		fireDisconnectedEvent();
	}

	private static void tryCloseSocket(Socket socket) {
		if( socket != null ) {
			try {
				socket.close();
			} catch (IOException ignored) {
			}
		}
	}

}