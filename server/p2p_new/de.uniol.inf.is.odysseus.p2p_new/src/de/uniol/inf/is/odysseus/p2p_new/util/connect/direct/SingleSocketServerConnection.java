package de.uniol.inf.is.odysseus.p2p_new.util.connect.direct;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import net.jxta.protocol.PipeAdvertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.p2p_new.activator.P2PNewPlugIn;
import de.uniol.inf.is.odysseus.p2p_new.util.IJxtaConnection;
import de.uniol.inf.is.odysseus.p2p_new.util.IJxtaConnectionListener;
import de.uniol.inf.is.odysseus.p2p_new.util.RepeatingJobThread;
import de.uniol.inf.is.odysseus.p2p_new.util.connect.AbstractJxtaServerConnection;

public class SingleSocketServerConnection extends AbstractJxtaServerConnection implements IJxtaConnectionListener {

	private static final Logger LOG = LoggerFactory.getLogger(SingleSocketServerConnection.class);
	
	private final List<IJxtaConnection> connections = Lists.newArrayList();
	
	private boolean started = false;
	private ServerSocket serverSocket;
	private RepeatingJobThread accepterThread;
	
	public SingleSocketServerConnection(int port) throws IOException {
		Preconditions.checkArgument(port > 0, "Port must be positive");
		serverSocket = new ServerSocket(port);
		serverSocket.setSoTimeout(0);
	}
	
	public SingleSocketServerConnection() throws IOException {
		serverSocket = new ServerSocket(0);
		serverSocket.setSoTimeout(0);
	}
	
	@Override
	public void start() throws IOException {
		Preconditions.checkState(isStarted() == false, "Server socket already started");

		started = true;
		accepterThread = new RepeatingJobThread(0, "Single socket server accept") {
			@Override
			public void doJob() {
				try {
					Socket clientSocket = serverSocket.accept();
					clientSocket.setSendBufferSize(P2PNewPlugIn.TRANSPORT_BUFFER_SIZE);
					IJxtaConnection connection = new SocketConnection(clientSocket);
					connection.addListener(SingleSocketServerConnection.this);
					
					connection.connect();
					fireConnectionAddEvent(connection);
					
				} catch (IOException e) {
					LOG.error("Could not accept connections", e);
				} finally {
					stopRunning();					
				}
			};
		};
		accepterThread.start();
	}

	public int getLocalPort() {
		return serverSocket.getLocalPort();
	}
	
	@Override
	public void stop() {
		Preconditions.checkState(isStarted() == true, "Server socket is already stopped");
		
		if( accepterThread != null ) {
			accepterThread.stopRunning();
			accepterThread.interrupt();
			accepterThread = null;
		}
		
		for( IJxtaConnection connection : connections ) {
			connection.disconnect();
		}
		connections.clear();
		
		started = false;
	}

	@Override
	public boolean isStarted() {
		return started;
	}

	@Override
	public ImmutableList<IJxtaConnection> getConnections() {
		return ImmutableList.copyOf(connections);
	}

	@Override
	public PipeAdvertisement getPipeAdvertisement() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void onReceiveData(IJxtaConnection sender, byte[] data) {
		// do nothing
	}

	@Override
	public void onDisconnect(IJxtaConnection sender) {
		connections.remove(sender);
		fireConnectionRemoveEvent(sender);
	}

	@Override
	public void onConnect(IJxtaConnection sender) {
		// do nothing
	}
}
