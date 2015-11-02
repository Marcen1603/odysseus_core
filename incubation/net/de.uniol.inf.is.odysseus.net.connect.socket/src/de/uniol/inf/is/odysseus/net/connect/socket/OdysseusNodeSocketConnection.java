package de.uniol.inf.is.odysseus.net.connect.socket;

import java.io.IOException;
import java.net.Socket;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.connect.AbstractOdysseusNodeConnection;
import de.uniol.inf.is.odysseus.net.connect.OdysseusNetConnectionException;

public class OdysseusNodeSocketConnection extends AbstractOdysseusNodeConnection implements ISocketReceiveThreadListener {

	private final SocketReceiveThread receiveThread;
	private final IOdysseusNode node;

	public OdysseusNodeSocketConnection(Socket clientSocket, IOdysseusNode node) throws IOException {
		Preconditions.checkNotNull(clientSocket, "clientSocket must not be null!");
		Preconditions.checkNotNull(node, "node must not be null!");

		this.node = node;

		receiveThread = new SocketReceiveThread(clientSocket, this);
		receiveThread.start();
	}

	@Override
	public IOdysseusNode getOdysseusNode() {
		return node;
	}

	@Override
	public void send(byte[] data) throws OdysseusNetConnectionException {
		receiveThread.write(data);
	}

	// called async!
	@Override
	public void bytesReceived(byte[] data) {
		fireMessageReceivedEvent(data);
	}

	// called from SocketReceiveThread
	@Override
	public void socketDisconnected() {
		disconnect();
	}

	@Override
	public void disconnect() {
		receiveThread.stopRunning();

		try {
			receiveThread.getClientSocket().close();
		} catch (IOException e) {
		}
		
		fireDisconnectedEvent();
	}

}
