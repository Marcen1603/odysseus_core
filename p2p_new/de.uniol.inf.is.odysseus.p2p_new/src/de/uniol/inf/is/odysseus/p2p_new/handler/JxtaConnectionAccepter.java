package de.uniol.inf.is.odysseus.p2p_new.handler;

import java.io.IOException;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import net.jxta.socket.JxtaServerSocket;
import de.uniol.inf.is.odysseus.p2p_new.util.StoppableThread;

public class JxtaConnectionAccepter extends StoppableThread {

	private static final Logger LOG = LoggerFactory.getLogger(JxtaConnectionAccepter.class);
	
	private final JxtaServerSocket socket;
	private final JxtaTransportHandler transportHandler;
	
	public JxtaConnectionAccepter(JxtaServerSocket socket, JxtaTransportHandler transportHandler) {
		this.socket = Preconditions.checkNotNull(socket, "JXTAServerSocket must not be null!");
		this.transportHandler = Preconditions.checkNotNull(transportHandler, "TransportHandler must not be null!");
	}
	
	@Override
	public void run() {
		while( isRunning() ) {
			try {
				Socket clientSocket = socket.accept();
				transportHandler.acceptNewClientConnection(clientSocket);
			} catch (IOException ex) {
				LOG.error("Could not establish connection with jxta-socket", ex);
			}
		}
	}
}
